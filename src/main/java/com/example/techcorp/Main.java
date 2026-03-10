package com.example.techcorp;

public class Main {
    public static void main(String[] args) {
        Company company = new Company("TechCorp", 50000);

        // 1. Add a third employee
        Employee anna = new Employee("Anna", 8, 7000);
        Employee piotr = new Employee("Piotr", 6, 6500);
        Employee john = new Employee("John", 5, 6000);   // third employee

        company.hire(anna);
        company.hire(piotr);
        company.hire(john);

        // 2. Create a first project with increased required work
        Project mobileApp = new Project("Mobile App", 40);  // 3. required work increased

        // Assign three employees to the first project
        mobileApp.addEmployee(anna);
        mobileApp.addEmployee(piotr);
        mobileApp.addEmployee(john);

        company.startProject(mobileApp);

        // 2. Create a second project
        Project webPortal = new Project("Web Portal", 25);
        webPortal.addEmployee(anna);      // you can assign any employees you like
        webPortal.addEmployee(piotr);

        company.startProject(webPortal);

        // 4. Determine how many turns are required to complete the first project
        int turns = 0;
        while (mobileApp.getProgress() < mobileApp.getRequiredWork()) {
            mobileApp.workOneTurn();
            turns++;
        }

        System.out.println("Mobile App project completed in " + turns + " turns.");
    }
}
