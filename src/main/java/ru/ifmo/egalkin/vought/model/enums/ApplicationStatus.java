package ru.ifmo.egalkin.vought.model.enums;

/**
 * Created by egalkin
 * Date: 13.10.2021
 */

public enum ApplicationStatus {
    PENDING("На рассмотрении"),
    APPROVED("Согласовано"),
    REJECTED("Отказано");

    private final String description;

    ApplicationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
