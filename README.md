# 🏠 Smart Home Automation System

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Android-24%2B-green.svg)](https://developer.android.com)
[![ESP8266](https://img.shields.io/badge/ESP8266-IoT-blue.svg)](https://www.espressif.com/)
[![Firebase](https://img.shields.io/badge/Firebase-Integrated-orange.svg)](https://firebase.google.com/)
[![MQTT](https://img.shields.io/badge/MQTT-HiveMQ-red.svg)](https://www.hivemq.com/)

> **A professional IoT-based smart home automation system with voice control, real-time monitoring, and energy management**

[🌐 Live Demo](https://pikumandal2005.github.io/Home-automation/) | [📱 Download APK](https://github.com/pikumandal2005/Home-automation/releases) | [📖 Documentation](./docs/)

---

## 🌟 Features

### 🎤 Voice Control
- **Advanced voice recognition** with natural language processing
- **Device name recognition** - Control devices by custom names
- **Multiple command variations** - "Turn on", "Switch on", "Enable", etc.
- **Sensor queries** - Ask about temperature, humidity, power consumption
- **Google Text-to-Speech feedback** - Voice confirmation for all actions

### 🏡 Device Control
- **8 Relay Control** - Light, fan, and appliance management
- **AC Dimmer** - Variable speed control for fans (0-100%)
- **IR Remote Control** - Control infrared devices
- **Instant Response** - < 500ms control latency (99.5% faster than v1.0)
- **Optimistic UI Updates** - Instant visual feedback

### 📊 Real-Time Monitoring
- **DHT22 Sensor** - Temperature and humidity tracking
- **PZEM-004T v3.0** - Power, voltage, current, and energy monitoring
- **MQ-2 Gas Sensor** - Gas leak detection
- **PIR Motion Sensor** - Motion detection
- **Live Charts** - Time-series data visualization (24H, 7D, 30D, 1Y, ALL)

### 🔥 Safety Features
- **Fire Alarm System** - Real-time fire detection and alerts
- **Gas Leak Alerts** - Automatic notifications
- **Motion Detection** - Security monitoring
- **Activity Logs** - Complete action history with PDF export
- **Offline Detection** - Device health monitoring

### 🌐 Web Dashboard
- **Responsive Design** - Mobile-first, works on all devices
- **Real-time Updates** - MQTT-based instant synchronization
- **Firebase Integration** - Cloud storage and authentication
- **Dark Mode UI** - Modern, professional interface
- **PWA Support** - Install as standalone app

### 📱 Android App
- **Native Performance** - Built with Kotlin
- **Material Design 3** - Modern UI/UX
- **WebView Integration** - Seamless web interface
- **Push Notifications** - Real-time alerts
- **Offline Support** - Local caching

---

## 🎯 Performance

| Metric | Before | After | Improvement |
|--------|--------|-------|-------------|
| **Control Response** | 10-120s | **< 500ms** | **99.5% faster** ⚡ |
| **UI Feedback** | 10-120s | **< 100ms** | **99.9% faster** ⚡ |
| **MQTT Timeout** | 10s | 3s | **70% faster** |
| **ESP Heartbeat** | 20s | 10s | **50% faster** |
| **Offline Detection** | 35s | 15s | **57% faster** |

---

## 🛠️ Hardware Components

### Main Controller
- **ESP8266 NodeMCU** - WiFi-enabled microcontroller
- **Arduino Nano** - I2C slave for sensor management

### Sensors
- **DHT22** - Temperature & Humidity (via Arduino Nano I2C)
- **PZEM-004T v3.0** - Energy monitoring
- **MQ-2** - Gas sensor
- **PIR HC-SR501** - Motion sensor

### Actuators
- **8-Channel Relay Module** - Device control
- **AC Light Dimmer** - Variable fan speed control
- **IR LED Transmitter** - Infrared remote control

### Additional Components
- **RTC DS3231** - Real-time clock (optional)
- **LCD Display** - Local status display (optional)

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Cloud Services                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Firebase   │  │  HiveMQ MQTT │  │ GitHub Pages │      │
│  │  Firestore   │  │    Broker    │  │   Hosting    │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                            ▲
                            │ Internet
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                  User Interfaces                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ Web Dashboard│  │  Android App │  │ Voice Control│      │
│  │   (PWA)      │  │   (Kotlin)   │  │  (Built-in)  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                            ▲
                            │ WiFi/MQTT
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                  IoT Devices                                 │
│  ┌──────────────┐         ┌──────────────┐                  │
│  │  ESP8266     │─────────│ Arduino Nano │                  │
│  │  (Master)    │  I2C    │  (Sensors)   │                  │
│  │              │         │  DHT22       │                  │
│  │ • 8 Relays   │         │  + Future    │                  │
│  │ • AC Dimmer  │         │    Sensors   │                  │
│  │ • IR Control │         │              │                  │
│  │ • PZEM-004T  │         │              │                  │
│  │ • MQ-2       │         │              │                  │
│  │ • PIR        │         │              │                  │
│  └──────────────┘         └──────────────┘                  │
└─────────────────────────────────────────────────────────────┘
                            ▲
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│               Home Appliances                                │
│  Lights • Fans • AC • TV • Other Devices                    │
└─────────────────────────────────────────────────────────────┘
```

---

## 🚀 Quick Start

### Prerequisites
- ESP8266 NodeMCU with Arduino IDE installed
- Android Studio (for Android app)
- Firebase account
- HiveMQ Cloud account (free tier)
- Basic electronics knowledge

### 1️⃣ Hardware Setup
```bash
# Connect components according to circuit diagram
# See docs/CIRCUIT_DIAGRAM.md for detailed wiring
```

### 2️⃣ ESP8266 Firmware
```bash
# 1. Install required libraries in Arduino IDE:
#    - ESP8266WiFi, PubSubClient, ArduinoJson, DHT, IRremoteESP8266

# 2. Update credentials in the .ino file:
const char* SSID = "YOUR_WIFI_SSID";
const char* PASSWORD = "YOUR_WIFI_PASSWORD";
const char* FIREBASE_HOST = "YOUR_FIREBASE_HOST";
const char* FIREBASE_AUTH = "YOUR_FIREBASE_AUTH";

# 3. Upload to ESP8266
#    - Select Board: NodeMCU 1.0 (ESP-12E Module)
#    - Upload Speed: 115200
```

### 3️⃣ Arduino Nano Firmware
```bash
# Upload Arduino_Nano_I2C_Slave.ino to Arduino Nano
# Configure I2C address (default: 0x08)
```

### 4️⃣ Web Dashboard
```bash
# 1. Update Firebase config in index.html (lines 22-33)
# 2. Update MQTT credentials (lines 7600-7650)
# 3. Deploy to GitHub Pages or any web server
```

### 5️⃣ Android App
```bash
# See BUILD_INSTRUCTIONS.md for detailed build steps
./gradlew assembleDebug
# APK output: app/build/outputs/apk/debug/app-debug.apk
```

---

## 📖 Documentation

- [📋 Build Instructions](./BUILD_INSTRUCTIONS.md) - How to build the Android app
- [🔧 Deployment Guide](./DEPLOYMENT_CHECKLIST.md) - Complete deployment steps
- [🎤 Voice Commands](./VOICE_COMMANDS_QUICK_REFERENCE.md) - All supported voice commands
- [⚡ Performance Optimizations](./PERFORMANCE_OPTIMIZATIONS_DECEMBER_2025.md) - Technical details
- [🐛 Troubleshooting](./DHT22_TROUBLESHOOTING.md) - Common issues and fixes
- [📊 Project Summary](./PROJECT_SUCCESS_SUMMARY.md) - Complete transformation story

---

## 🎤 Voice Commands Examples

### Device Control
```
✅ "Turn on all lights"
✅ "Turn off bedroom fan"
✅ "Set living room fan to 75 percent"
✅ "Turn on ceiling light"
✅ "Switch off table lamp"
```

### Sensor Queries
```
✅ "What is the temperature?"
✅ "Check humidity"
✅ "Power consumption"
✅ "Is there any motion?"
✅ "Gas level"
```

### System Control
```
✅ "Turn on all devices"
✅ "Emergency off" / "Panic mode"
✅ "Device status"
```

---

## 🔐 Security & Privacy

- **Local Network Communication** - No data sent to third parties
- **Firebase Authentication** - Secure user management
- **MQTT over SSL** - Encrypted communication (Port 8883)
- **API Key Protection** - Environment-based configuration
- **User Permission Control** - Role-based access

---

## 🌐 Deployment

### GitHub Pages (Web Dashboard)
```bash
# 1. Push code to GitHub
git add .
git commit -m "Initial commit"
git push origin main

# 2. Enable GitHub Pages
# Settings → Pages → Source: main branch → Save

# 3. Access at: https://pikumandal2005.github.io/Home-automation/
```

### Android App Release
```bash
# Generate signed APK
./gradlew assembleRelease

# Create GitHub Release
# Upload APK from: app/build/outputs/apk/release/
```

---

## 🤝 Contributing

We welcome contributions! Please see [CONTRIBUTING.md](./CONTRIBUTING.md) for guidelines.

### Development Setup
```bash
# Clone repository
git clone https://github.com/pikumandal2005/Home-automation.git
cd Home-automation

# Android app setup
# Open in Android Studio and sync Gradle

# Web dashboard - just open index.html in browser
```

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.

---

## 👨‍💻 Credits

**Developed by:** [Piku Mandal](https://github.com/pikumandal2005)

### Special Thanks
- **Firebase** - Backend infrastructure
- **HiveMQ** - MQTT broker service
- **ESP8266 Community** - Arduino libraries
- **Chart.js** - Data visualization
- **Tailwind CSS** - UI framework

---

## 📧 Contact & Support

- **GitHub Issues:** [Report Bug / Request Feature](https://github.com/pikumandal2005/Home-automation/issues)
- **GitHub Profile:** [@pikumandal2005](https://github.com/pikumandal2005)
- **Project Website:** [Live Demo](https://pikumandal2005.github.io/Home-automation/)

---

## 📊 Project Stats

- **Version:** 7.0 (Production Ready)
- **Status:** ✅ Active Development
- **First Release:** 2025
- **Last Updated:** December 2025
- **Lines of Code:** ~15,000+
- **Languages:** JavaScript, Kotlin, C/C++ (Arduino)

---

## 🎯 Roadmap

### ✅ Completed
- [x] Voice control with natural language
- [x] Real-time sensor monitoring
- [x] Energy management system
- [x] Android app
- [x] Web dashboard
- [x] Performance optimization (99.5% improvement)
- [x] Fire alarm system

### 🔄 In Progress
- [ ] HomeKit/Alexa/Google Home integration
- [ ] Advanced automation rules
- [ ] Machine learning for usage patterns

### 📅 Future Plans
- [ ] Multi-user support
- [ ] Geofencing
- [ ] Schedule-based automation
- [ ] Energy usage predictions
- [ ] Mobile notifications improvements

---

## 🌟 Star History

If you found this project helpful, please give it a ⭐ on GitHub!

---

## 📸 Screenshots

### Web Dashboard
![Dashboard](./docs/screenshots/dashboard.png)

### Android App
![Android App](./docs/screenshots/android_app.png)

### Hardware Setup
![Hardware](./docs/screenshots/hardware_setup.png)

---

<div align="center">

### Made with ❤️ by [Piku Mandal](https://github.com/pikumandal2005)

**If this project helped you, consider giving it a ⭐!**

[⬆ Back to Top](#-smart-home-automation-system)

</div>
