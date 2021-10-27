package ru.ifmo.egalkin.vought.model.enums;

import ch.qos.logback.core.util.AggregationType;

public enum EventAggregationType {
    DAY("День"),
    WEEK("Неделю"),
    MONTH("Месяц");

    private final String description;

    EventAggregationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
