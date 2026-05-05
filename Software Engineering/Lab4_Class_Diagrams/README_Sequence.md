# Sequence Diagram – Smart Home System

## What it shows
The interaction between objects over time when a user controls the thermostat remotely.

## Lifelines
- **User** – sends the request via mobile app
- **MobileApp** – handles user interface
- **HomeController** – processes the command
- **Thermostat** – executes the temperature change

## Flow
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

## Key Concepts Used
- Lifelines (vertical dashed lines)
- Activation boxes (when each object is active)
- Solid arrows = calls / requests
- Dashed arrows = return messages
