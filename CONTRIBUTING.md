# Contributing to Smart Home Automation System

First off, thank you for considering contributing to this project! 🎉

## 🤝 How Can I Contribute?

### Reporting Bugs 🐛

Before creating bug reports, please check the existing issues to avoid duplicates. When you create a bug report, include as many details as possible:

- **Use a clear and descriptive title**
- **Describe the exact steps to reproduce the problem**
- **Provide specific examples** (code snippets, screenshots, logs)
- **Describe the behavior you observed** and what you expected
- **Include details about your setup:**
  - ESP8266 board version
  - Arduino IDE version
  - Library versions
  - Operating system
  - Browser (for web dashboard issues)

### Suggesting Enhancements 💡

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, include:

- **Use a clear and descriptive title**
- **Provide a detailed description** of the suggested enhancement
- **Explain why this enhancement would be useful**
- **List any similar features** in other projects (if applicable)

### Pull Requests 🔀

1. **Fork the repository** and create your branch from `main`
   ```bash
   git checkout -b feature/amazing-feature
   ```

2. **Make your changes** following our coding standards

3. **Test your changes thoroughly:**
   - ESP8266: Upload firmware and test all functions
   - Android app: Build and test on real device
   - Web dashboard: Test in multiple browsers

4. **Commit your changes** with clear commit messages:
   ```bash
   git commit -m "Add: New feature description"
   git commit -m "Fix: Bug description"
   git commit -m "Update: Improvement description"
   ```

5. **Push to your fork:**
   ```bash
   git push origin feature/amazing-feature
   ```

6. **Open a Pull Request** with a clear title and description

## 📝 Coding Standards

### ESP8266/Arduino Code
- Use **meaningful variable names** (camelCase)
- **Comment complex logic** and hardware-specific code
- Follow **Arduino style guide** conventions
- Keep functions **focused and small** (< 50 lines ideally)
- Add **header comments** to explain file purpose

```cpp
// Good example
const int LED_PIN = 2;
bool isDeviceOnline = false;

void connectToWiFi() {
    // Connection logic here
}

// Bad example
int p = 2;
bool x = false;
```

### JavaScript (Web Dashboard)
- Use **ES6+ features** (const, let, arrow functions)
- Follow **consistent indentation** (4 spaces)
- Use **meaningful function names**
- Add **JSDoc comments** for complex functions
- Keep functions **modular and reusable**

```javascript
// Good example
const MAX_RETRY_ATTEMPTS = 3;

/**
 * Connects to MQTT broker with retry logic
 * @param {string} broker - MQTT broker URL
 * @returns {Promise<boolean>} Connection status
 */
async function connectToMQTT(broker) {
    // Implementation
}

// Bad example
var x = 3;
function f1(b) {
    // unclear purpose
}
```

### Kotlin (Android App)
- Follow **Kotlin coding conventions**
- Use **data classes** for models
- Implement **proper error handling**
- Add **KDoc comments** for public functions
- Use **coroutines** for async operations

```kotlin
// Good example
data class Device(
    val id: String,
    val name: String,
    val isOnline: Boolean
)

/**
 * Fetches device status from server
 * @return List of devices or empty list on error
 */
suspend fun fetchDeviceStatus(): List<Device> {
    // Implementation
}
```

## 🔧 Development Setup

### Prerequisites
- **Arduino IDE** (1.8.19 or later)
- **Android Studio** (Latest stable version)
- **Git** for version control
- **VS Code** (recommended for web development)

### Local Setup

1. **Clone your fork:**
   ```bash
   git clone https://github.com/YOUR_USERNAME/Home-automation.git
   cd Home-automation
   ```

2. **Create a branch:**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Install ESP8266 libraries:**
   - ESP8266WiFi
   - PubSubClient
   - ArduinoJson
   - DHT sensor library
   - IRremoteESP8266

4. **Configure Firebase:**
   - Create your own Firebase project for testing
   - Update credentials in code

5. **Test your changes:**
   - Hardware: Upload and test on ESP8266
   - Android: Build and test APK
   - Web: Open index.html in browser

## 🧪 Testing Guidelines

### Hardware Testing
- [ ] Test all relay operations
- [ ] Verify AC dimmer functionality (0-100%)
- [ ] Check sensor readings accuracy
- [ ] Test MQTT connection stability
- [ ] Verify offline/online detection
- [ ] Test power loss recovery

### Web Dashboard Testing
- [ ] Test in Chrome, Firefox, Safari
- [ ] Verify mobile responsiveness
- [ ] Check voice commands
- [ ] Test real-time updates
- [ ] Verify chart functionality
- [ ] Test offline mode

### Android App Testing
- [ ] Test on multiple Android versions (API 24+)
- [ ] Verify permissions handling
- [ ] Test WebView integration
- [ ] Check notifications
- [ ] Test offline functionality

## 📚 Documentation

When adding new features:
- Update relevant `.md` files in docs folder
- Add inline code comments
- Update README.md if needed
- Include usage examples
- Document any new dependencies

## 🎨 Commit Message Guidelines

Use clear and descriptive commit messages:

- `Add: New feature or file`
- `Fix: Bug fix`
- `Update: Improvement to existing feature`
- `Remove: Deletion of code or file`
- `Refactor: Code restructuring`
- `Docs: Documentation changes`
- `Style: Formatting, missing semicolons, etc`
- `Test: Adding or updating tests`

Examples:
```bash
git commit -m "Add: Voice command for device status"
git commit -m "Fix: MQTT reconnection issue on ESP8266"
git commit -m "Update: Improve chart performance"
git commit -m "Docs: Add circuit diagram to README"
```

## 🚀 Feature Branches

Branch naming convention:
- `feature/feature-name` - New features
- `fix/bug-description` - Bug fixes
- `docs/description` - Documentation updates
- `refactor/description` - Code refactoring

## ⚠️ Important Notes

### Security
- **Never commit sensitive data:**
  - WiFi passwords
  - Firebase API keys
  - MQTT credentials
  - Personal information
- Use environment variables or config files (gitignored)
- Review your changes before committing

### Hardware Safety
- **Always test on a breadboard first**
- **Double-check wiring** before powering on
- **Use appropriate voltage levels** (3.3V for ESP8266)
- **Include current limiting resistors** where needed
- **Never connect AC directly** without proper isolation

### Code Review
- Be respectful and constructive
- Focus on the code, not the person
- Explain your reasoning
- Accept feedback gracefully

## 📞 Getting Help

- **Questions?** Open a GitHub Discussion
- **Bug?** Create an Issue
- **Feature request?** Create an Issue with `enhancement` label

## 🏆 Recognition

Contributors will be:
- Listed in the README.md
- Mentioned in release notes
- Part of a growing IoT community!

## 📄 License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

## 🎯 Priority Areas for Contribution

### High Priority
- [ ] HomeKit/Alexa/Google Home integration
- [ ] Advanced automation rules engine
- [ ] Multi-language support
- [ ] Better error handling and logging

### Medium Priority
- [ ] Additional sensor support (BME280, BH1750, etc.)
- [ ] Machine learning for usage patterns
- [ ] Improved scheduling system
- [ ] Energy usage analytics

### Low Priority (Nice to Have)
- [ ] Dark/Light theme toggle
- [ ] More chart types
- [ ] Export data to CSV
- [ ] Custom device icons

---

<div align="center">

### Thank you for contributing! 🙏

**Made with ❤️ by the Smart Home Community**

[⬆ Back to Top](#contributing-to-smart-home-automation-system)

</div>
