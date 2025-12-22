# 📖 Smart Home Automation - Documentation

Welcome to the complete documentation for the Smart Home Automation System.

## 📚 Documentation Index

### Getting Started
- [Quick Start Guide](#quick-start-guide)
- [Hardware Requirements](#hardware-requirements)
- [Software Requirements](#software-requirements)
- [Initial Setup](#initial-setup)

### Hardware Documentation
- [Circuit Diagram](./CIRCUIT_DIAGRAM.md)
- [Component Wiring](./WIRING_GUIDE.md)
- [Hardware Troubleshooting](../DHT22_TROUBLESHOOTING.md)

### Software Documentation
- [ESP8266 Firmware](./ESP8266_FIRMWARE.md)
- [Arduino Nano Setup](./ARDUINO_NANO_SETUP.md)
- [Web Dashboard Guide](./WEB_DASHBOARD.md)
- [Android App Guide](./ANDROID_APP.md)

### Features & Usage
- [Voice Commands](../VOICE_COMMANDS_QUICK_REFERENCE.md)
- [Device Control](./DEVICE_CONTROL.md)
- [Sensor Monitoring](./SENSOR_MONITORING.md)
- [Energy Management](./ENERGY_MANAGEMENT.md)

### Advanced Topics
- [MQTT Configuration](./MQTT_SETUP.md)
- [Firebase Integration](./FIREBASE_SETUP.md)
- [Performance Optimization](../PERFORMANCE_OPTIMIZATIONS_DECEMBER_2025.md)
- [Security Best Practices](./SECURITY.md)

### Deployment
- [Build Instructions](../BUILD_INSTRUCTIONS.md)
- [Deployment Checklist](../DEPLOYMENT_CHECKLIST.md)
- [GitHub Pages Setup](#github-pages-setup)

### Troubleshooting
- [Common Issues](./TROUBLESHOOTING.md)
- [DHT22 Sensor Issues](../DHT22_TROUBLESHOOTING.md)
- [ESP Crash Fixes](../ESP_CRASH_FIX_FAN_CONTROL.md)
- [Stack Smashing Fix](../STACK_SMASHING_FIX.md)

### Contributing
- [Contributing Guidelines](../CONTRIBUTING.md)
- [Code of Conduct](./CODE_OF_CONDUCT.md)
- [Development Setup](./DEVELOPMENT_SETUP.md)

---

## 🚀 Quick Start Guide

### 1. Hardware Setup
```
Required Components:
├── ESP8266 NodeMCU
├── Arduino Nano
├── 8-Channel Relay Module
├── AC Dimmer Module
├── DHT22 Sensor
├── PZEM-004T v3.0
├── MQ-2 Gas Sensor
├── PIR Motion Sensor
├── IR LED Transmitter
└── Power Supply (5V/3.3V)
```

### 2. Software Setup
```
Required Software:
├── Arduino IDE 1.8.19+
├── Android Studio (for app)
├── Firebase Account
├── HiveMQ Cloud Account
└── Git
```

### 3. Installation Steps
1. **Clone the repository**
   ```bash
   git clone https://github.com/pikumandal2005/Home-automation.git
   ```

2. **Configure ESP8266**
   - Update WiFi credentials
   - Set Firebase details
   - Configure MQTT settings

3. **Upload firmware**
   - ESP8266: Upload main .ino file
   - Arduino Nano: Upload I2C slave code

4. **Deploy web dashboard**
   - Update Firebase config
   - Deploy to GitHub Pages

5. **Build Android app**
   - Follow BUILD_INSTRUCTIONS.md
   - Install APK on device

---

## 🔌 Hardware Requirements

### Microcontrollers
| Component | Specification | Purpose |
|-----------|--------------|---------|
| ESP8266 NodeMCU | WiFi-enabled | Main controller |
| Arduino Nano | ATmega328P | Sensor management |

### Sensors
| Sensor | Model | Measurement |
|--------|-------|-------------|
| Temperature/Humidity | DHT22 | -40°C to 80°C, 0-100% RH |
| Energy Monitor | PZEM-004T v3.0 | Voltage, Current, Power, Energy |
| Gas Sensor | MQ-2 | Combustible gases |
| Motion Sensor | PIR HC-SR501 | Motion detection |

### Actuators
| Module | Specification | Control |
|--------|--------------|---------|
| Relay Module | 8-Channel 5V | Lights, fans, appliances |
| AC Dimmer | RobotDyn | Fan speed (0-100%) |
| IR Transmitter | 940nm LED | Infrared devices |

---

## 💻 Software Requirements

### Development Tools
- **Arduino IDE**: 1.8.19 or later
- **Android Studio**: Latest stable
- **VS Code**: Recommended for web development
- **Git**: Version control

### Libraries (ESP8266)
```cpp
- ESP8266WiFi
- PubSubClient (MQTT)
- ArduinoJson
- DHT sensor library
- IRremoteESP8266
- Wire (I2C)
```

### Cloud Services
- **Firebase**: Authentication, Firestore, Storage, Realtime DB
- **HiveMQ**: MQTT broker (free tier)
- **GitHub Pages**: Web hosting

---

## ⚙️ Initial Setup

### Step 1: Firebase Configuration
1. Create Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
2. Enable Authentication (Email/Password)
3. Create Firestore database
4. Enable Realtime Database
5. Copy configuration credentials

### Step 2: MQTT Broker Setup
1. Sign up at [HiveMQ Cloud](https://www.hivemq.com/mqtt-cloud-broker/)
2. Create cluster (free tier)
3. Create credentials
4. Note broker URL and port (8883 for SSL)

### Step 3: ESP8266 Configuration
```cpp
// Update in .ino file
const char* SSID = "YOUR_WIFI_SSID";
const char* PASSWORD = "YOUR_WIFI_PASSWORD";
const char* FIREBASE_HOST = "your-project.firebaseio.com";
const char* FIREBASE_AUTH = "your-database-secret";
const char* MQTT_BROKER = "your-broker.hivemq.cloud";
```

### Step 4: Web Dashboard Configuration
```javascript
// Update in index.html
const firebaseConfig = {
    apiKey: "YOUR_API_KEY",
    authDomain: "your-project.firebaseapp.com",
    databaseURL: "https://your-project.firebaseio.com",
    projectId: "your-project",
    storageBucket: "your-project.appspot.com",
    messagingSenderId: "YOUR_SENDER_ID",
    appId: "YOUR_APP_ID"
};
```

---

## 🌐 GitHub Pages Setup

### Enable GitHub Pages
1. Push code to GitHub repository
2. Go to Settings → Pages
3. Source: Select `main` branch
4. Save
5. Access at: `https://pikumandal2005.github.io/Home-automation/`

### Custom Domain (Optional)
1. Add CNAME file with your domain
2. Configure DNS settings
3. Enable HTTPS in GitHub Pages settings

---

## 📊 System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Cloud Services                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Firebase   │  │  HiveMQ MQTT │  │ GitHub Pages │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                            ▲
                            │ Internet
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                  User Interfaces                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ Web Dashboard│  │  Android App │  │ Voice Control│      │
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

## 🎯 Key Features

### ✅ Device Control
- 8 relay channels for on/off control
- AC dimmer for variable speed (0-100%)
- IR remote control for infrared devices
- Instant response (< 500ms)

### ✅ Sensor Monitoring
- Real-time temperature & humidity
- Power consumption tracking
- Gas leak detection
- Motion detection
- Historical data with charts

### ✅ Voice Control
- Natural language processing
- Device name recognition
- Multiple command variations
- Sensor queries
- Voice feedback

### ✅ Safety Features
- Fire alarm system
- Gas leak alerts
- Motion detection
- Activity logging
- Offline detection

---

## 📞 Support

- **GitHub Issues**: [Report Bug](https://github.com/pikumandal2005/Home-automation/issues)
- **Documentation**: [Full Docs](https://github.com/pikumandal2005/Home-automation/tree/main/docs)
- **Contributing**: [Guidelines](../CONTRIBUTING.md)

---

## 📄 License

This project is licensed under the MIT License - see [LICENSE](../LICENSE) for details.

---

<div align="center">

**Made with ❤️ by [Piku Mandal](https://github.com/pikumandal2005)**

[⬆ Back to Top](#-smart-home-automation---documentation)

</div>
