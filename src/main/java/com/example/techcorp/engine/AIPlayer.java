package com.example.techcorp.engine;

import com.example.techcorp.Company;
import com.example.techcorp.Project;
import com.example.techcorp.ProjectStatus;

public class AIPlayer {
    private Company company;

    public AIPlayer(Company company) {
        if (company == null) {
            throw new NullPointerException("AI company cannot be null.");
        }

        this.company = company;
    }

    public String makeDecision() {
        boolean startedProject = false;

        for (Project project : company.getProjects()) {
            if (project.getStatus() == ProjectStatus.PLANNED) {
                project.start();
                startedProject = true;
            }
        }

        if (startedProject) {
            return "AI started its planned projects.";
        }

        boolean worked = false;

        for (Project project : company.getProjects()) {
            if (project.getStatus() == ProjectStatus.IN_PROGRESS) {
                project.workOneTurn();
                worked = true;
            }
        }

        if (worked) {
            return "AI worked on its projects.";
        }

        return "AI skipped the turn.";
    }

    public Company getCompany() {
        return company;
    }
}