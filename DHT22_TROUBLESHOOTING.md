# DHT22 Serial Monitor Troubleshooting Guide

## Problem: DHT22 data not showing on Serial Monitor

### Quick Fix Checklist

#### 1. ✅ Check Serial Monitor Settings
- **Baud Rate**: Must be **115200** (bottom-right corner of Serial Monitor)
- **Line Ending**: Set to "Both NL & CR" or "Newline"
- **Port**: Correct COM port selected (Tools > Port)

#### 2. ✅ Upload the Updated Code
```arduino
1. Open Arduino IDE
2. Open: Arduino_Nano_I2C_Slave.ino
3. Select: Tools > Board > Arduino Nano
4. Select: Tools > Processor > ATmega328P (Old Bootloader) [or ATmega328P]
5. Select: Tools > Port > COM# (your Arduino port)
6. Click: Upload button (right arrow)
7. Wait for "Done uploading" message
8. Open: Tools > Serial Monitor (Ctrl+Shift+M)
```

#### 3. ✅ Check Wiring
**DHT22 Connections:**
```
DHT22 Pin 1 (VCC)  -> Arduino 5V
DHT22 Pin 2 (DATA) -> Arduino D12 (with 10K resistor to 5V)
DHT22 Pin 4 (GND)  -> Arduino GND

CRITICAL: 10K ohm pullup resistor between DATA and VCC!
```

**DHT22 Pinout (facing the grille):**
```
 _________________
|  |  |  |  |     |
|  1  2  3  4     |  <- Pins
|_________________|

Pin 1: VCC (3-5V)
Pin 2: DATA
Pin 3: Not connected
Pin 4: GND
```

#### 4. ✅ Expected Serial Output

**On Arduino Startup:**
```
========================================
  Arduino Nano I2C Slave v2.1
  DHT22 + PZEM-004T Monitor
========================================

✓ I2C Slave: Address 0x08
Initializing PZEM-004T... ✓ OK: 230.5V
Initializing DHT22 on D12... ✓ OK: 25.2°C, 60.5%

========================================
  System Ready!
  Monitoring DHT22 every 2 seconds
  Serial Monitor: 115200 baud
========================================

[2s] Reading DHT22... ✓ DHT22 | Temp: 25.2°C | Humidity: 60.5% | Raw: T=252 H=605
⚡ PZEM | V:230.5V | A:1.20A | W:276W | Hz:50.0 | PF:0.99
[4s] Reading DHT22... ✓ DHT22 | Temp: 25.3°C | Humidity: 60.4% | Raw: T=253 H=604
[6s] Reading DHT22... ✓ DHT22 | Temp: 25.2°C | Humidity: 60.5% | Raw: T=252 H=605
```

**If DHT22 Fails:**
```
Initializing DHT22 on D12... ✗ FAIL!
  Wiring Check:
    DHT22 VCC   -> Arduino 5V
    DHT22 DATA  -> Arduino D12 (with 10K pullup to 5V)
    DHT22 GND   -> Arduino GND
  Note: DHT22 requires 10K pullup resistor!

[2s] Reading DHT22... ✗ FAILED | T=nan H=nan
   Check: DHT22 wiring, power, 10K pullup resistor on DATA
```

### Common Issues & Solutions

#### Issue 1: "✗ FAIL!" during initialization
**Cause**: DHT22 not connected or wiring incorrect  
**Fix**:
1. Check all 3 wires (VCC, DATA, GND)
2. Verify 10K pullup resistor on DATA pin
3. Try different DHT22 sensor (might be damaged)
4. Use shorter wires (<20cm recommended)

#### Issue 2: "✗ FAILED | T=nan H=nan" during reading
**Cause**: Missing pullup resistor or sensor timing issue  
**Fix**:
1. **Add 10K ohm resistor between DATA (D12) and 5V** - CRITICAL!
2. Power cycle the Arduino
3. Try with 4.7K resistor if 10K doesn't work
4. Ensure stable 5V power supply

#### Issue 3: Serial Monitor shows nothing
**Cause**: Wrong baud rate or COM port  
**Fix**:
1. Set baud rate to **115200**
2. Close and reopen Serial Monitor
3. Press Arduino RESET button
4. Check COM port in Device Manager (Windows)

#### Issue 4: Readings frozen or stuck
**Cause**: Sensor hung or timing issue  
**Fix**:
1. Disconnect DHT22 power for 5 seconds
2. Reconnect and upload code again
3. Add decoupling capacitor (100nF) near DHT22 VCC

#### Issue 5: Intermittent readings
**Cause**: Long wires or electrical noise  
**Fix**:
1. Use shorter wires (under 20cm)
2. Twist DATA and GND wires together
3. Add 100nF capacitor between VCC and GND at sensor
4. Move DHT22 away from motors/relays

### DHT22 Library Check

Ensure DHT library is installed:
```
Arduino IDE > Tools > Manage Libraries
Search: "DHT sensor library"
Install: "DHT sensor library by Adafruit"
Also install: "Adafruit Unified Sensor"
```

### Hardware Test Code (Simple)

If still having issues, try this minimal test:
```cpp
#include <DHT.h>
#define DHT_PIN 12
#define DHTTYPE DHT22

DHT dht(DHT_PIN, DHTTYPE);

void setup() {
  Serial.begin(115200);
  Serial.println("DHT22 Test");
  dht.begin();
  delay(2000);
}

void loop() {
  float temp = dht.readTemperature();
  float hum = dht.readHumidity();
  
  Serial.print("Temp: ");
  Serial.print(temp);
  Serial.print("°C | Humidity: ");
  Serial.print(hum);
  Serial.println("%");
  
  delay(2000);
}
```

### Voltage & Power Check

**DHT22 Power Requirements:**
- Voltage: 3.3V to 5V (use 5V for Arduino Nano)
- Current: 1-1.5mA (measuring) to 2.5mA max
- Power: 5mW typical

**Check:**
1. Measure voltage at DHT22 VCC pin: should be ~5V
2. If < 4.8V, use external 5V power supply
3. Ensure Arduino USB cable is good quality

### Final Checklist

✅ Baud rate = 115200  
✅ Correct COM port selected  
✅ Code uploaded successfully  
✅ DHT22 VCC connected to 5V  
✅ DHT22 DATA connected to D12  
✅ DHT22 GND connected to GND  
✅ **10K ohm pullup resistor installed** (DATA to 5V)  
✅ DHT library installed  
✅ Wires are short and secure  
✅ No loose connections  
✅ Press RESET button on Arduino  

### Still Not Working?

1. **Try a different DHT22 sensor** (might be defective)
2. **Try a different Arduino pin** (change D12 to D11 in code)
3. **Check with multimeter**: DATA pin should read ~5V when idle
4. **Look for bent/damaged pins** on DHT22
5. **Verify DHT22 vs DHT11**: DHT22 has metal grille, DHT11 is blue plastic

---

## Expected Behavior Summary

**What you SHOULD see:**
- Startup banner with version info
- "✓ OK" messages for DHT22 and PZEM
- DHT22 readings every 2 seconds with timestamp
- Temperature and humidity values updating
- Clear formatting with ✓ or ✗ symbols

**What indicates success:**
```
[2s] Reading DHT22... ✓ DHT22 | Temp: 25.2°C | Humidity: 60.5%
```

**The timestamp [2s], [4s], [6s] confirms the loop is running!**

---

**Last Updated**: December 19, 2025  
**Code Version**: Arduino_Nano_I2C_Slave.ino v2.1
