# TechCorp Simulator

A turn-based business strategy game written in Java.

The player manages a technology company competing against an AI-controlled company.  
Both companies manage employees, projects, budgets, and random business events.

## Features
- Turn-based gameplay
- AI opponent company
- Employee hierarchy
- Multiple project management
- Random events
- Salary system
- Win and lose conditions
- Project lifecycle management
- Console UI
- Exception handling and validation
- External file saving

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
- AI actions,
- project progress,
- random events,
- game results,
- company budget updates.