# 🎉 Smart Home Automation v3.0.0 - Production Ready

## 📱 Download APK
**[Download app-debug.apk](https://github.com/pikumandal2005/Home-automation/releases/download/v3.0.0/app-debug.apk)**

---

## 🌟 Release Highlights

This is the **Production Ready** version of the Smart Home Automation System featuring **99.5% performance improvement** over previous versions, professional voice control, and comprehensive IoT device management.

### ✨ Key Features

#### 🎤 Advanced Voice Control
- **Natural Language Processing** - Control devices by name or number
- **Multiple Command Variations** - "Turn on", "Switch on", "Enable", etc.
- **Sensor Queries** - Ask about temperature, humidity, power consumption
- **Google Text-to-Speech** - Voice feedback for all actions
- **Device Name Recognition** - Fuzzy matching for custom device names

#### 🏡 Device Control
- **8 Relay Channels** - Complete control of lights and appliances
- **AC Dimmer** - Variable speed control for fans (0-100%)
- **IR Remote Control** - Infrared device management
- **Instant Response** - < 500ms control latency
- **Optimistic UI Updates** - Immediate visual feedback

#### 📊 Real-Time Monitoring
- **DHT22 Sensor** - Temperature and humidity tracking
- **PZEM-004T v3.0** - Power, voltage, current, and energy monitoring
- **MQ-2 Gas Sensor** - Gas leak detection
- **PIR Motion Sensor** - Motion detection
- **Live Charts** - Historical data with multiple time ranges (24H, 7D, 30D, 1Y, ALL)

#### 🔥 Safety Features
- **Fire Alarm System** - Real-time fire detection with alerts
- **Gas Leak Alerts** - Automatic notifications
- **Motion Detection** - Security monitoring
- **Activity Logs** - Complete action history with PDF export
- **Device Health Monitoring** - Online/offline status

#### 🌐 Connectivity
- **Firebase Integration** - Cloud storage and authentication
- **MQTT Protocol** - Real-time bidirectional communication
- **Offline Support** - Local caching for faster response
- **WebView Integration** - Seamless web dashboard in app

---

## 🚀 Performance Metrics

| Metric | Before v3.0 | After v3.0 | Improvement |
|--------|-------------|------------|-------------|
| **Control Response** | 10-120s | **< 500ms** | **99.5% faster** ⚡ |
| **UI Feedback** | 10-120s | **< 100ms** | **99.9% faster** ⚡ |
| **MQTT Timeout** | 10s | 3s | **70% faster** |
| **ESP Heartbeat** | 20s | 10s | **50% faster** |
| **Offline Detection** | 35s | 15s | **57% faster** |
| **Reconnection** | 5s | 2s | **60% faster** |

---

## 📋 Version Information

- **Version Name:** 3.0.0
- **Version Code:** 3
- **Package Name:** com.example.smarthome
- **Target SDK:** Android 36 (Latest)
- **Minimum SDK:** Android 24 (Android 7.0 and above)
- **Build Type:** Debug
- **Release Date:** December 22, 2025

---

## 📥 Installation Instructions

### Prerequisites
- Android 7.0 (API 24) or higher
- ~25 MB free storage
- Internet connection for Firebase and MQTT

### Installation Steps

1. **Download APK**
   - Click the download link above
   - Or download from [Releases](https://github.com/pikumandal2005/Home-automation/releases)

2. **Enable Unknown Sources**
   - Go to: Settings → Security
   - Enable "Install from Unknown Sources" or "Install Unknown Apps"

3. **Install APK**
   - Open downloaded APK file
   - Tap "Install"
   - Wait for installation to complete

4. **Grant Permissions**
   - Allow required permissions:
     - Internet access (for MQTT/Firebase)
     - Microphone (for voice control)
     - Notifications (for alerts)
     - Storage (for logs and exports)

5. **Configure**
   - Launch app
   - Sign in with Firebase credentials
   - Configure MQTT broker settings if needed

---

## 🎤 Voice Commands Examples

### Device Control
```
✅ "Turn on all lights"
✅ "Turn off bedroom fan"
✅ "Set living room fan to 75 percent"
✅ "Turn on ceiling light"
✅ "Switch off table lamp"
✅ "Turn on light 1"
```

### Sensor Queries
```
✅ "What is the temperature?"
✅ "Check humidity"
✅ "Power consumption"
✅ "How much power?"
✅ "Is there any motion?"
✅ "Gas level"
✅ "What is the voltage?"
```

### System Control
```
✅ "Turn on all devices"
✅ "Turn off all devices"
✅ "Emergency off"
✅ "Device status"
```

---

## 🛠️ What's New in v3.0.0

### Major Updates
✅ **Voice Control Overhaul**
   - Device name recognition with fuzzy matching
   - Multiple command variations support
   - Enhanced Google TTS integration
   - Natural language processing

✅ **Performance Optimization**
   - 99.5% faster device control response
   - Optimistic UI updates for instant feedback
   - MQTT timeout reduced from 10s to 3s
   - Faster heartbeat and reconnection

✅ **Enhanced Sensor Monitoring**
   - Real-time charts with historical data
   - Multiple time range views
   - Improved data accuracy
   - DHT22 via Arduino Nano I2C

✅ **Improved Stability**
   - Fixed ESP8266 stack smashing issues
   - Resolved MQTT connection drops
   - Better error handling and logging
   - Crash-free operation

✅ **UI/UX Improvements**
   - Modern Material Design 3
   - Mobile-optimized touch targets (44px)
   - Responsive layouts
   - Better loading states

### Bug Fixes
- Fixed TTS initialization issues
- Resolved voice command recognition problems
- Fixed sensor data display bugs
- Corrected PZEM device name handling
- Fixed fan control crash issues

---

## 🔧 Technical Specifications

### Android App
- **Language:** Kotlin
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 36
- **Architecture:** WebView + Native Integration
- **Dependencies:** Firebase, WebView, Material Design

### IoT Hardware
- **Controller:** ESP8266 NodeMCU
- **Sensors:** DHT22 (via Arduino Nano I2C), PZEM-004T, MQ-2, PIR
- **Actuators:** 8-Channel Relay, AC Dimmer, IR LED
- **Communication:** WiFi, MQTT, I2C

### Cloud Services
- **Backend:** Firebase (Auth, Firestore, Realtime DB, Storage)
- **MQTT Broker:** HiveMQ Cloud
- **Hosting:** GitHub Pages

---

## 📖 Documentation

- [📋 Complete Documentation](https://github.com/pikumandal2005/Home-automation/tree/main/docs)
- [🔧 Build Instructions](https://github.com/pikumandal2005/Home-automation/blob/main/BUILD_INSTRUCTIONS.md)
- [🎤 Voice Commands Guide](https://github.com/pikumandal2005/Home-automation/blob/main/VOICE_COMMANDS_QUICK_REFERENCE.md)
- [⚡ Performance Details](https://github.com/pikumandal2005/Home-automation/blob/main/PERFORMANCE_OPTIMIZATIONS_DECEMBER_2025.md)
- [🐛 Troubleshooting](https://github.com/pikumandal2005/Home-automation/blob/main/DHT22_TROUBLESHOOTING.md)

---

## ⚠️ Important Notes

### Security
- **Firebase credentials** are embedded in the app
- Use your own Firebase project for production
- Keep MQTT credentials secure
- Use SSL for MQTT connections (Port 8883)

### Hardware Requirements
- ESP8266 NodeMCU with firmware uploaded
- Proper wiring according to circuit diagram
- Stable WiFi connection
- Power supply (5V/3A recommended)

### Known Limitations
- Voice control requires internet connection
- MQTT requires active broker connection
- Some features require Firebase setup
- Android 7.0+ required

---

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](https://github.com/pikumandal2005/Home-automation/blob/main/CONTRIBUTING.md).

---

## 📞 Support

- **Issues:** [Report Bug](https://github.com/pikumandal2005/Home-automation/issues)
- **Documentation:** [Full Docs](https://github.com/pikumandal2005/Home-automation/tree/main/docs)
- **Repository:** [GitHub](https://github.com/pikumandal2005/Home-automation)

---

## 📄 License

This project is licensed under the MIT License - see [LICENSE](https://github.com/pikumandal2005/Home-automation/blob/main/LICENSE) for details.

---

## 👨‍💻 Credits

**Developed by:** [Piku Mandal](https://github.com/pikumandal2005)

### Special Thanks
- Firebase - Backend infrastructure
- HiveMQ - MQTT broker service
- ESP8266 Community - Arduino libraries
- Chart.js - Data visualization
- Android Open Source Project

---

## 🌟 Support the Project

If you find this project helpful:
- ⭐ Star the repository
- 🐛 Report bugs
- 💡 Suggest features
- 🤝 Contribute code
- 📢 Share with others

---

## 📊 Project Stats

- **Lines of Code:** ~15,000+
- **Languages:** Kotlin, JavaScript, C/C++ (Arduino)
- **Platforms:** Android, ESP8266, Arduino Nano
- **Status:** ✅ Production Ready

---

## 🔗 Quick Links

- **Repository:** https://github.com/pikumandal2005/Home-automation
- **Web Dashboard:** https://pikumandal2005.github.io/Home-automation/
- **Releases:** https://github.com/pikumandal2005/Home-automation/releases
- **Issues:** https://github.com/pikumandal2005/Home-automation/issues

---

<div align="center">

### Made with ❤️ by [Piku Mandal](https://github.com/pikumandal2005)

**🎉 Thank you for using Smart Home Automation! 🎉**

</div>
