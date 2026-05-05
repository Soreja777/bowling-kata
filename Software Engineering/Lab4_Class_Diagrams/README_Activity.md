# Activity Diagram – Smart Home Security Alert

## What it shows
The step-by-step process of detecting and handling a security threat in the Smart Home System.

## Swimlanes
- **Security Camera** – detects and captures
- **Home Controller** – analyzes and sends alerts
- **User** – receives and responds

## Flow
```
[START]
Security Camera : Detect motion
Security Camera : Capture image
Home Controller : Analyze threat
    [Threat?]
    Yes → Home Controller : Send alert
          User : Receive notification
              [Respond?]
              Yes → User : Call police
    No  → Home Controller : Log event
[END]
```

## Key Concepts Used
- Swimlanes (responsibility of each actor)
- Decision nodes (diamond shape)
- Start node (filled circle)
- End node (circle with dot)
- Parallel and sequential actions
