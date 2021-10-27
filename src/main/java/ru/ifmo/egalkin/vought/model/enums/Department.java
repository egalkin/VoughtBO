package ru.ifmo.egalkin.vought.model.enums;

/**
 * Created by egalkin
 * Date: 13.10.2021
 */
public enum Department {
    HEAD("Руководство", "ROLE_HEAD"),
    HERO("Герои", "ROLE_HERO"),
    PR("PR служба", "ROLE_PR_MANAGER"),
    SECURITY("Служба охраны", "ROLE_SECURITY_MANAGER"),
    LABORATORY("Лаборатория", "ROLE_SCIENTIST");

    private final String description;
    private final String correspondentRole;

    Department(String description, String correspondentRole) {
        this.description = description;
        this.correspondentRole = correspondentRole;
    }

    public String getDescription() {
        return description;
    }

    public String getCorrespondentRole() {
        return correspondentRole;
    }

}
