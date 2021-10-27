package ru.ifmo.egalkin.vought.model.enums;

/**
 * Created by egalkin
 * Date: 13.10.2021
 */
public enum ApplicationType {
    MEETING("Встреча"),
    PR_STRATEGY("PR стратегия"),
    RESEARCH("Исследование");

    private final String description;

    ApplicationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
