# 🔧 Circuit Diagram & Wiring Guide

## ⚠️ Safety Warning
**WORKING WITH AC VOLTAGE IS DANGEROUS!**
- Always disconnect power before wiring
- Use proper insulation
- Follow electrical codes
- Consult a licensed electrician if unsure
- Never touch live circuits

---

## 📋 Component List

### Microcontrollers
- 1x ESP8266 NodeMCU (Main controller)
- 1x Arduino Nano (Sensor management via I2C)

### Sensors
- 1x DHT22 (Temperature & Humidity) - Connected to Arduino Nano
- 1x PZEM-004T v3.0 (Energy monitoring) - Connected to ESP8266
- 1x MQ-2 (Gas sensor) - Connected to ESP8266
- 1x PIR HC-SR501 (Motion sensor) - Connected to ESP8266

### Actuators
- 1x 8-Channel Relay Module (5V) - Connected to ESP8266
- 1x AC Dimmer Module (RobotDyn) - Connected to ESP8266
- 1x IR LED 940nm (Infrared transmitter) - Connected to ESP8266

### Power Supply
- 1x 5V 3A Power Supply (for ESP8266, relays, Arduino)
- 1x 3.3V regulator (if needed for sensors)

### Miscellaneous
- Jumper wires (M-M, M-F, F-F)
- Breadboard or PCB
- Resistors (220Ω for IR LED, 10kΩ pull-up if needed)
- Screw terminals
- Heat shrink tubing

---

## 🔌 ESP8266 NodeMCU Pin Configuration

### Digital Pins (GPIO)
| GPIO | NodeMCU Pin | Connected To | Purpose |
|------|------------|--------------|---------|
| GPIO16 | D0 | Relay 1 | Light 1 Control |
| GPIO5 | D1 | Relay 2 | Light 2 Control |
| GPIO4 | D2 | Relay 3 | Light 3 Control |
| GPIO0 | D3 | Relay 4 | Light 4 Control |
| GPIO2 | D4 | Relay 5 | Fan 1 Control |
| GPIO14 | D5 | Relay 6 | Fan 2 Control |
| GPIO12 | D6 | Relay 7 | Fan 3 Control |
| GPIO13 | D7 | Relay 8 | Fan 4 Control |
| GPIO15 | D8 | AC Dimmer (PWM) | Fan Speed Control |
| GPIO3 | RX | PZEM-004T TX | Energy Monitor |
| GPIO1 | TX | PZEM-004T RX | Energy Monitor |

### I2C Communication
| Pin | Connected To | Purpose |
|-----|--------------|---------|
| D1 (GPIO5) | Arduino Nano A5 (SCL) | I2C Clock |
| D2 (GPIO4) | Arduino Nano A4 (SDA) | I2C Data |
| GND | Arduino Nano GND | Common Ground |

### Analog & Other Pins
| Pin | Connected To | Purpose |
|-----|--------------|---------|
| A0 | MQ-2 Analog Out | Gas Level Reading |
| D8 (GPIO15) | IR LED (via 220Ω) | Infrared Control |
| D10 | PIR Sensor Out | Motion Detection |

### Power Pins
| Pin | Connection |
|-----|------------|
| VIN | 5V Power Supply |
| GND | Common Ground |
| 3V3 | 3.3V Sensors (if needed) |

---

## 🔌 Arduino Nano Pin Configuration

### I2C Slave
| Pin | Connected To | Purpose |
|-----|--------------|---------|
| A5 (SCL) | ESP8266 D1 | I2C Clock |
| A4 (SDA) | ESP8266 D2 | I2C Data |
| GND | ESP8266 GND | Common Ground |

### DHT22 Sensor
| DHT22 Pin | Arduino Pin | Purpose |
|-----------|-------------|---------|
| VCC | 5V | Power |
| DATA | D2 | Data Signal |
| GND | GND | Ground |

**Note**: Add a 10kΩ pull-up resistor between DATA and VCC for DHT22

### Power
| Pin | Connection |
|-----|------------|
| VIN | 5V Power Supply |
| GND | Common Ground |

---

## 🔧 Detailed Wiring Instructions

### 1. ESP8266 to Relay Module

```
ESP8266          8-Channel Relay
----------------------------------------
D0 (GPIO16) ---> IN1 (Relay 1 - Light 1)
D1 (GPIO5)  ---> IN2 (Relay 2 - Light 2)
D2 (GPIO4)  ---> IN3 (Relay 3 - Light 3)
D3 (GPIO0)  ---> IN4 (Relay 4 - Light 4)
D4 (GPIO2)  ---> IN5 (Relay 5 - Fan 1)
D5 (GPIO14) ---> IN6 (Relay 6 - Fan 2)
D6 (GPIO12) ---> IN7 (Relay 7 - Fan 3)
D7 (GPIO13) ---> IN8 (Relay 8 - Fan 4)
GND ---------> GND
VIN (5V) ----> VCC
```

### 2. ESP8266 to AC Dimmer Module

```
ESP8266          AC Dimmer (RobotDyn)
----------------------------------------
D8 (GPIO15) ---> PWM Input
GND ---------> GND
VIN (5V) ----> VCC

AC Dimmer        Load (Fan)
----------------------------------------
AC IN -------> AC Mains (230V/110V)
AC OUT ------> Fan AC Connection
```

**WARNING**: AC dimmer handles HIGH VOLTAGE. Use proper insulation!

### 3. ESP8266 to PZEM-004T

