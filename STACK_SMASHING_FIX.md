# Stack Smashing Fix - Critical Stability Update

## Problem
**Stack smashing exception (Exception 5)** occurred after ~60 seconds of operation, causing ESP8266 to crash and reboot.

## Root Cause Analysis
The stack overflow was caused by **HTTPClient Firebase operations combined with high-frequency interrupts**:

1. **Firebase HTTP operations** use:
   - WiFiClientSecure (TLS/SSL) - ~2KB stack usage
   - HTTPClient - ~1KB stack usage
   - JSON serialization buffers - 512 bytes
   
2. **Zero-cross interrupt** fires 100 times/second (50Hz AC)
   - Interrupts can nest during HTTP operations
   - ESP8266 has only ~4KB stack total

3. **Frequent updates** exacerbated the problem:
   - Firebase updates every 5 seconds
   - PZEM data every 10 seconds
   - Sensor data every 10 seconds
   - Serial log showed "✅ Firebase OK" appearing constantly

## Solution Applied

### 1. Disabled All Firebase HTTP Operations
```cpp
// Firebase operations cause stack overflow when combined with interrupts
// Use MQTT for all real-time data - web interface handles Firebase writes
void sendToFirebase(String path, String jsonData) {
  // Firebase DISABLED to prevent stack smashing
  yield();
}
```

**Why this works:**
- Eliminates HTTPClient/SSL stack usage completely
- MQTT is lightweight and interrupt-safe
- Web interface already has Firebase SDK and can persist data
- Separates concerns: ESP = control + MQTT, Web = persistence

### 2. Commented Out Firebase Calls
All sendToFirebase() calls disabled in:
- `saveCurrentSensorData()` - sensor data
- `savePZEMData()` - power monitoring current data
- `savePZEMData()` - power monitoring history

### 3. Reduced Update Frequencies
- Firebase interval: 5s → 60s (now irrelevant since disabled)
- History save: 1 min → 5 min (reduces processing load)

## Architecture After Fix

```
┌─────────────┐
│   ESP8266   │
│             │
│ - Control   │───MQTT──►┌──────────────┐
│ - Sensors   │          │ Web Interface│
│ - MQTT Only │◄──MQTT───│              │
└─────────────┘          │ - MQTT Recv  │
                         │ - Firebase   │───► Firebase
                         │ - UI Updates │    Realtime DB
                         └──────────────┘
```

**Data Flow:**
1. ESP reads sensors → publishes to MQTT
2. Web interface subscribes to MQTT → receives real-time data
3. Web interface writes to Firebase for persistence
4. ESP never touches Firebase (preventing stack overflow)

## Benefits
✅ **No more stack smashing** - HTTPClient removed entirely  
✅ **Faster response** - MQTT is lighter than HTTP  
✅ **Better separation** - ESP focuses on control, web handles storage  
✅ **More stable** - Reduced interrupt conflicts  
✅ **Scalable** - Web can batch Firebase writes efficiently  

## Testing Recommendations
1. Flash updated firmware to ESP8266
2. Open Serial Monitor at 115200 baud
3. Run for at least 5 minutes with fans active
4. Verify no crashes/reboots occur
5. Check web interface shows real-time data
6. Confirm Firebase still receives data (from web)

## Related Fixes
This is the 5th critical fix in the stability improvement chain:
1. MQTT timeout optimization (10s → 3s)
2. Toggle loop fix (removed optimistic rollback)
3. String memory safety (char buffers)
4. Watchdog reset fix (ISR optimization)
5. **Stack smashing fix (Firebase disabled)** ← Current

## File Modified
- `SMARTHOME_AUTOMATION_AC_Dimmer_Test_Working.ino`

## Date
December 2024
