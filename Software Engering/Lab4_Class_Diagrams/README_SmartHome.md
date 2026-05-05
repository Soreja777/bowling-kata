# Smart Home System – UML Diagrams

**Course: Introduction to Software Engineering**
**Lab: 2, 3, 4**

---

## Overview

The Smart Home System allows users to remotely control and monitor home devices such as lights, thermostats, door locks, and security cameras through a mobile application. The system supports multiple user roles with different access levels.

---

## Lab 2 – Use Case Diagram

### Actors
- **Homeowner** – has full access to all system features
- **Guest** – limited access to lights and door lock only
- **Admin** – manages users and views energy reports

### Use Cases
| Use Case | Description |
|----------|-------------|
| Control Lights | Turn lights on/off and adjust brightness |
| Control Thermostat | Set target temperature for rooms |
| Lock / Unlock Door | Remotely lock or unlock the front door |
| Monitor Security | View live camera feed and alerts |
| View Energy Usage | See energy consumption statistics |
| Manage Users | Add or remove homeowners and guests |

### Relationships
- Homeowner → Control Lights, Control Thermostat, Lock/Unlock Door, Monitor Security, View Energy Usage
- Guest → Control Lights, Lock/Unlock Door
- Admin → Manage Users, View Energy Usage
- Lock/Unlock Door <<include>> Monitor Security

---

## Lab 3 – Class Diagram

### Classes

**User**
- Attributes: userId: String, name: String
- Methods: login(), logout(), controlDevice()

**Home**
- Attributes: address: String, rooms: List
- Methods: addRoom(), removeRoom(), getStatus()

**Room**
- Attributes: roomName: String, devices: List
- Methods: addDevice(), listDevices(), getStatus()

**Device** (abstract)
- Attributes: deviceId: String, isOn: boolean
- Methods: turnOn(), turnOff(), getStatus()

**Light** (extends Device)
- Attributes: brightness: int
- Methods: dim(), setBrightness()

**Thermostat** (extends Device)
- Attributes: targetTemp: double
- Methods: setTemp(), getTemp(), getMode()

**DoorLock** (extends Device)
- Methods: lock(), unlock()

### Relationships
| Relationship | Type | Multiplicity |
|-------------|------|-------------|
| User – Home | Association | 1 to 1..* |
| Home – Room | Composition | 1 to 1..* |
| Room – Device | Aggregation | 1 to 0..* |
| Device – Light | Inheritance | – |
| Device – Thermostat | Inheritance | – |
| Device – DoorLock | Inheritance | – |

---

## Lab 4a – Sequence Diagram: Control Thermostat

### Lifelines
- User, MobileApp, HomeController, Thermostat

### Flow
```
User           → MobileApp       : setTemperature(22C)
MobileApp      → HomeController  : sendCommand(22C)
HomeController → Thermostat      : setTarget(22C)
Thermostat     → HomeController  : OK: new temp set
HomeController → MobileApp       : success: true
MobileApp      → User            : showConfirmation()
HomeController → MobileApp       : pushNotification()
MobileApp      → User            : notify: temp changed
```

---

## Lab 4b – State Diagram: Thermostat

### States
| State | Description |
|-------|-------------|
| Idle | Monitoring temperature, no action |
| Heating | Burner ON – heating the room |
| Cooling | AC ON – cooling the room |
| Off | System powered off |

### Transitions
| From | To | Trigger |
|------|----|---------|
| Idle | Heating | currentTemp < target - 1 degree |
| Idle | Cooling | currentTemp > target + 1 degree |
| Heating | Idle | currentTemp >= target |
| Cooling | Idle | currentTemp <= target |
| Any | Off | powerOff() |
| Off | Idle | powerOn() |

---

## Lab 4c – Activity Diagram: Security Alert

### Swimlanes
- Security Camera
- Home Controller
- User

### Flow
```
[START]
Security Camera : Detect motion
Security Camera : Capture image
Home Controller : Analyze threat
    [Threat?]
    Yes → Send alert to user
          User: Receive notification
              [Respond?]
              Yes → Call police
    No  → Log event
[END]
```

---

## Technologies Used
- UML 2.0 notation
- Diagrams created using draw.io
- Hosted on GitHub: github.com/[username]/SE_Labs_Portfolio
