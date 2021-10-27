package ru.ifmo.egalkin.vought.model.enums;

/**
 * Created by egalkin
 * Date: 13.10.2021
 */
public enum IncidentType {

    ROBBERY("Ограбление"),
    TERRORIST_ATTACK("Терракт"),
    HIJACK("Угон");

    private final String description;

    IncidentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
