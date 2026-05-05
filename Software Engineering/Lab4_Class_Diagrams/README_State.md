# State Diagram – Smart Home Thermostat

## What it shows
All possible states of the thermostat and how it transitions between them based on temperature readings.

## States
| State | Description |
|-------|-------------|
| Idle | System running, temperature is within range |
| Heating | Temperature too low – burner is ON |
| Cooling | Temperature too high – AC is ON |
| Off | System is powered off |

## Transitions
| From | To | Trigger |
|------|----|---------|
| Idle | Heating | currentTemp < target - 1° |
| Idle | Cooling | currentTemp > target + 1° |
| Heating | Idle | currentTemp >= target |
| Cooling | Idle | currentTemp <= target |
| Any | Off | powerOff() |
| Off | Idle | powerOn() |

## Key Concepts Used
- Initial state (filled circle)
- Final state (circle with dot)
- States (rounded rectangles)
- Transitions with triggers and guards
