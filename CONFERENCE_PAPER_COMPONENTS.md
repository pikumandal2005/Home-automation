# IoT-Based Smart Home Automation System
## Hardware Components - Specifications and Working

*Conference Paper - Technical Documentation*

---

| **S.No** | **Component** | **Specification** | **Working/Function** |
|----------|---------------|-------------------|----------------------|
| **1** | ESP8266 NodeMCU | WiFi Module, 32-bit processor, 4MB Flash, 80MHz, 3.3V logic, 802.11 b/g/n | Main microcontroller; WiFi connectivity, MQTT communication, GPIO control, master device for all sensors and actuators |
| **2** | Arduino Nano | ATmega328P, 5V, 16MHz, 32KB Flash, 8 analog inputs | I2C slave controller for sensor management and I/O expansion; handles DHT22 and PZEM-004T data acquisition |
| **3** | DHT22 | Temperature: -40°C to 80°C (±0.5°C), Humidity: 0-100% RH (±2%), 3.3-5V, Digital output | Temperature and humidity monitoring; connected to Arduino Nano pin D12 via single wire digital protocol |
| **4** | PZEM-004T v3.0 | AC 80-260V, 0-100A, Power: 0-22kW, Frequency: 45-65Hz, Energy: 0-9999.99kWh, TTL Serial (9600 baud) | Real-time AC power monitoring; measures voltage, current, power, energy consumption, frequency, and power factor |
| **5** | MQ-2 Gas Sensor | Detects LPG, Propane, Methane, Smoke; 5V supply, Analog output 0-1024; Detection range: 300-10000ppm | Gas leak detection and fire safety; analog reading threshold set at 700 for alarm activation |
| **6** | PIR HC-SR501 | Detection range: 7m, Angle: 120°, 3.3-5V, Digital output, Delay time: 0.3-200s adjustable | Passive infrared motion detection for security monitoring; connected to ESP8266 D0 (GPIO16) |
| **7** | 8-Channel Relay Module | 5V DC coil, 10A 250VAC/30VDC contacts per relay, Optocoupler isolated, Active LOW trigger | Switching control for lights and appliances; provides electrical isolation between control circuit and AC loads |
| **8** | AC Dimmer Module (RobotDyn) | AC 220-240V, Max 400W resistive load, TRIAC-based, PWM control input, Zero-cross detection circuit | Variable speed control for AC fans (0-100%); uses phase-angle control with zero-cross detection for smooth dimming |
| **9** | IR Receiver VS1838B | 940nm wavelength, 38KHz carrier frequency, 3.3-5V, Detection range: 18m | Infrared remote control reception; enables wireless device control via standard IR remote |
| **10** | IR LED Transmitter | 940nm wavelength, Forward voltage: 1.2-1.5V, Forward current: 100mA, with 220Ω current limiting resistor | Infrared signal transmission; controls IR-enabled appliances like TV, AC units |
| **11** | Flame Sensor | Detection wavelength: 760-1100nm, Detection angle: 60°, Detection range: 0.8m, Digital output, 3.3-5V | Fire detection sensor; detects infrared radiation from flames for fire alarm system |
| **12** | Power Supply | 5V DC, 3A output, Input: 100-240V AC | Provides regulated 5V power to ESP8266, Arduino Nano, relay module, and all sensors |
| **13** | Zero-Cross Detector | Integrated in AC dimmer module, detects AC waveform zero-crossing at 50Hz (100 events/sec) | Synchronizes TRIAC firing with AC waveform for smooth phase-angle dimming control; prevents EMI |
| **14** | RTC DS3231 (Optional) | I2C interface, ±2ppm accuracy, 3.3V, Battery backup, Temperature compensated | Real-time clock for timestamping and scheduled automation (optional component) |
| **15** | I2C Bus | Clock: SCL (ESP D1/GPIO5 to Arduino A5), Data: SDA (ESP D2/GPIO4 to Arduino A4), 100kHz standard mode | Inter-integrated circuit communication between ESP8266 and Arduino Nano for sensor data exchange |

---

## System Operation

### Working Principle