```
ESP8266          PZEM-004T v3.0
----------------------------------------
TX (GPIO1)  ---> RX
RX (GPIO3)  ---> TX
GND ---------> GND
VIN (5V) ----> 5V
```

**Note**: PZEM measures current on AC line. Connect in series with load.

### 4. ESP8266 to MQ-2 Gas Sensor

```
ESP8266          MQ-2
----------------------------------------
A0 ---------    AO (Analog Out)
GND --------    GND
VIN (5V) ---    VCC
```

### 5. ESP8266 to PIR Motion Sensor

```
ESP8266          PIR HC-SR501
----------------------------------------
D10 --------    OUT
GND --------    GND
VIN (5V) ---    VCC
```

### 6. ESP8266 to IR LED

```
ESP8266          IR LED (940nm)
----------------------------------------
D8 (GPIO15) ---> [220Ω Resistor] ---> IR LED Anode (+)
GND ---------> IR LED Cathode (-)
```

### 7. ESP8266 to Arduino Nano (I2C)

```
ESP8266          Arduino Nano
----------------------------------------
D1 (GPIO5) ---> A5 (SCL)
D2 (GPIO4) ---> A4 (SDA)
GND ---------> GND
```

### 8. Arduino Nano to DHT22

```
Arduino Nano     DHT22
----------------------------------------
5V ----------    VCC
D2 ----------    DATA (with 10kΩ pull-up to VCC)
GND ---------    GND
```

---

## 🔋 Power Supply Configuration

### Option 1: Single 5V Supply
```
5V 3A PSU
    │
    ├──> ESP8266 VIN
    ├──> Arduino Nano VIN
    ├──> Relay Module VCC
    ├──> PZEM VCC
    ├──> MQ-2 VCC
    ├──> PIR VCC
    └──> AC Dimmer VCC
    
All GND pins connected together
```

### Option 2: Separate Supplies
```
5V 2A PSU #1 ---> ESP8266, Arduino Nano
5V 3A PSU #2 ---> Relay Module (isolated)

**Connect all GNDs together**
```

---

## 📐 Circuit Diagram (ASCII)

```
                    +5V Power Supply
                         │
         ┌───────────────┼───────────────┐
         │               │               │
    ┌────▼────┐    ┌────▼────┐    ┌────▼─────┐
    │ ESP8266 │    │ Arduino │    │  Relay   │
    │ NodeMCU │────│  Nano   │    │  Module  │
    │         │I2C │         │    │ 8-Channel│
    └────┬────┘    └────┬────┘    └────┬─────┘
         │              │              │
         │              │              │ AC Loads
    ┌────┴────┬────┬───▼───┬──────────┼──────┐
    │         │    │       │          │      │
┌───▼──┐  ┌──▼─┐ ┌▼──┐ ┌──▼───┐   ┌──▼─┐  Lights
│ PZEM │  │MQ-2│ │PIR│ │ DHT22│   │ AC │  Fans
│004T  │  │Gas │ │   │ │Temp/ │   │Dim │  Appliances
│Power │  │    │ │   │ │Humid │   │mer │
└──────┘  └────┘ └───┘ └──────┘   └────┘
```

---

## ✅ Testing Checklist

### Before Powering On
- [ ] All connections double-checked
- [ ] No short circuits
- [ ] Proper polarity on all components
- [ ] AC circuits properly insulated
- [ ] All grounds connected
- [ ] Power supply voltage verified (5V)

### After Powering On
- [ ] ESP8266 blue LED blinks (WiFi connecting)
- [ ] Arduino Nano power LED on
- [ ] Relay module LEDs respond to signals
- [ ] Sensors reading values
- [ ] MQTT connection established
- [ ] Web dashboard shows device online

### Safety Tests
- [ ] No overheating components
- [ ] No smoke or burning smell
- [ ] Proper grounding of AC circuits
- [ ] Insulation intact
- [ ] No exposed live wires

---

## 🛠️ Troubleshooting

### ESP8266 Not Powering On
- Check 5V power supply
- Verify USB cable (if using USB)
- Check for shorts on breadboard

### Relays Not Switching
- Verify GPIO pin numbers in code
- Check relay module power (5V)
- Test with LED instead of relay
- Check control signal polarity

### Sensors Not Reading
- Verify sensor power (5V or 3.3V)
- Check wiring connections
- Test sensor with simple sketch
- Verify I2C address (for Arduino Nano)

### AC Dimmer Not Working
- Verify zero-cross detection
- Check PWM signal from ESP8266
- Ensure proper AC connection
- Test with incandescent bulb first

---

## 📸 Photos & Diagrams

Place your circuit photos in `docs/screenshots/`:
- `circuit_overview.jpg` - Complete setup
- `esp8266_wiring.jpg` - ESP8266 connections
- `relay_module.jpg` - Relay wiring
- `power_supply.jpg` - Power distribution

---

## 🔗 Additional Resources

- [ESP8266 Pinout](https://randomnerdtutorials.com/esp8266-pinout-reference-gpios/)
- [Arduino Nano Pinout](https://www.arduino.cc/en/pmwiki.php?n=Main/Arduino_BoardNano)
- [Relay Module Guide](https://www.electronicshub.org/control-a-relay-using-arduino/)
- [AC Dimmer Safety](https://www.instructables.com/AC-Light-Dimmer-Arduino/)

---

<div align="center">

**⚠️ Always prioritize safety when working with electronics! ⚠️**

[⬆ Back to Documentation](./README.md)

</div>
