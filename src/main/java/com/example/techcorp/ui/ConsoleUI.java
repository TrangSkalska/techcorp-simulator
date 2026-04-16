package com.example.techcorp.ui;

import com.example.techcorp.Company;
import com.example.techcorp.Project;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);

    public void showTurnHeader(int turn) {
        System.out.println();
        System.out.println("============");
        System.out.println("TURN " + turn);
        System.out.println("============");
    }

    public void showMainMenu() {
        System.out.println("Choose an action:");
        System.out.println("1. Show company status");
        System.out.println("2. Start planned projects");
        System.out.println("3. Work on projects");
        System.out.println("4. Show unfinished projects");
        System.out.println("5. Put project on hold");
        System.out.println("6. Resume project");
        System.out.println("7. Exit game");
    }

    public int readMenuChoice() {
        while (true) {
            System.out.print("Enter choice: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a number.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice >= 1 && choice <= 7) {
                return choice;
            }

            System.out.println("Choose a number from 1 to 7.");
        }
    }

    public void showCompanyStatus(Company company) {
        company.printStatus();
    }

    public void showUnfinishedProjects(List<Project> projects) {
        boolean found = false;
        System.out.println("=== UNFINISHED PROJECTS ===");
        for (Project project : projects) {
            if (!project.isFinished()) {
                found = true;
                System.out.println(" - " + project.getName()
                        + " | status: " + project.getStatus()
                        + " | progress: " + project.getProgress()
                        + "/" + project.getRequiredWork());
            }
        }

        if (!found) {
            System.out.println("No unfinished projects.");
        }
    }

    public void showProjectsWithNumbers(List<Project> projects) {
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            System.out.println((i + 1) + ". " + project.getName()
                    + " | status: " + project.getStatus()
                    + " | progress: " + project.getProgress()
                    + "/" + project.getRequiredWork());
        }
    }

    public int readProjectNumber() {
        while (true) {
            System.out.print("Choose project number: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a number.");
                scanner.nextLine();
                continue;
            }

            int number = scanner.nextInt();
            scanner.nextLine();
            return number;
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}