1. **User Input**: Commands received via voice control, Android app, or web dashboard
2. **Cloud Communication**: MQTT messages transmitted through HiveMQ broker over SSL/TLS
3. **ESP8266 Processing**: Main controller receives commands via WiFi and processes them
4. **Device Control**: 
   - Direct GPIO control for fans (TRIAC-based AC dimming with zero-cross detection)
   - Direct GPIO control for relay-connected lights and appliances
   - I2C commands sent to Arduino Nano for sensor data acquisition
5. **Sensor Monitoring**: 
   - Arduino Nano reads DHT22 (temperature/humidity) and PZEM-004T (power parameters)
   - ESP8266 directly monitors MQ-2 (gas), PIR (motion), and flame sensors
6. **Data Feedback**: Sensor data and device status published back to MQTT and stored in Firebase
7. **Real-time Updates**: Web dashboard and Android app display live status and historical data

### Key Operations

- **AC Dimming**: Zero-cross detection synchronizes TRIAC firing for smooth 0-100% fan speed control
- **Relay Switching**: Optocoupler-isolated relays provide safe AC load switching for lights
- **Sensor Acquisition**: I2C protocol enables ESP8266 to offload sensor reading to Arduino Nano
- **Safety Monitoring**: Continuous monitoring of gas levels and flame detection with instant alerts
- **Power Monitoring**: Real-time AC voltage, current, power, energy, and power factor measurement
- **IR Control**: Infrared transmitter/receiver for controlling legacy IR-based appliances

---

## IV. SYSTEM ALGORITHMS

### A. Overall System Working Algorithm

**Algorithm 1** Complete Smart Home System Operation

**Input:** User commands (Voice/App/Web), Sensor readings  
**Output:** Device control, Real-time monitoring, Safety alerts

```
1:  SMART HOME SYSTEM - COMPLETE WORKFLOW
2:  
3:  // PHASE 1: System Initialization
4:  Start System:
5:      Power on ESP8266 and Arduino Nano
6:      ESP8266 connects to home WiFi network
7:      ESP8266 connects to MQTT cloud broker (HiveMQ)
8:      ESP8266 connects to Firebase database
9:      Setup I2C communication between ESP8266 ↔ Arduino Nano
10:     Initialize all sensors (DHT22, PZEM, MQ-2, PIR, Flame)
11:     Initialize all actuators (8 Relays, 2 TRIACs, IR transmitter)
12:     System ready - Publish "ONLINE" status to cloud
13: 
14: // PHASE 2: User Control Flow
15: When User Gives Command:
16:     // Step 1: User interacts with system
17:     User speaks to phone OR taps button on app OR clicks on web
18:     
19:     // Step 2: Command reaches cloud
20:     Voice/App/Web converts command to MQTT message
21:     Message published to HiveMQ broker (e.g., "fan/1/set" = "100")
22:     
23:     // Step 3: ESP8266 receives command
24:     ESP8266 subscribes to all control topics
25:     ESP8266 receives the MQTT message
26:     ESP8266 processes the command
27:     
28:     // Step 4: Device control execution
29:     If command is for Fan Then
30:         Calculate TRIAC delay based on speed (0-100%)
31:         Wait for next AC zero-cross event
32:         Trigger TRIAC at calculated delay
33:         Fan speed changes smoothly
34:     Else If command is for Light Then
35:         Send I2C command to Arduino Nano
36:         Arduino Nano toggles relay ON/OFF
37:         Light turns ON/OFF instantly
38:     End If
39:     
40:     // Step 5: Feedback to user
41:     ESP8266 publishes device status back to MQTT
42:     Cloud updates Firebase database
43:     App/Web updates UI immediately (< 100ms)
44:     Voice assistant speaks confirmation
45: 
46: // PHASE 3: Continuous Monitoring Loop
47: Every 2-3 Seconds:
48:     // Sensor data collection
49:     Arduino Nano reads DHT22 → Temperature & Humidity
50:     Arduino Nano reads PZEM-004T → Voltage, Current, Power, Energy
51:     ESP8266 reads MQ-2 → Gas level (0-1024)
52:     ESP8266 reads PIR → Motion detection (Yes/No)
53:     ESP8266 reads Flame sensor → Fire detection (Yes/No)
54:     
55:     // Data transmission
56:     ESP8266 requests sensor data from Arduino via I2C (20 bytes)
57:     ESP8266 publishes all sensor data to MQTT topics
58:     Firebase stores data for historical graphs
59:     App/Web displays real-time sensor values
60:     Charts update automatically
61: 
62: // PHASE 4: Safety Monitoring (Every 500ms)
63: Continuous Safety Check:
64:     Read gas level from MQ-2 sensor
65:     If gas level > 700 Then
66:         Trigger gas leak alarm
67:         Send push notification to phone
68:         Voice alert: "Warning! Gas leak detected!"
69:         Log event with timestamp
70:     End If
71:     
72:     Read flame sensor status
73:     If flame detected Then
74:         Trigger fire alarm
75:         Send push notification to phone
76:         Voice alert: "Emergency! Fire detected!"
77:         Turn OFF all electrical devices (safety)
78:         Log event with timestamp
79:     End If
80: 
81: // PHASE 5: System Health Monitoring
82: Every 10 Seconds:
83:     ESP8266 sends heartbeat to MQTT
84:     If heartbeat stops Then
85:         App/Web shows "Device Offline" (15 seconds timeout)
86:         System attempts auto-reconnect
87:     End If
88: 
89: Every 60 Seconds:
90:     Save sensor history to Firebase
91:     Cleanup old data (keep last 24 hours for charts)
92:     Update energy consumption statistics
93: 
94: // PHASE 6: Data Flow Summary
95: Complete Data Journey:
96:     User → Voice/App/Web → MQTT Broker → ESP8266 → Device
97:     Device → ESP8266 → MQTT Broker → Firebase → App/Web → User
98:     
99:     Sensors → Arduino Nano → I2C → ESP8266 → MQTT → Firebase → Charts
100:    
101:    Safety Alert → ESP8266 → MQTT → Push Notification → User Phone
102:
103: // System runs continuously 24/7 with auto-recovery
104: End Algorithm
```

