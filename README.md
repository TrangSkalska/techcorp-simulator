# TechCorp Simulator

A turn-based business decision game written in Java.

## Features
- Employee hierarchy
- Multiple project management
- Random events
- Salary system
- Win and lose conditions
- Project lifecycle management
- Console UI
- Exception handling and validation

## Run

```bash
mvn exec:java -Dexec.mainClass="com.example.techcorp.Main"
```

## External File Saving

The game saves gameplay results and events into an external text file:

```text
game-results.txt
```

The file stores:
- player actions,
- project progress,
- random events,
- game results,
- company budget updates.
