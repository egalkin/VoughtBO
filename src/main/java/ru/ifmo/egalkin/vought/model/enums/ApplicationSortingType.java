package ru.ifmo.egalkin.vought.model.enums;

public enum ApplicationSortingType {

    DATE_ASC("Дате\u25B2"),
    DATE_DESC("Дате\u25BC"),
    CREATOR_ASC("Создателю\u25B2"),
    CREATOR_DESC("Создателю\u25BC"),
    STATUS_ASC("Статусу\u25B2"),
    STATUS_DESC("Статусу\u25BC");

    private final String description;

    ApplicationSortingType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
