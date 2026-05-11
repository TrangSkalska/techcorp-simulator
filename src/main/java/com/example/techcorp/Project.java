package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Project {
    private String name;
    private int requiredWork;
    private int progress;
    private ProjectStatus status;
    private List<Workable> workers = new ArrayList<>();

    public Project(String name, int requiredWork) {
        if (name == null) {
            throw new NullPointerException("Project name cannot be null.");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be blank.");
        }

        if (requiredWork <= 0) {
            throw new IllegalArgumentException("Required work must be positive.");
        }

        this.name = name;
        this.requiredWork = requiredWork;
        this.progress = 0;
        this.status = ProjectStatus.PLANNED;
    }

    public void addEmployee(Employee employee) {
        addWorker(employee);
    }

    public void addWorker(Workable worker) {
        Objects.requireNonNull(worker, "Worker cannot be null.");
        workers.add(worker);
    }

    public void start() {
        if (status != ProjectStatus.PLANNED) {
            throw new IllegalStateException("Only planned projects can be started.");
        }

        status = ProjectStatus.IN_PROGRESS;
    }

    public void workOneTurn() {
        if (status != ProjectStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only projects in progress can be worked on.");
        }

        if (workers.isEmpty()) {
            throw new IllegalStateException("Project must have at least one worker.");
        }

        for (Workable worker : workers) {
            progress += worker.work();
        }

        if (progress >= requiredWork) {
            progress = requiredWork;
            status = ProjectStatus.FINISHED;
        }
    }

    public void putOnHold() {
        if (status != ProjectStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only projects in progress can be put on hold.");
        }

        status = ProjectStatus.ON_HOLD;
    }

    public void resume() {
        if (status != ProjectStatus.ON_HOLD) {
            throw new IllegalStateException("Only projects on hold can be resumed.");
        }

        status = ProjectStatus.IN_PROGRESS;
    }

    public void cancel() {
        if (status == ProjectStatus.FINISHED) {
            throw new IllegalStateException("Finished projects cannot be cancelled.");
        }

        if (status == ProjectStatus.CANCELLED) {
            throw new IllegalStateException("Project is already cancelled.");
        }

        status = ProjectStatus.CANCELLED;
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
}