**Key Performance Metrics:**
- Command Response Time: < 500ms (99.5% faster than v1.0)
- UI Update Time: < 100ms (Optimistic updates)
- Sensor Update Rate: Every 3 seconds
- Safety Check Rate: Every 500ms
- Heartbeat Interval: Every 10 seconds
- System Uptime: 99.5% with auto-reconnect

---

### B. ESP8266 Main Control Loop Algorithm

**Algorithm 2** ESP8266 Main Controller

**Input:** WiFi credentials, MQTT broker details, sensor configurations  
**Output:** Device control signals, sensor data to cloud

```
1:  Start System
2:      Connect to WiFi network
3:      Connect to MQTT broker using SSL encryption
4:      Subscribe to control topics (fan/1/set, fan/2/set, light/1/set, light/2/set)
5:      Initialize all GPIO pins (Relays, TRIACs, Sensors)
6:      Setup I2C communication as master (Address = 0x08)
7:      Setup zero-cross interrupt on GPIO13
8:      Connect to time server for timestamps
9:      
10:     Repeat Forever:
11:         // Check WiFi connection
12:         If WiFi disconnected Then
13:             Reconnect to WiFi
14:         End If
15:         
16:         // Check MQTT connection
17:         If MQTT disconnected Then
18:             Reconnect to MQTT broker
19:         End If
20:         
21:         // Process incoming commands
22:         Process any incoming MQTT messages
23:         
24:         // Read sensors every 3 seconds
25:         If 3 seconds passed since last sensor read Then
26:             Read sensor data from Arduino Nano via I2C
27:             Read local sensors (gas, flame, PIR)
28:             Publish all sensor data to MQTT
29:             Update last sensor read time
30:         End If
31:         
32:         // Check safety sensors every 500ms
33:         If 500ms passed since last safety check Then
34:             Read gas level from MQ-2 sensor
35:             If gas level > 700 Then
36:                 Trigger gas leak alarm
37:             End If
38:             If flame detected Then
39:                 Trigger fire alarm
40:             End If
41:             Update last safety check time
42:         End If
43:         
44:         // Send heartbeat every 10 seconds
45:         If 10 seconds passed since last heartbeat Then
46:             Publish heartbeat message to MQTT
47:             Update last heartbeat time
48:         End If
49:         
50:         // Save to Firebase every 60 seconds
51:         If 60 seconds passed since last Firebase update Then
52:             Save historical sensor data to Firebase
53:             Update last Firebase update time
54:         End If
55:         
56:         Keep system responsive (yield to watchdog)
57:     End Repeat
58: End Algorithm
```

### C. MQTT Message Processing Algorithm

