package com.example.techcorp;

public class Main {
    public static void main(String[] args) {
        Company company = new Company("TechCorp", 50000);

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

        // Different combinations of employees
        mobileApp.addEmployee(anna);  // developer
        mobileApp.addEmployee(jan);   // intern

        backendSystem.addEmployee(piotr); // tester
        backendSystem.addEmployee(ola);   // manager

        FreelancerBot bot = new FreelancerBot("Freelancer", 5);
        mobileApp.addWorker(bot);   

        company.startProject(mobileApp);
        company.startProject(backendSystem);

        mobileApp.start();
        backendSystem.start();

        int turn = 0;
        while (!mobileApp.isFinished() || !backendSystem.isFinished()) {
            turn++;
            System.out.println("Turn " + turn);

            if (!mobileApp.isFinished()) {
                mobileApp.workOneTurn();
            }
            if (!backendSystem.isFinished()) {
                backendSystem.workOneTurn();
            }

            company.printStatus();
            System.out.println();
        }

        System.out.println("Simulation finished in " + turn + " turns.");
        System.out.println("Mobile App finished: " + mobileApp.isFinished());
        System.out.println("Backend System finished: " + backendSystem.isFinished());
    }
}
