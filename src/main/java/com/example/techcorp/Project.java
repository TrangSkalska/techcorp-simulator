package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private int requiredWork;
    private int progress;
    private List<Workable> team = new ArrayList<>();
    private ProjectStatus status;

    public Project(String name, int requiredWork) {
        validateName(name);
        validateRequiredWork(requiredWork);
        this.name = name;
        this.requiredWork = requiredWork;
        this.progress = 0;
        this.status = ProjectStatus.PLANNED;
    }

    public void addWorker(Workable workable) {
        if (workable == null) {
            throw new IllegalArgumentException("Worker cannot be null.");
        }
        team.add(workable);
    }

    public void addEmployee(Employee employee) {
        addWorker(employee);
    }

    public void start() {
        if (status == ProjectStatus.PLANNED) {
            status = ProjectStatus.IN_PROGRESS;
            System.out.println("Project " + name + " started.");
        }
    }

    public void putOnHold() {
        if (status == ProjectStatus.IN_PROGRESS) {
            status = ProjectStatus.ON_HOLD;
            System.out.println("Project " + name + " put ON_HOLD.");
        }
    }

    public void resume() {
        if (status == ProjectStatus.ON_HOLD) {
            status = ProjectStatus.IN_PROGRESS;
            System.out.println("Project " + name + " resumed.");
        }
    }

    public void cancel() {
        if (status != ProjectStatus.FINISHED) {
            status = ProjectStatus.CANCELLED;
            System.out.println("Project " + name + " was CANCELLED.");
        }
    }

    public void workOneTurn() {
        if (status != ProjectStatus.IN_PROGRESS) {
            return;
        }
        for (Workable workable : team) {
            progress += workable.work();
        }
        if (progress >= requiredWork) {
            progress = requiredWork;
            status = ProjectStatus.FINISHED;
            System.out.println("Project " + name + " is FINISHED.");
        }
    }

    public boolean isFinished() {
        return status == ProjectStatus.FINISHED;
    }

    public String getName() {
        return name;
    }

    public int getRequiredWork() {
        return requiredWork;
    }

    public int getProgress() {
        return progress;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be null or blank.");
        }
    }

    private void validateRequiredWork(int requiredWork) {
        if (requiredWork <= 0) {
            throw new IllegalArgumentException("Required work must be greater than 0.");
        }
    }
}