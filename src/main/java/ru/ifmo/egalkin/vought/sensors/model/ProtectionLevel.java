package ru.ifmo.egalkin.vought.sensors.model;

public enum ProtectionLevel {
    LOW("Низкий"),
    MEDIUM("Средний"),
    HIGH("Высокий");

    private final String description;

    ProtectionLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ProtectionLevel resolveProtectionLevel(Integer heroesNumber) {
        if (heroesNumber < 5) {
            return LOW;
        } else if (heroesNumber <=10) {
            return MEDIUM;
        } else {
            return HIGH;
        }
    }


}
