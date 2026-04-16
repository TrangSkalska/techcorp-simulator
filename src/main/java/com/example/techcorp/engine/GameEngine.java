package com.example.techcorp.engine;

import com.example.techcorp.Company;
import com.example.techcorp.Project;
import com.example.techcorp.ProjectStatus;
import com.example.techcorp.ui.ConsoleUI;

import java.util.List;

public class GameEngine {
    private Company company;
    private ConsoleUI ui;
    private boolean running;
    private int turn;
    private final int maxTurns = 10;

    public GameEngine(Company company, ConsoleUI ui) {
        this.company = company;
        this.ui = ui;
        this.running = true;
        this.turn = 1;
    }

    public void start() {
        while (running) {
            ui.showTurnHeader(turn);
            ui.showCompanyStatus(company);
            ui.showMainMenu();

            int choice = ui.readMenuChoice();
            boolean actionConsumesTurn = handleChoice(choice);

            if (running && actionConsumesTurn) {
                endTurn();
            }
        }
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
                running = false;
                ui.showMessage("Thanks for playing!");
                return false;
            default:
                ui.showMessage("Invalid menu option.");
                return false;
        }
    }

    private boolean startPlannedProjects() {
        boolean startedAny = false;

        for (Project project : company.getProjects()) {
            if (project.getStatus() == ProjectStatus.PLANNED) {
                project.start();
                startedAny = true;
            }
        }

        if (startedAny) {
            ui.showMessage("All planned projects started.");
            return true;
        } else {
            ui.showMessage("No planned projects to start.");
            return false;
        }
    }

    private boolean workOnProjects() {
        boolean workedAny = false;

        for (Project project : company.getProjects()) {
            if (project.getStatus() == ProjectStatus.IN_PROGRESS) {
                project.workOneTurn();
                workedAny = true;
            }
        }

        if (workedAny) {
            ui.showMessage("Projects worked for one turn.");
            return true;
        } else {
            ui.showMessage("No projects in progress.");
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

        if (project.getStatus() == ProjectStatus.IN_PROGRESS) {
            project.putOnHold();
            ui.showMessage("Project placed on hold.");
            return true;
        } else {
            ui.showMessage("Only IN_PROGRESS projects can be put on hold.");
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

        if (project.getStatus() == ProjectStatus.ON_HOLD) {
            project.resume();
            ui.showMessage("Project resumed.");
            return true;
        } else {
            ui.showMessage("Only ON_HOLD projects can be resumed.");
            return false;
        }
    }

    private void endTurn() {
        company.paySalaries();

        if (allProjectsFinished()) {
            ui.showMessage("You win! All projects are finished.");
            running = false;
            return;
        }

        if (company.isBankrupt()) {
            ui.showMessage("You lose! Budget fell below zero.");
            running = false;
            return;
        }

        if (turn >= maxTurns) {
            ui.showMessage("Maximum turns reached. Game over!");
            running = false;
            return;
        }

        turn++;
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