package com.example.smarthome

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat as CoreContextCompat
import java.util.Locale
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var myWebView: WebView
    private val RECORD_AUDIO_PERMISSION_CODE = 101
    private val NOTIFICATION_PERMISSION_CODE = 102

    // Hold a reference to a pending web PermissionRequest while we ask Android runtime permission
    private var pendingPermissionRequest: PermissionRequest? = null

    // TextToSpeech for speaking strings requested by the web page (Web Speech API polyfill)
    private lateinit var tts: TextToSpeech
    private var ttsInitialized = false

    // Queue utterances that arrive before TTS initialization completes
    private val ttsQueue: MutableList<String> = mutableListOf()

    // Native speech recognizer for implementing webkitSpeechRecognition polyfill
    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false

    // Biometric authentication
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ensure this matches your layout file (e.g., activity_main.xml)

        myWebView = findViewById(R.id.webview) // IMPORTANT: Make sure your WebView in layout has id="@+id/webview"

        // Clear WebView cache to force reload of updated HTML files
        myWebView.clearCache(true)
        myWebView.clearHistory()

        // Configure WebView settings
        myWebView.settings.javaScriptEnabled = true // Enable JavaScript for your HTML to function
        myWebView.settings.domStorageEnabled = true // Essential for web storage APIs
        myWebView.settings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE // Disable caching during development
        // Allow media playback without user gesture for speech synthesis / audio capture
        myWebView.settings.mediaPlaybackRequiresUserGesture = false
        // Allow local file access (required when loading from assets). Required flags for file:// origins.
        myWebView.settings.allowFileAccess = true

        // Use reflection to set file access-from-file-URLs and universal-access-from-file-URLs
        // This avoids compile-time deprecation warnings while preserving behavior on older API levels.
        try {
            val settingsObj = myWebView.settings
            // setAllowFileAccessFromFileURLs(boolean)
            try {
                val method = settingsObj.javaClass.getMethod("setAllowFileAccessFromFileURLs", java.lang.Boolean.TYPE)
                method.invoke(settingsObj, true)
            } catch (e: Exception) {
                Log.w("WebViewSettings", "setAllowFileAccessFromFileURLs not available: $e")
            }

            // setAllowUniversalAccessFromFileURLs(boolean)
            try {
                val method2 = settingsObj.javaClass.getMethod("setAllowUniversalAccessFromFileURLs", java.lang.Boolean.TYPE)
                method2.invoke(settingsObj, true)
            } catch (e: Exception) {
                Log.w("WebViewSettings", "setAllowUniversalAccessFromFileURLs not available: $e")
            }
        } catch (e: Exception) {
            Log.w("WebViewSettings", "Failed to set file URL access flags via reflection: $e")
        }

        // Enable WebView debug for easier troubleshooting (only in debug builds)
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)

        // Initialize TTS engine to provide speech output for pages that use the Web Speech API
        initTextToSpeech()

        // Initialize Biometric authentication
        initBiometric()

        // Set a WebViewClient to handle page navigation within the WebView and inject polyfills
        myWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Inject a small polyfill for the Web Speech API (speechSynthesis.speak)
                // and a polyfill for SpeechRecognition (webkitSpeechRecognition).
                val js = """
                    (function(){
                      try{
                        if (window._nativeSpeechPolyfilled) return;
                        window._nativeSpeechPolyfilled = true;

                        // TTS polyfill
                        window.speechSynthesis = window.speechSynthesis || {};
                        window.speechSynthesis.speak = function(utterance){
                          try{
                            var text = '';
                            if (typeof utterance === 'string') text = utterance;
                            else if (utterance && utterance.text) text = utterance.text;
                            else text = String(utterance || '');
                            AndroidTTS.speak(text);
                          }catch(e){console && console.log && console.log('TTS polyfill speak error', e);}                          
                        };
                        window.speechSynthesis.cancel = function(){ try{ AndroidTTS.cancel(); }catch(e){} };
                        window.speechSynthesis.getVoices = function(){ return []; };

                        // SpeechRecognition polyfill (basic). Maps webkitSpeechRecognition -> Android STT via AndroidSTT bridge.
                        function PolyfillRecognition(){
                          this.continuous = false;
                          this.lang = 'en-US';
                          this.interimResults = false;
                          this.maxAlternatives = 1;
                          this.onstart = null;
                          this.onresult = null;
                          this.onerror = null;
                          this.onend = null;
                        }
                        PolyfillRecognition.prototype.start = function(){
                          try{
                            if (typeof this.onstart === 'function') this.onstart();
                            // Inform Android to start listening. Android will call window.__androidRecognitionCallback with a JSON payload.
                            AndroidTTS.startListening();
                          }catch(e){ if (typeof this.onerror === 'function') this.onerror({error: e}); }
                        };
                        PolyfillRecognition.prototype.stop = function(){
                          try{ AndroidTTS.stopListening(); }catch(e){}
                          if (typeof this.onend === 'function') this.onend();
                        };
                        PolyfillRecognition.prototype.abort = function(){ try{ AndroidTTS.stopListening(); }catch(e){} if (typeof this.onend === 'function') this.onend(); };

                        window.SpeechRecognition = window.SpeechRecognition || PolyfillRecognition;
                        window.webkitSpeechRecognition = window.webkitSpeechRecognition || PolyfillRecognition;

                        // A global callback the native side will call with recognition results/errors
                        window.__androidRecognitionCallback = function(payload){
                          try{
                            var obj = (typeof payload === 'string') ? JSON.parse(payload) : payload;
                            // expected obj = { type: 'result'|'error'|'end'|'start', results: [[{transcript, confidence}], ...], error: 'message' }
                            if (!window._currentRecognition) return;
                            var rec = window._currentRecognition;
                            if (obj.type === 'result') {
                              if (typeof rec.onresult === 'function') {
                                var event = { results: obj.results };
                                rec.onresult(event);
                              }
                            } else if (obj.type === 'error') {
                              if (typeof rec.onerror === 'function') rec.onerror({error: obj.error});
                              if (typeof rec.onend === 'function') rec.onend();
                            } else if (obj.type === 'start') {
                              if (typeof rec.onstart === 'function') rec.onstart();
                            } else if (obj.type === 'end') {
                              if (typeof rec.onend === 'function') rec.onend();
                            }
                          }catch(e){ console && console.log && console.log('androidRecognitionCallback error', e); }
                        };

                        // When a page creates a new recognition, keep a global reference so callbacks can target it.
                        var OriginalConstructor = window.SpeechRecognition || window.webkitSpeechRecognition;
                        window.SpeechRecognition = function(){
                          var r = new OriginalConstructor();
                          window._currentRecognition = r;
                          return r;
                        };
                        window.webkitSpeechRecognition = window.SpeechRecognition;

                      }catch(e){console && console.log && console.log('speech polyfill injection error', e);}                      
                    })();
                """.trimIndent()
                view?.evaluateJavascript(js, null)
            }
        }

        // Set a WebChromeClient to handle browser-like events, including permission requests
        myWebView.webChromeClient = object : WebChromeClient() {
            // Log JS console messages to Android logcat for debugging
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                if (consoleMessage != null) {
                    Log.d("WebViewConsole", "${consoleMessage.message()} -- From line ${consoleMessage.lineNumber()} of ${consoleMessage.sourceId()}")
                }
                return true
            }

            // This method is called when the web content requests a permission (e.g., microphone)
            override fun onPermissionRequest(request: PermissionRequest?) {
                if (request == null) {
                    super.onPermissionRequest(request)
                    return
                }

                // Check if the requested permission is for audio capture (microphone)
                for (resource in request.resources) {
                    if (resource == PermissionRequest.RESOURCE_AUDIO_CAPTURE) {
                        // If the app already has RECORD_AUDIO permission, grant it to the web content
                        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                            request.grant(request.resources)
                        } else {
                            // Save the web PermissionRequest and ask the user for Android runtime permission.
                            // Do NOT call request.deny() here — wait for the user's answer and then grant/deny accordingly.
                            pendingPermissionRequest = request
                            ActivityCompat.requestPermissions(this@MainActivity,
                                arrayOf(Manifest.permission.RECORD_AUDIO),
                                RECORD_AUDIO_PERMISSION_CODE)
                        }
                        return // Handled this permission request
                    }
                }
                // Deny any other permissions or if audio capture wasn't requested
                request.deny()
            }
        }

        // Expose the TTS and STT bridge to JavaScript as "AndroidTTS"
        myWebView.addJavascriptInterface(WebAppInterface(), "AndroidTTS")

        // Load your HTML file from the 'assets' folder
        myWebView.loadUrl("file:///android_asset/index.html")

        // Check and request permissions
        checkAndRequestAudioPermission()
        checkAndRequestNotificationPermission()
        
        // Start background monitoring service
        startAlarmMonitoringService()
    }
    
    private fun startAlarmMonitoringService() {
        val serviceIntent = Intent(this, AlarmMonitoringService::class.java).apply {
            action = AlarmMonitoringService.ACTION_START_MONITORING
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        Log.d("MainActivity", "Alarm monitoring service started")
    }
    
    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE)
            }
        }
    }

    // Helper function to programmatically check and request the RECORD_AUDIO permission
    private fun checkAndRequestAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_CODE)
        }
    }

    // Callback for when the user responds to the Android runtime permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Microphone permission granted by the user.
                // If there was a pending web PermissionRequest, grant it now.
                pendingPermissionRequest?.grant(arrayOf(PermissionRequest.RESOURCE_AUDIO_CAPTURE))
                pendingPermissionRequest = null

                // Optionally reload the WebView to re-evaluate permissions in the page
                // myWebView.reload()
            } else {
                // Microphone permission denied. Deny any pending web PermissionRequest and inform the user if needed.
                pendingPermissionRequest?.deny()
                pendingPermissionRequest = null
                // You might want to inform the user here using a Toast or dialog.
            }
        }
    }

    // Initialize Android TextToSpeech engine
    private fun initTextToSpeech() {
        Log.d("TTS", "Initializing TextToSpeech engine...")
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                Log.d("TTS", "TextToSpeech engine initialized successfully")
                val res = tts.setLanguage(Locale.US)
                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                    ttsInitialized = false
                    Log.e("TTS", "English (US) language not supported for TTS")
                } else {
                    ttsInitialized = true
                    Log.d("TTS", "Language set to English (US) successfully")
                    
                    // Ensure TTS uses media audio attributes so it is audible with normal volume levels
                    try {
                        val attrs = AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build()
                        tts.setAudioAttributes(attrs)
                        Log.d("TTS", "Audio attributes set successfully")
                    } catch (e: Exception) {
                        Log.e("TTS", "Failed to set audio attributes: $e")
                    }
                    
                    // Set speech rate and pitch for better clarity
                    try {
                        tts.setSpeechRate(0.9f)
                        tts.setPitch(1.0f)
                        Log.d("TTS", "Speech rate and pitch configured")
                    } catch (e: Exception) {
                        Log.w("TTS", "Failed to set speech rate/pitch: $e")
                    }

                    // Speak any queued utterances that arrived before initialization
                    if (ttsQueue.isNotEmpty()) {
                        Log.d("TTS", "Processing ${ttsQueue.size} queued utterances")
                        val copy = ArrayList(ttsQueue)
                        ttsQueue.clear()
                        for (u in copy) {
                            try {
                                tts.speak(u, TextToSpeech.QUEUE_ADD, null, "web_utterance_${System.currentTimeMillis()}")
                                Log.d("TTS", "Spoke queued utterance: $u")
                            } catch (ex: Exception) {
                                Log.e("TTS", "Failed to speak queued utterance: $ex")
                            }
                        }
                    }
                }
            } else {
                ttsInitialized = false
                Log.e("TTS", "TextToSpeech initialization failed with status: $status")
            }
        }
    }

    // Initialize Biometric authentication
    private fun initBiometric() {
        executor = CoreContextCompat.getMainExecutor(this)
        
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    runOnUiThread {
                        val jsCode = "if (typeof window.__androidBiometricCallback === 'function') window.__androidBiometricCallback({success: false, error: '$errString'});"
                        myWebView.evaluateJavascript(jsCode, null)
                    }
                    Log.e("Biometric", "Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    runOnUiThread {
                        val jsCode = "if (typeof window.__androidBiometricCallback === 'function') window.__androidBiometricCallback({success: true});"
                        myWebView.evaluateJavascript(jsCode, null)
                    }
                    Log.d("Biometric", "Authentication succeeded")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.w("Biometric", "Authentication failed")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Unlock Smart Home App")
            .setNegativeButtonText("Use PIN instead")
            .build()
    }

    // JavaScript interface to expose TTS and STT methods to the page
    @Suppress("unused")
    inner class WebAppInterface {
         @JavascriptInterface
         fun speak(text: String?) {
             if (text == null || text.isEmpty()) {
                 Log.w("TTS", "speak called with null or empty text")
                 return
             }

             Log.d("TTS", "speak called with text: $text")
             
             // If TTS is not ready yet, queue the utterance and return early
             if (!ttsInitialized) {
                 Log.w("TTS", "TTS not initialized yet, queueing utterance: $text")
                 synchronized(ttsQueue) {
                     ttsQueue.add(text)
                 }
                 return
             }

             // Speak on UI thread
             runOnUiThread {
                 try {
                     val utteranceId = "web_utterance_${System.currentTimeMillis()}"
                     tts.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId)
                     Log.d("TTS", "Successfully queued speech: $text (ID: $utteranceId)")
                 } catch (e: Exception) {
                     Log.e("TTS", "speak failed for text '$text': $e")
                 }
             }
         }

         @JavascriptInterface
         fun cancel() {
             if (!ttsInitialized) return
             tts.stop()
             synchronized(ttsQueue) { ttsQueue.clear() }
         }

         @JavascriptInterface
         fun isSpeaking(): Boolean {
             return ttsInitialized && tts.isSpeaking
         }

         // STT controls called from JS polyfill
         @JavascriptInterface
         fun startListening() {
             runOnUiThread { startNativeListening() }
         }

         @JavascriptInterface
         fun stopListening() {
             runOnUiThread { stopNativeListening() }
         }
         
         // Biometric authentication
         @JavascriptInterface
         fun authenticateBiometric() {
             runOnUiThread {
                 val biometricManager = BiometricManager.from(this@MainActivity)
                 when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
                     BiometricManager.BIOMETRIC_SUCCESS -> {
                         Log.d("Biometric", "App can authenticate using biometrics")
                         biometricPrompt.authenticate(promptInfo)
                     }
                     BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                         Log.e("Biometric", "No biometric features available on this device")
                         val jsCode = "if (typeof window.__androidBiometricCallback === 'function') window.__androidBiometricCallback({success: false, error: 'No biometric hardware'});"
                         myWebView.evaluateJavascript(jsCode, null)
                     }
                     BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                         Log.e("Biometric", "Biometric features are currently unavailable")
                         val jsCode = "if (typeof window.__androidBiometricCallback === 'function') window.__androidBiometricCallback({success: false, error: 'Biometric hardware unavailable'});"
                         myWebView.evaluateJavascript(jsCode, null)
                     }
                     BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                         Log.e("Biometric", "No biometric credentials enrolled")
                         val jsCode = "if (typeof window.__androidBiometricCallback === 'function') window.__androidBiometricCallback({success: false, error: 'No biometric enrolled'});"
                         myWebView.evaluateJavascript(jsCode, null)
                     }
                 }
             }
         }
         
         @JavascriptInterface
         fun isBiometricAvailable(): Boolean {
             val biometricManager = BiometricManager.from(this@MainActivity)
             val result = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
             return result == BiometricManager.BIOMETRIC_SUCCESS
         }
         
         // Alarm notification methods - called from JavaScript when alarms trigger
         @JavascriptInterface
         fun notifyFireAlarm(isActive: Boolean) {
             runOnUiThread {
                 val intent = Intent(this@MainActivity, AlarmMonitoringService::class.java)
                 if (isActive) {
                     intent.action = AlarmMonitoringService.ACTION_FIRE_ALARM
                 } else {
                     intent.action = AlarmMonitoringService.ACTION_ALARM_CLEARED
                     intent.putExtra("alarm_type", "fire")
                 }
                 startService(intent)
                 Log.d("WebInterface", "Fire alarm notification: $isActive")
             }
         }
         
         @JavascriptInterface
         fun notifyFaultAlarm(isActive: Boolean) {
             runOnUiThread {
                 val intent = Intent(this@MainActivity, AlarmMonitoringService::class.java)
                 if (isActive) {
                     intent.action = AlarmMonitoringService.ACTION_FAULT_ALARM
                 } else {
                     intent.action = AlarmMonitoringService.ACTION_ALARM_CLEARED
                     intent.putExtra("alarm_type", "fault")
                 }
                 startService(intent)
                 Log.d("WebInterface", "Fault alarm notification: $isActive")
             }
         }
     }

    // Start Android native speech recognition and forward events/results to the web page
    @SuppressLint("MissingPermission")
    private fun startNativeListening() {
        if (isListening) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Request permission; the pending web PermissionRequest (if any) will be handled elsewhere
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_PERMISSION_CODE)
            return
        }

        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            sendRecognitionErrorToJs("Speech recognition not available")
            return
        }

        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    sendRecognitionEventToJs("start")
                }

                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {
                    // sendRecognitionEventToJs("end")
                }

                override fun onError(error: Int) {
                    val msg = when (error) {
                        SpeechRecognizer.ERROR_NETWORK -> "network"
                        SpeechRecognizer.ERROR_AUDIO -> "audio"
                        SpeechRecognizer.ERROR_CLIENT -> "client"
                        SpeechRecognizer.ERROR_SERVER -> "server"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "timeout"
                        SpeechRecognizer.ERROR_NO_MATCH -> "no_match"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "busy"
                        else -> "unknown"
                    }
                    sendRecognitionErrorToJs(msg)
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val confidences = results?.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)
                    val resArray = ArrayList<Map<String, Any>>()
                    if (matches != null && matches.isNotEmpty()) {
                        for (i in matches.indices) {
                            val transcript = matches[i]
                            val conf = confidences?.getOrNull(i)?.toDouble() ?: 0.0
                            val item: Map<String, Any> = mapOf("transcript" to transcript, "confidence" to conf)
                            resArray.add(item)
                        }
                    }
                    // Build results in the shape expected by the page: results: [[{transcript, confidence}]]
                    val resultsForJs = if (resArray.isNotEmpty()) listOf(listOf(resArray[0])) else listOf(listOf(mapOf("transcript" to "", "confidence" to 0.0)))
                    val payload = mapOf("type" to "result", "results" to resultsForJs)
                    sendJsonToJs(payload)
                    sendRecognitionEventToJs("end")
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    // ignore for simplicity
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        }
        speechRecognizer?.startListening(intent)
        isListening = true
    }

    private fun stopNativeListening() {
        if (!isListening) return
        try {
            speechRecognizer?.stopListening()
        } catch (e: Exception) {
            Log.w("STT", "stopListening failed: $e")
        }
        isListening = false
    }

    private fun sendRecognitionEventToJs(type: String) {
        val payload = mutableMapOf<String, Any?>("type" to type)
        sendJsonToJs(payload)
    }

    private fun sendRecognitionErrorToJs(errorMsg: String) {
        val payload = mapOf("type" to "error", "error" to errorMsg)
        sendJsonToJs(payload)
    }

    private fun sendJsonToJs(obj: Any) {
        val json = JsonUtil.toJson(obj)
        val js = "window.__androidRecognitionCallback(${JsonUtil.escapeForJavascript(json)});"
        runOnUiThread {
            try {
                myWebView.evaluateJavascript(js, null)
            } catch (e: Exception) {
                Log.w("STT", "Failed to send JSON to JS: $e")
            }
        }
    }

    override fun onDestroy() {
        // Shutdown TTS to free resources
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        speechRecognizer?.destroy()
        speechRecognizer = null
        super.onDestroy()
    }
}

// Minimal JSON utility to avoid adding dependencies (serialize simple maps/lists)
object JsonUtil {
    fun toJson(obj: Any?): String {
        return when (obj) {
            null -> "null"
            is String -> escapeString(obj)
            is Number, is Boolean -> obj.toString()
            is Map<*, *> -> {
                val entries = obj.entries.joinToString(",") { "\"${it.key}\":${toJson(it.value)}" }
                "{${entries}}"
            }
            is List<*> -> {
                val entries = obj.joinToString(",") { toJson(it) }
                "[${entries}]"
            }
            else -> escapeString(obj.toString())
        }
    }

    private fun escapeString(s: String): String {
        val esc = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r")
        return "\"$esc\""
    }

    // Wraps a JSON string safely so it can be placed inside evaluateJavascript call
    fun escapeForJavascript(json: String): String {
        // evaluateJavascript expects a JS expression; the JSON string must be passed as a JS string literal or literal object.
        // We'll pass it as a string literal and let the JS side JSON.parse.
        val escaped = json.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r")
        return "JSON.parse(\"$escaped\")"
    }
}
