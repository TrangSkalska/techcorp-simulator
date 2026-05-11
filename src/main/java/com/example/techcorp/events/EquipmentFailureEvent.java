package com.example.techcorp.events;

import com.example.techcorp.Company;

public class EquipmentFailureEvent implements GameEvent {
    @Override
    public void apply(Company company) {
        company.reduceBudget(3000);
    }

    @Override
    public String getDescription() {
        return "Equipment failure! Repairs cost 3000.";
    }
}