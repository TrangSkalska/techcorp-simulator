package com.example.techcorp.engine;

import com.example.techcorp.Company;
import com.example.techcorp.GameLogger;
import com.example.techcorp.Project;
import com.example.techcorp.ProjectStatus;
import com.example.techcorp.events.EquipmentFailureEvent;
import com.example.techcorp.events.GameEvent;
import com.example.techcorp.events.InvestorEvent;
import com.example.techcorp.events.MarketSlowdownEvent;
import com.example.techcorp.events.ReputationBoostEvent;
import com.example.techcorp.ui.ConsoleUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {
    private Company company;
    private ConsoleUI ui;
    private boolean running;
    private int turn;
    private final int maxTurns = 10;

    private List<GameEvent> events = new ArrayList<>();
    private Random random = new Random();
    private GameLogger logger = new GameLogger("game-results.txt");

    public GameEngine(Company company, ConsoleUI ui) {
        if (company == null) {
            throw new NullPointerException("Company cannot be null.");
        }

        if (ui == null) {
            throw new NullPointerException("Console UI cannot be null.");
        }

        this.company = company;
        this.ui = ui;
        this.running = true;
        this.turn = 1;

        events.add(new MarketSlowdownEvent());
        events.add(new InvestorEvent());
        events.add(new ReputationBoostEvent());
        events.add(new EquipmentFailureEvent());

        logger.clear();
        logger.log("Game started.");
        logger.log("Company: " + company.getName());
        logger.log("Starting budget: " + company.getBudget());
    }

    public void start() {
        while (running) {
            ui.showTurnHeader(turn);
            ui.showCompanyStatus(company);
            ui.showMainMenu();

            int choice = ui.readMenuChoice();
            logger.log("Turn " + turn + ": player chose option " + choice);

            boolean actionConsumesTurn = handleChoice(choice);

            if (running && actionConsumesTurn) {
                endTurn();
            }
        }

        logger.log("Game ended.");
    }

    private boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                ui.showCompanyStatus(company);
                return false;

            case 2:
                return startPlannedProjects();

            case 3:
                return workOnProjects();

            case 4:
                ui.showUnfinishedProjects(company.getProjects());
                return false;

            case 5:
                return putProjectOnHold();

            case 6:
                return resumeProject();

            case 7:
                return cancelProject();

            case 8:
                running = false;
                ui.showMessage("Thanks for playing!");
                logger.log("Player exited the game.");
                return false;

            default:
                ui.showMessage("Invalid menu option.");
                logger.log("Invalid menu option selected.");
                return false;
        }
    }

    private boolean startPlannedProjects() {
        boolean startedAny = false;

        for (Project project : company.getProjects()) {
            if (project.getStatus() == ProjectStatus.PLANNED) {
                try {
                    project.start();
                    startedAny = true;
                    logger.log("Started project: " + project.getName());
                } catch (IllegalStateException e) {
                    ui.showMessage("Cannot start project " + project.getName() + ": " + e.getMessage());
                    logger.log("Could not start project " + project.getName() + ": " + e.getMessage());
                }
            }
        }

        if (startedAny) {
            ui.showMessage("All planned projects started.");
            return true;
        } else {
            ui.showMessage("No planned projects to start.");
            logger.log("No planned projects to start.");
            return false;
        }
    }

    private boolean workOnProjects() {
        boolean workedAny = false;

        for (Project project : company.getProjects()) {
            if (project.getStatus() == ProjectStatus.IN_PROGRESS) {
                try {
                    project.workOneTurn();
                    workedAny = true;
                    logger.log("Worked on project: " + project.getName()
                            + " | progress: " + project.getProgress()
                            + "/" + project.getRequiredWork());
                } catch (IllegalStateException e) {
                    ui.showMessage("Cannot work on project " + project.getName() + ": " + e.getMessage());
                    logger.log("Could not work on project " + project.getName() + ": " + e.getMessage());
                }
            }
        }

        if (workedAny) {
            ui.showMessage("Projects worked for one turn.");
            return true;
        } else {
            ui.showMessage("No projects in progress.");
            logger.log("No projects in progress.");
            return false;
        }
    }

    private boolean putProjectOnHold() {
        List<Project> projects = company.getProjects();

        if (projects.isEmpty()) {
            ui.showMessage("No projects available.");
            return false;
        }

        ui.showProjectsWithNumbers(projects);
        int choice = ui.readProjectNumber();

        if (choice < 1 || choice > projects.size()) {
            ui.showMessage("Invalid project number.");
            return false;
        }

        Project project = projects.get(choice - 1);

        try {
            project.putOnHold();
            ui.showMessage("Project placed on hold.");
            logger.log("Project placed on hold: " + project.getName());
            return true;
        } catch (IllegalStateException e) {
            ui.showMessage("Cannot put project on hold: " + e.getMessage());
            logger.log("Could not put project on hold: " + e.getMessage());
            return false;
        }
    }

    private boolean resumeProject() {
        List<Project> projects = company.getProjects();

        if (projects.isEmpty()) {
            ui.showMessage("No projects available.");
            return false;
        }

        ui.showProjectsWithNumbers(projects);
        int choice = ui.readProjectNumber();

        if (choice < 1 || choice > projects.size()) {
            ui.showMessage("Invalid project number.");
            return false;
        }

        Project project = projects.get(choice - 1);

        try {
            project.resume();
            ui.showMessage("Project resumed.");
            logger.log("Project resumed: " + project.getName());
            return true;
        } catch (IllegalStateException e) {
            ui.showMessage("Cannot resume project: " + e.getMessage());
            logger.log("Could not resume project: " + e.getMessage());
            return false;
        }
    }

    private boolean cancelProject() {
        List<Project> projects = company.getProjects();

        if (projects.isEmpty()) {
            ui.showMessage("No projects available.");
            return false;
        }

        ui.showProjectsWithNumbers(projects);
        int choice = ui.readProjectNumber();

        if (choice < 1 || choice > projects.size()) {
            ui.showMessage("Invalid project number.");
            return false;
        }

        Project project = projects.get(choice - 1);

        try {
            project.cancel();
            ui.showMessage("Project cancelled.");
            logger.log("Project cancelled: " + project.getName());
            return true;
        } catch (IllegalStateException e) {
            ui.showMessage("Cannot cancel project: " + e.getMessage());
            logger.log("Could not cancel project: " + e.getMessage());
            return false;
        }
    }

    private void endTurn() {
        try {
            company.paySalaries();
            logger.log("Salaries paid. Budget is now: " + company.getBudget());
            applyRandomEvent();
        } catch (IllegalStateException e) {
            ui.showMessage("Turn error: " + e.getMessage());
            logger.log("Turn error: " + e.getMessage());
            running = false;
            return;
        }

        logCompanyState();

        if (allProjectsFinished()) {
            ui.showMessage("You win! All projects are finished.");
            logger.log("RESULT: WIN. All projects finished.");
            running = false;
            return;
        }

        if (company.isBankrupt()) {
            ui.showMessage("You lose! Budget fell below zero.");
            logger.log("RESULT: LOSS. Budget fell below zero.");
            running = false;
            return;
        }

        if (turn >= maxTurns) {
            ui.showMessage("Maximum turns reached. Game over!");
            logger.log("RESULT: GAME OVER. Maximum turns reached.");
            running = false;
            return;
        }

        turn++;
    }

    private void applyRandomEvent() {
        if (events.isEmpty()) {
            return;
        }

        if (random.nextInt(100) < 30) {
            GameEvent event = events.get(random.nextInt(events.size()));
            event.apply(company);
            ui.showMessage("EVENT: " + event.getDescription());
            logger.log("EVENT: " + event.getDescription());
        }
    }

    private void logCompanyState() {
        logger.log("--- End of turn " + turn + " ---");
        logger.log("Budget: " + company.getBudget());

        for (Project project : company.getProjects()) {
            logger.log("Project: " + project.getName()
                    + " | status: " + project.getStatus()
                    + " | progress: " + project.getProgress()
                    + "/" + project.getRequiredWork());
        }
    }

    private boolean allProjectsFinished() {
        if (company.getProjects().isEmpty()) {
            return false;
        }

        for (Project project : company.getProjects()) {
            if (!project.isFinished()) {
                return false;
            }
        }

        return true;
    }
}