**Algorithm 3** MQTT Callback Handler

**Input:** MQTT topic, message payload, message length  
**Output:** Updated device states

```
1:  When MQTT Message Arrives:
2:      Convert payload bytes to text message
3:      
4:      // Handle Fan 1 control
5:      If topic = "homeautomation/project/fan/1/set" Then
6:          Convert message to number (0-100)
7:          Set Fan 1 power to this value
8:          Calculate new dimming delays
9:          Publish Fan 1 status back to MQTT
10:     
11:     // Handle Fan 2 control
12:     Else If topic = "homeautomation/project/fan/2/set" Then
13:         Convert message to number (0-100)
14:         Set Fan 2 power to this value
15:         Calculate new dimming delays
16:         Publish Fan 2 status back to MQTT
17:     
18:     // Handle Light 1 control
19:     Else If topic = "homeautomation/project/light/1/set" Then
20:         If message = "ON" Then
21:             Send I2C command 0x01 to Arduino (Turn ON)
22:             Publish "ON" to light/1/status
23:         Else
24:             Send I2C command 0x02 to Arduino (Turn OFF)
25:             Publish "OFF" to light/1/status
26:         End If
27:     
28:     // Handle Light 2 control
29:     Else If topic = "homeautomation/project/light/2/set" Then
30:         If message = "ON" Then
31:             Send I2C command 0x03 to Arduino (Turn ON)
32:             Publish "ON" to light/2/status
33:         Else
34:             Send I2C command 0x04 to Arduino (Turn OFF)
35:             Publish "OFF" to light/2/status
36:         End If
37:     
38:     // Handle status requests
39:     Else If topic contains "/get" AND message = "status" Then
40:         Publish current state of all devices
41:     End If
### D. AC Dimming Algorithm (Fan Speed Control)

**Algorithm 4** Zero-Cross Detection and TRIAC Control

**Input:** Fan power percentage (0-100%), AC frequency (50Hz)  
**Output:** TRIAC trigger pulses for smooth fan speed control

```
1:  // This runs every time AC voltage crosses zero (100 times per second)
2:  When Zero-Cross Detected:
3:      Count zero-cross events
4:      
5:      // Control Fan 1
6:      If Fan 1 power > 5% AND Fan 1 delay calculated Then
7:          Schedule Fan 1 TRIAC trigger after calculated delay
8:      End If
9:      
10:     // Control Fan 2
11:     If Fan 2 power > 5% AND Fan 2 delay calculated Then
12:         Schedule Fan 2 TRIAC trigger after calculated delay
13:     End If
14: End Algorithm
15: 
16: 
17: // Calculate delay times for each fan based on power percentage
18: Calculate Dimming Delays:
19:     HALF_CYCLE = 10000 microseconds (for 50Hz AC)
20:     MIN_DELAY = 200 microseconds (safety minimum)
21:     
22:     For each fan (Fan 1 and Fan 2):
23:         If fan power > 5% Then
24:             // Higher power = shorter delay = more power to fan
25:             delay_percentage = (100 - fan_power) / 100
26:             fan_delay = (delay_percentage × (HALF_CYCLE - MIN_DELAY))
27:                         + MIN_DELAY
28:         Else
29:             fan_delay = 0  (Fan is OFF)
30:         End If
31:     End For
32: End Calculation
33: 
34: 
35: // Send trigger pulse to TRIAC
36: Trigger TRIAC:
37:     Set TRIAC pin HIGH (turn on)
38:     Wait 20 microseconds
39:     Set TRIAC pin LOW (turn off)
40:     // TRIAC stays on until next zero-cross
41: End Trigger
```
### E. Sensor Data Communication Algorithm (ESP8266 ↔ Arduino)

**Algorithm 5** I2C Data Exchange Between Controllers

**Input:** I2C slave address (0x08), data request  
**Output:** Temperature, Humidity, Voltage, Current, Power, Energy

```
1:  // Read all sensor data from Arduino Nano
2:  Read Sensor Data via I2C:
3:      Request 20 bytes from Arduino Nano (address 0x08)
4:      
5:      If 20 bytes received successfully Then
6:          Read all 20 bytes into buffer
7:          
8:          // Extract individual sensor values from buffer
9:          motion_detected = buffer[0]
10:         temperature = Combine buffer[1] and buffer[2]
11:         humidity = Combine buffer[3] and buffer[4]
12:         voltage = Convert buffer[5-8] to decimal number
13:         current = Convert buffer[9-12] to decimal number
14:         power = Convert buffer[13-16] to decimal number
15:         energy = Convert buffer[17-20] to decimal number
16:         
17:         // Publish all data to MQTT
18:         Publish temperature to "sensor/temperature"
19:         Publish humidity to "sensor/humidity"
20:         Publish voltage to "pzem/voltage"
21:         Publish current to "pzem/current"
22:         Publish power to "pzem/power"
23:         Publish energy to "pzem/energy"
24:         
25:         Return all sensor values
26:     Else
27:         Log error "I2C communication failed"
28:         Return nothing
29:     End If
30: End Algorithm
31: 
32: 
33: // Send control commands to Arduino Nano
34: Send Command via I2C:
35:     Start I2C transmission to Arduino (address 0x08)
36:     Send command byte (what to do)
37:     Send data byte (additional information)
38:     End I2C transmission
39:     Keep system responsive
40: End Algorithm
30: 
31: procedure SEArduino Nano - Read Sensors and Send Data

