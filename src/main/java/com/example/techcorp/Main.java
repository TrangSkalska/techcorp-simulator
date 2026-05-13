package com.example.techcorp;

import com.example.techcorp.engine.GameEngine;
import com.example.techcorp.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Company playerCompany = new Company("TechCorp", 150000);

        Employee anna = new Developer("Anna", 8, 7000);
        Employee piotr = new Tester("Piotr", 6, 6500);
        Employee ola = new Manager("Ola", 7, 9000);
        Employee jan = new Intern("Jan", 4, 3000);

        playerCompany.hire(anna);
        playerCompany.hire(piotr);
        playerCompany.hire(ola);
        playerCompany.hire(jan);

        Project mobileApp = new Project("Mobile App", 40);
        Project backendSystem = new Project("Backend System", 40);

        mobileApp.addEmployee(anna);
        mobileApp.addEmployee(jan);

        backendSystem.addEmployee(piotr);
        backendSystem.addEmployee(ola);

        FreelancerBot bot = new FreelancerBot("Freelancer", 5);
        mobileApp.addWorker(bot);

        playerCompany.startProject(mobileApp);
        playerCompany.startProject(backendSystem);

        Company aiCompany = new Company("AI Systems Ltd", 150000);

        Employee botDev = new Developer("Bot Developer", 7, 7000);
        Employee botTester = new Tester("Bot Tester", 6, 6000);
        Employee botManager = new Manager("Bot Manager", 6, 8500);

        aiCompany.hire(botDev);
        aiCompany.hire(botTester);
        aiCompany.hire(botManager);

        Project aiPlatform = new Project("AI Platform", 40);
        Project aiBackend = new Project("AI Backend", 40);

        aiPlatform.addEmployee(botDev);
        aiBackend.addEmployee(botTester);
        aiBackend.addEmployee(botManager);

        aiCompany.startProject(aiPlatform);
        aiCompany.startProject(aiBackend);

        ConsoleUI ui = new ConsoleUI();
        GameEngine engine = new GameEngine(playerCompany, aiCompany, ui);
        engine.start();
    }
}