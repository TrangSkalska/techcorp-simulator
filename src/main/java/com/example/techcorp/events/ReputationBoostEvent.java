package com.example.techcorp.events;

import com.example.techcorp.Company;

public class ReputationBoostEvent implements GameEvent {
    @Override
    public void apply(Company company) {
        company.increaseBudget(7000);
    }

    @Override
    public String getDescription() {
        return "Great publicity! Company gained 7000 extra budget.";
    }
}