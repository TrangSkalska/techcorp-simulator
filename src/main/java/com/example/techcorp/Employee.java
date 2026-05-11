package com.example.techcorp;

public abstract class Employee implements Workable {
    private String name;
    private int skill;
    private double salary;

    public Employee(String name, int skill, double salary) {
        if (name == null) {
            throw new NullPointerException("Employee name cannot be null.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Employee name cannot be blank.");
        }
        if (skill <= 0) {
            throw new IllegalArgumentException("Skill must be positive.");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }

        this.name = name;
        this.skill = skill;
        this.salary = salary;
    }

    public abstract int work();

    public String getName() {
        return name;
    }

    public int getSkill() {
        return skill;
    }

    public double getSalary() {
        return salary;
    }

    public abstract String getRoleName();
}