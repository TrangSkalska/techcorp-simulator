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
    private Company playerCompany;
    private Company aiCompany;
    private AIPlayer aiPlayer;
    private ConsoleUI ui;

    private boolean running;
    private int turn;
    private final int maxTurns = 10;
    private GameResult result;

    private List<GameEvent> events = new ArrayList<>();
    private Random random = new Random();
    private GameLogger logger = new GameLogger("game-results.txt");

    public GameEngine(Company playerCompany, Company aiCompany, ConsoleUI ui) {
        if (playerCompany == null) {
            throw new NullPointerException("Player company cannot be null.");
        }

        if (aiCompany == null) {
            throw new NullPointerException("AI company cannot be null.");
        }

        if (ui == null) {
            throw new NullPointerException("Console UI cannot be null.");
        }

        this.playerCompany = playerCompany;
        this.aiCompany = aiCompany;
        this.aiPlayer = new AIPlayer(aiCompany);
        this.ui = ui;
        this.running = true;
        this.turn = 1;
        this.result = GameResult.IN_PROGRESS;

        events.add(new MarketSlowdownEvent());
        events.add(new InvestorEvent());
        events.add(new ReputationBoostEvent());
        events.add(new EquipmentFailureEvent());

        logger.clear();
        logger.log("Game started.");
        logger.log("Player company: " + playerCompany.getName());
        logger.log("AI company: " + aiCompany.getName());
        logger.log("Player starting budget: " + playerCompany.getBudget());
        logger.log("AI starting budget: " + aiCompany.getBudget());
    }

    public void start() {
        while (running) {
            ui.showTurnHeader(turn);

            ui.showMessage("=== PLAYER COMPANY ===");
            ui.showCompanyStatus(playerCompany);

            ui.showMessage("=== AI COMPANY ===");
            ui.showCompanyStatus(aiCompany);

            ui.showMainMenu();

            int choice = ui.readMenuChoice();
            logger.log("Turn " + turn + ": player chose option " + choice);

            boolean actionConsumesTurn = handleChoice(choice);

            if (running && actionConsumesTurn) {
                aiTurn();
                endTurn();
            }
        }

        logger.log("Game ended.");
        logger.log("Final result: " + result);
    }

    private boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                ui.showCompanyStatus(playerCompany);
                return false;

            case 2:
                return startPlannedProjects(playerCompany, "Player");

            case 3:
                return workOnProjects(playerCompany, "Player");

            case 4:
                ui.showUnfinishedProjects(playerCompany.getProjects());
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

    private boolean startPlannedProjects(Company company, String owner) {
        boolean startedAny = false;

        for (Project project : company.getProjects()) {
            if (project.getStatus() == ProjectStatus.PLANNED) {
                try {
                    project.start();
                    startedAny = true;
                    logger.log(owner + " started project: " + project.getName());
                } catch (IllegalStateException e) {
                    logger.log(owner + " could not start project " + project.getName() + ": " + e.getMessage());
                }
            }
        }

        if (startedAny) {
            ui.showMessage(owner + " started planned projects.");
            return true;
        } else {
            ui.showMessage("No planned projects to start.");
            logger.log(owner + " had no planned projects to start.");
            return false;
        }
    }

    private boolean workOnProjects(Company company, String owner) {
        boolean workedAny = false;

        for (Project project : company.getProjects()) {
            if (project.getStatus() == ProjectStatus.IN_PROGRESS) {
                try {
                    project.workOneTurn();
                    workedAny = true;
                    logger.log(owner + " worked on project: " + project.getName()
                            + " | progress: " + project.getProgress()
                            + "/" + project.getRequiredWork());
                } catch (IllegalStateException e) {
                    logger.log(owner + " could not work on project " + project.getName() + ": " + e.getMessage());
                }
            }
        }

        if (workedAny) {
            ui.showMessage(owner + " worked on projects.");
            return true;
        } else {
            ui.showMessage("No projects in progress.");
            logger.log(owner + " had no projects in progress.");
            return false;
        }
    }

    private boolean putProjectOnHold() {
        List<Project> projects = playerCompany.getProjects();

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
            logger.log("Player placed project on hold: " + project.getName());
            return true;
        } catch (IllegalStateException e) {
            ui.showMessage("Cannot put project on hold: " + e.getMessage());
            logger.log("Could not put project on hold: " + e.getMessage());
            return false;
        }
    }

    private boolean resumeProject() {
        List<Project> projects = playerCompany.getProjects();

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
            logger.log("Player resumed project: " + project.getName());
            return true;
        } catch (IllegalStateException e) {
            ui.showMessage("Cannot resume project: " + e.getMessage());
            logger.log("Could not resume project: " + e.getMessage());
            return false;
        }
    }

    private boolean cancelProject() {
        List<Project> projects = playerCompany.getProjects();

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
            logger.log("Player cancelled project: " + project.getName());
            return true;
        } catch (IllegalStateException e) {
            ui.showMessage("Cannot cancel project: " + e.getMessage());
            logger.log("Could not cancel project: " + e.getMessage());
            return false;
        }
    }

    private void aiTurn() {
        try {
            String decision = aiPlayer.makeDecision();
            ui.showMessage("AI: " + decision);
            logger.log("AI decision: " + decision);
        } catch (IllegalStateException e) {
            ui.showMessage("AI could not act: " + e.getMessage());
            logger.log("AI error: " + e.getMessage());
        }
    }

    private void endTurn() {
        try {
            playerCompany.paySalaries();
            aiCompany.paySalaries();

            logger.log("Player salaries paid. Budget: " + playerCompany.getBudget());
            logger.log("AI salaries paid. Budget: " + aiCompany.getBudget());

            applyRandomEvent(playerCompany, "Player");
            applyRandomEvent(aiCompany, "AI");

        } catch (IllegalStateException e) {
            ui.showMessage("Turn error: " + e.getMessage());
            logger.log("Turn error: " + e.getMessage());
            running = false;
            return;
        }

        logCompanyState(playerCompany, "Player");
        logCompanyState(aiCompany, "AI");

        evaluateResult();

        if (result != GameResult.IN_PROGRESS) {
            showResult();
            running = false;
            return;
        }

        turn++;
    }

    private void applyRandomEvent(Company company, String owner) {
        if (events.isEmpty()) {
            return;
        }

        if (random.nextInt(100) < 30) {
            GameEvent event = events.get(random.nextInt(events.size()));
            event.apply(company);

            String message = owner + " EVENT: " + event.getDescription();
            ui.showMessage(message);
            logger.log(message);
        }
    }

    private void evaluateResult() {
        boolean playerFinished = allProjectsFinished(playerCompany);
        boolean aiFinished = allProjectsFinished(aiCompany);

        if (playerCompany.isBankrupt() && aiCompany.isBankrupt()) {
            result = GameResult.DRAW;
            return;
        }

        if (playerCompany.isBankrupt()) {
            result = GameResult.AI_WINS;
            return;
        }

        if (aiCompany.isBankrupt()) {
            result = GameResult.PLAYER_WINS;
            return;
        }

        if (playerFinished && !aiFinished) {
            result = GameResult.PLAYER_WINS;
            return;
        }

        if (aiFinished && !playerFinished) {
            result = GameResult.AI_WINS;
            return;
        }

        if (playerFinished && aiFinished) {
            compareCompanyScores();
            return;
        }

        if (turn >= maxTurns) {
            compareCompanyScores();
        }
    }

    private void compareCompanyScores() {
        double playerScore = calculateCompanyScore(playerCompany);
        double aiScore = calculateCompanyScore(aiCompany);

        logger.log("Player score: " + playerScore);
        logger.log("AI score: " + aiScore);

        if (playerScore > aiScore) {
            result = GameResult.PLAYER_WINS;
        } else if (aiScore > playerScore) {
            result = GameResult.AI_WINS;
        } else {
            result = GameResult.DRAW;
        }
    }

    private double calculateCompanyScore(Company company) {
        int finishedProjects = 0;

        for (Project project : company.getProjects()) {
            if (project.isFinished()) {
                finishedProjects++;
            }
        }

        return company.getBudget() + finishedProjects * 10000;
    }

    private void showResult() {
        if (result == GameResult.PLAYER_WINS) {
            ui.showMessage("You win! Your company defeated the AI competitor.");
        } else if (result == GameResult.AI_WINS) {
            ui.showMessage("You lose! The AI competitor performed better.");
        } else if (result == GameResult.DRAW) {
            ui.showMessage("Draw! Both companies performed equally.");
        }

        logger.log("RESULT: " + result);
    }

    private void logCompanyState(Company company, String owner) {
        logger.log("--- " + owner + " company after turn " + turn + " ---");
        logger.log("Budget: " + company.getBudget());

        for (Project project : company.getProjects()) {
            logger.log("Project: " + project.getName()
                    + " | status: " + project.getStatus()
                    + " | progress: " + project.getProgress()
                    + "/" + project.getRequiredWork());
        }
    }

    private boolean allProjectsFinished(Company company) {
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