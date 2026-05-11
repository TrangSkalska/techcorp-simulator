package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private String name;
    private double budget;
    private List<Employee> employees = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();

    public Company(String name, double budget) {
        // Preconditions
        if (name == null) {
            throw new NullPointerException("Company name cannot be null.");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be blank.");
        }

        if (budget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative.");
        }

        this.name = name;
        this.budget = budget;
    }

    public void hire(Employee employee) {
        // Precondition
        if (employee == null) {
            throw new NullPointerException("Employee cannot be null.");
        }

        employees.add(employee);
    }

    public void startProject(Project project) {
        // Precondition
        if (project == null) {
            throw new NullPointerException("Project cannot be null.");
        }

        projects.add(project);
    }

    public void reduceBudget(double amount) {
        // Precondition
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        budget -= amount;

        // Postcondition check
        if (Double.isNaN(budget)) {
            throw new IllegalStateException("Budget became invalid after reducing budget.");
        }
    }

    public void increaseBudget(double amount) {
        // Precondition
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        budget += amount;

        // Postcondition check
        if (Double.isNaN(budget)) {
            throw new IllegalStateException("Budget became invalid after increasing budget.");
        }
    }

    public void paySalaries() {
        for (Employee employee : employees) {
            budget -= employee.getSalary();
        }

        // Postcondition check
        if (Double.isNaN(budget)) {
            throw new IllegalStateException("Budget became invalid after paying salaries.");
        }
    }

    public boolean isBankrupt() {
        return budget < 0;
    }

    public String getName() {
        return name;
    }

    public double getBudget() {
        return budget;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void printStatus() {
        System.out.println("=== COMPANY STATUS ===");
        System.out.println("Name: " + name);
        System.out.println("Budget: " + budget);
        System.out.println("Employees: " + employees.size());
        System.out.println("Projects: " + projects.size());
        System.out.println();

        if (projects.isEmpty()) {
            System.out.println("No active projects.");
        } else {
            System.out.println("Projects:");
            for (Project project : projects) {
                System.out.println(" - " + project.getName()
                        + " | status: " + project.getStatus()
                        + " | progress: " + project.getProgress()
                        + "/" + project.getRequiredWork()
                        + " | finished: " + project.isFinished());
            }
        }

        System.out.println("====================================");
    }
}