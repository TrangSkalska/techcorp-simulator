package com.example.techcorp;

public class FreelancerBot implements Workable {

    private String name;
    private int productivity;

    public FreelancerBot(String name, int productivity) {
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