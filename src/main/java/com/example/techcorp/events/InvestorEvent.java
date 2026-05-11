package com.example.techcorp.events;

import com.example.techcorp.Company;

public class InvestorEvent implements GameEvent {
    @Override
    public void apply(Company company) {
        company.increaseBudget(10000);
    }

    @Override
    public String getDescription() {
        return "Investor appeared! Company received 10000 extra budget.";
    }
}