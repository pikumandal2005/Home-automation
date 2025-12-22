# ESP Code Implementation Guide

## Problem Solved
The Android app was incorrectly showing the ESP as offline even when it was running, because:
1. ESP only sends MQTT messages when device states change
2. No mechanism to actively check if ESP is responding

## Solutions Implemented

### Solution 1: Status Request Handler (REQUIRED)
The app can now request status from your ESP. Your ESP must respond to these requests.

### Solution 2: Heartbeat (OPTIONAL but recommended)
ESP sends periodic "I'm alive" messages.

---

## REQUIRED: Add Status Request Handler

Your ESP must subscribe to "get" topics and respond with current status:

```cpp
// In your MQTT callback function, add these handlers:
void callback(char* topic, byte* payload, unsigned int length) {
    String topicStr = String(topic);
    String payloadStr = "";
    for (int i = 0; i < length; i++) {
        payloadStr += (char)payload[i];
    }
    
    // Handle status requests from app
    if (topicStr.indexOf("/get") > 0 && payloadStr == "status") {
        
        // Respond to light status requests
        if (topicStr.indexOf("light/1/get") > 0) {
            mqttClient.publish("homeautomation/project/light/1/status", light1State ? "ON" : "OFF");
        }
        else if (topicStr.indexOf("light/2/get") > 0) {
            mqttClient.publish("homeautomation/project/light/2/status", light2State ? "ON" : "OFF");
        }
        else if (topicStr.indexOf("light/3/get") > 0) {
            mqttClient.publish("homeautomation/project/light/3/status", light3State ? "ON" : "OFF");
        }
        else if (topicStr.indexOf("light/4/get") > 0) {
            mqttClient.publish("homeautomation/project/light/4/status", light4State ? "ON" : "OFF");
        }
        
        // Respond to fan status requests
        else if (topicStr.indexOf("fan/1/get") > 0) {
            mqttClient.publish("homeautomation/project/fan/1/status", String(fan1Speed).c_str());
        }
        else if (topicStr.indexOf("fan/2/get") > 0) {
            mqttClient.publish("homeautomation/project/fan/2/status", String(fan2Speed).c_str());
        }
        
        // Respond to energy status request
        else if (topicStr.indexOf("energy/get") > 0) {
            // Publish your current PZEM data
            publishEnergyData(); // Call your existing function
        }
        
        Serial.println("Status request handled: " + topicStr);
    }
    
    // ... rest of your existing callback code ...
}

// In setup(), subscribe to get topics:
void setup() {
    // ... your existing setup code ...
    
    // Subscribe to status request topics
    mqttClient.subscribe("homeautomation/project/light/1/get");
    mqttClient.subscribe("homeautomation/project/light/2/get");
    mqttClient.subscribe("homeautomation/project/light/3/get");
    mqttClient.subscribe("homeautomation/project/light/4/get");
    mqttClient.subscribe("homeautomation/project/fan/1/get");
    mqttClient.subscribe("homeautomation/project/fan/2/get");
    mqttClient.subscribe("homeautomation/project/energy/get");
    
    // ... rest of your setup code ...
}
```

---

## OPTIONAL: Add Heartbeat (Recommended)

```cpp
// At the top with other variables
unsigned long lastHeartbeat = 0;
const long heartbeatInterval = 20000; // Send heartbeat every 20 seconds

// In your loop() function:
void loop() {
    // ... your existing code ...
    
    // Heartbeat mechanism
    unsigned long currentMillis = millis();
    if (currentMillis - lastHeartbeat >= heartbeatInterval) {
        lastHeartbeat = currentMillis;
        mqttClient.publish("homeautomation/project/heartbeat", "alive", false);
        Serial.println("Heartbeat sent");
    }
    
    // ... rest of your existing code ...
}
```

---

## Testing

1. **Upload the updated code** to your ESP
2. **Open the Android app** and log in
3. **Click "Refresh" button** → App will request status from ESP
4. **Check ESP status** → Should show "ESP Online" (green) immediately if ESP responds
5. **Leave app idle for 30+ seconds** → ESP should stay "Online" with heartbeat

---

## Technical Details

### Status Request Topics (Required)
- `homeautomation/project/light/{1-4}/get` - Request light status
- `homeautomation/project/fan/{1-2}/get` - Request fan status  
- `homeautomation/project/energy/get` - Request energy data

### Heartbeat Topic (Optional)
- `homeautomation/project/heartbeat` - Periodic alive message
- Payload: `"alive"`
- Retained: `false`
- Interval: 20 seconds

---

## Why This Works

**Status Request Handler**:
- App actively asks "Are you there?" when refresh is clicked
- ESP responds with current device states
- Response confirms ESP is online and working

**Heartbeat**:
- ESP proactively announces "I'm alive" every 20 seconds
- App knows ESP is online even when idle
- Prevents false "Offline" status
