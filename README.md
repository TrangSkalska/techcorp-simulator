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

## How the game works?

The player controls a technology company competing against an AI-controlled company.

Each turn, the player chooses one action from the menu:

1. Show company status – displays company budget, employees, and projects.
2. Start planned projects – starts projects that are currently planned.
3. Work on projects – employees work on active projects and increase progress.
4. Show active projects – displays projects currently in progress.
5. Put project on hold – pauses a selected project.
6. Resume project – resumes a paused project.
7. Cancel project – permanently cancels a selected project.
8. Save money this turn – reduces company expenses and increases budget.
9. Exit game – ends the game.

At the end of each turn:
- salaries are paid,
- random business events may occur,
- the AI company performs its own actions automatically.

The goal is to manage projects and budget better than the AI competitor.

The player wins if:
- the AI company runs out of budget, or
- the player performs better financially than the AI.

The player loses if:
- their company runs out of budget, or
- the AI company performs better.

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