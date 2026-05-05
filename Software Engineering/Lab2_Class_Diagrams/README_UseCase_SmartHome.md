# Use Case Diagram – Smart Home System

## What it shows
The functional requirements of the Smart Home System and the interaction between users and the system.

## Actors
- **Homeowner** – has full access to all features
- **Guest** – limited access to lights and door only
- **Admin** – manages users and energy reports

## Use Cases
| Use Case | Description |
|----------|-------------|
| Control Lights | Turn lights on/off, adjust brightness |
| Control Thermostat | Set target room temperature |
| Lock / Unlock Door | Remotely secure the front door |
| Monitor Security | View camera feed and receive alerts |
| View Energy Usage | Check energy consumption statistics |
| Manage Users | Add or remove system users |

## Relationships
- Homeowner → Control Lights, Control Thermostat, Lock/Unlock Door, Monitor Security, View Energy Usage
- Guest → Control Lights, Lock/Unlock Door
- Admin → Manage Users, View Energy Usage
- Lock/Unlock Door <<include>> Monitor Security

## Key Concepts Used
- System boundary (rectangle)
- Actors (stick figures)
- Use cases (ellipses)
- Association lines
- <<include>> relationship
