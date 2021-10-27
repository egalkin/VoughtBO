package ru.ifmo.egalkin.vought.model.enums;

public enum EmployeeSortingType {
    DEPARTMENT_ASC("Отделу\u25B2"),
    DEPARTMENT_DESC("Отделу\u25BC"),
    NAME_ASC("Имени\u25B2"),
    NAME_DESC("Имени\u25BC");

    private final String description;

    EmployeeSortingType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