**Input:** Sensor connections (DHT22, PZEM-004T), Commands from ESP8266  
**Output:** Sensor data packets via I2C

```
1:  Start Arduino Nano:
2:      Setup I2C as slave device (address = 0x08)
3:      Setup I2C receive handler (for commands from ESP8266)
4:      Setup I2C request handler (when ESP8266 asks for data)
5:      Initialize DHT22 sensor on pin 12
6:      Initialize PZEM sensor on pins 10 and 11 (9600 baud)
7:      
8:      Repeat Forever:
9:          // Read temperature and humidity every 2 seconds
10:         If 2 seconds passed since last DHT read Then
11:             Read temperature from DHT22
12:             Read humidity from DHT22
13:             If readings are valid Then
14:                 Store temperature and humidity values
15:             End If
16:             Update last DHT read time
17:         End If
18:         
19:         // Read power parameters every 3 seconds
20:         If 3 seconds passed since last PZEM read Then
21:             Read voltage from PZEM
22:             Read current from PZEM
23:             Read power from PZEM
24:             Read energy from PZEM
25:             Read frequency from PZEM
26:             Read power factor from PZEM
27:             Store all PZEM values
28:             Update last PZEM read time
29:         End If
30:         
31:         Process any I2C events (commands or data requests)
32:     End Repeat
33: End Algorithm
34: 
35: 
36: // When ESP8266 requests sensor data
37: When Data Requested:
38:     Create 20-byte data packet:
39:     Byte 0 = motion detected (yes/no)
40:     Bytes 1-2 = temperature value
41:     Bytes 3-4 = humidity value
42:     Bytes 5-8 = voltage value
43:     Bytes 9-12 = current value
44:     Bytes 13-16 = power value
45:     Bytes 17-20 = energy value
46:     Send all 20 bytes to ESP8266
47: End Handler
48: 
49: 
50: // When ESP8266 sends a command
51: When Command Received:
52:     Read command byte
53:     Read data byte
54:     
55:     If command = 0x01 Then Turn Light 1 ON
56:     Else If command = 0x02 Then Turn Light 1 OFF
57:     Else If command = 0x03 Then Turn Light 2 ON
58:     Else If command = 0x04 Then Turn Light 2 OFF
59:     Else If command = 0x05 Then Set fan speed to data value
### F. Voice Command Processing Algorithm

**Algorithm 6** Voice Control - Understanding and Executing Commands

**Input:** Voice text from speech recognition  
**Output:** MQTT control messages, Voice feedback

```
1:  Process Voice Command:
2:      Convert voice text to lowercase
3:      Remove special characters and extra spaces
4:      Break text into words
5:      
6:      Identify what user wants to do (turn on, turn off, or ask question)
7:      Identify which device (fan 1, fan 2, light 1, light 2)
8:      
9:      // Turn devices ON
10:     If command is "turn on" or "switch on" Then
11:         If device is Fan 1 or Fan 2 Then
12:             Create topic = "homeautomation/project/fan/[number]/set"
13:             Publish "100" to MQTT topic (100% power)
14:             Speak "Turning on [device name]"
15:         Else If device is Light 1 or Light 2 Then
16:             Create topic = "homeautomation/project/light/[number]/set"
17:             Publish "ON" to MQTT topic
18:             Speak "Turning on [device name]"
19:         End If
20:     
21:     // Turn devices OFF
22:     Else If command is "turn off" or "switch off" Then
23:         If device is Fan 1 or Fan 2 Then
24:             Create topic = "homeautomation/project/fan/[number]/set"
25:             Publish "0" to MQTT topic (0% power)
26:             Speak "Turning off [device name]"
27:         Else If device is Light 1 or Light 2 Then
28:             Create topic = "homeautomation/project/light/[number]/set"
29:             Publish "OFF" to MQTT topic
30:             Speak "Turning off [device name]"
31:         End If
32:     
33:     // Answer questions about sensors
34:     Else If user asks about temperature Then
35:         Get current temperature from Firebase
36:         Speak "Temperature is [value] degrees Celsius"
37:     Else If user asks about humidity Then
38:         Get current humidity from Firebase
39:         Speak "Humidity is [value] percent"
40:     Else If user asks about power consumption Then
41:         Get current power from Firebase
42:         Speak "Power consumption is [value] watts"
43:     
44:     // Command not understood
45:     Else
46:         Speak "Sorry, I didn't understand that command"
47:     End If
48: End Algorithm
30:     else if action = QUERY then
31:         if contains(tokens, {"temperature", "temp"}) then
32:             data ← Firebase.get("sensors/temperature")
33:             TTS.speak("Temperature is " + data + " degrees Celsius")
34:         else if contaAlgorithm

