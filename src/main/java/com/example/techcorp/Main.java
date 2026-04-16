package com.example.techcorp;

import com.example.techcorp.engine.GameEngine;
import com.example.techcorp.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Company company = new Company("TechCorp", 150000);

        Employee anna = new Developer("Anna", 8, 7000);
        Employee piotr = new Tester("Piotr", 6, 6500);
        Employee ola = new Manager("Ola", 7, 9000);
        Employee jan = new Intern("Jan", 4, 3000);

        company.hire(anna);
        company.hire(piotr);
        company.hire(ola);
        company.hire(jan);

        Project mobileApp = new Project("Mobile App", 40);
        Project backendSystem = new Project("Backend System", 40);

        mobileApp.addEmployee(anna);
        mobileApp.addEmployee(jan);

        backendSystem.addEmployee(piotr);
        backendSystem.addEmployee(ola);

        FreelancerBot bot = new FreelancerBot("Freelancer", 5);
        mobileApp.addWorker(bot);

        company.startProject(mobileApp);
        company.startProject(backendSystem);

        ConsoleUI ui = new ConsoleUI();
        GameEngine engine = new GameEngine(company, ui);
        engine.start();
    }
}