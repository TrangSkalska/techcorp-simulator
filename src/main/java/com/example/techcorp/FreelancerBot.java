package com.example.techcorp;

public class FreelancerBot implements Workable {
    private String name;
    private int productivity;

    public FreelancerBot(String name, int productivity) {
        if (name == null) {
            throw new NullPointerException("Freelancer name cannot be null.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Freelancer name cannot be blank.");
        }
        if (productivity <= 0) {
            throw new IllegalArgumentException("Productivity must be positive.");
        }

        this.name = name;
        this.productivity = productivity;
    }

    @Override
    public int work() {
        return productivity;
    }

    public String getName() {
        return name;
    }
}