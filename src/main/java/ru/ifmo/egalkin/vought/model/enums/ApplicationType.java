package ru.ifmo.egalkin.vought.model.enums;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by egalkin
 * Date: 13.10.2021
 */
public enum ApplicationType {
    MEETING("Встреча"),
    PR_STRATEGY("PR стратегия"),
    RESEARCH("Исследование"),
    EQUIPMENT("Оборудование");

    private final String description;

    ApplicationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ApplicationType[] getLabApplicationType() {

        return new ApplicationType[]{RESEARCH, EQUIPMENT};
    }

}