**Algorithm 7** Gas Leak and Fire Detection System

**Input:** Gas sensor readings, Flame sensor readings  
**Output:** Safety alerts, Emergency notifications

```
1:  Safety Monitoring System:
2:      Set gas danger level = 700 (analog value from MQ-2)
3:      Set gas safe level = 500
4:      Check sensors every 500 milliseconds
5:      
6:      gas_alarm_active = No
7:      fire_alarm_active = No
8:      
9:      Repeat Forever:
10:         If 500ms passed since last check Then
11:             
12:             // Check for gas leaks
13:             Read gas level from MQ-2 sensor (pin A0)
14:             
15:             If gas level > 700 (Dangerous level) Then
16:                 If gas alarm not already active Then
17:                     Set gas alarm to active
18:                     Publish "GAS LEAK DETECTED" to MQTT
19:                     Send push notification "⚠ Gas Leak Detected!"
20:                     Save event to log with timestamp
21:                     Speak "Warning! Gas leak detected!"
22:                 End If
23:             
24:             Else If gas level < 500 (Safe level) Then
25:                 If gas alarm is active Then
26:                     Set gas alarm to inactive
27:                     Publish "GAS SAFE" to MQTT
28:                     Save "Gas alarm cleared" to log
29:                 End If
30:             End If
31:             
32:             // Check for fire
33:             Read flame sensor (pin D5)
34:             
35:             If flame detected Then
36:                 If fire alarm not already active Then
37:                     Set fire alarm to active
38:                     Publish "FIRE DETECTED" to MQTT
39:                     Send push notification "🔥 Fire Detected!"
40:                     Save event to log with timestamp
41:                     Speak "Emergency! Fire detected!"
42:                     
43:                     // Safety action: Turn off all electrical devices
44:                     Turn off all fans
45:                     Turn off all lights
46:                 End If
47:             
48:             Else (No flame detected)
49:                 If fire alarm is active Then
50:                     Set fire alarm to inactive
51:                     Publish "FIRE CLEARED" to MQTT
52:                     Save "Fire alarm cleared" to log
53:                 End If
54:             End If
55:             
56:             Update last check time
57:         End If
58:     End Repeat
59: End Algorithm
```
53:             last_check ← millis()
54:         end if
55:     end while
56: end procedure
```

**Complexity Analysis:**
- Algorithm 1: O(1) per iteration, runs continuously
- Algorithm 2: O(n) where n is message length
- Algorithm 3: O(1) interrupt latency < 50μs
- Algorithm 4: O(1) fixed 20-byte transfer
- Algorithm 5: O(1) sensor read operations
- Algorithm 6: O(m) where m is number of tokens
- Algorithm 7: O(k) where k is number of devices

---

*Document Version: 1.0 | Date: December 22, 2025*
