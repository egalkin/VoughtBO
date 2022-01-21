package ru.ifmo.egalkin.vought.sensors.model;

public enum TensionLevel {
    LOW("Низкий"),
    MEDIUM("Средний"),
    HIGH("Высокий");

    private final String description;

    TensionLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static TensionLevel resolveTensionLevel(Integer cityIncidentNumber) {
        if (cityIncidentNumber < 5) {
            return LOW;
        } else if (cityIncidentNumber <=10) {
            return MEDIUM;
        } else {
            return HIGH;
        }
    }

}
