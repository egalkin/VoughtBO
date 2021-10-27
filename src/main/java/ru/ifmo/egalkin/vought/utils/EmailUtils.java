package ru.ifmo.egalkin.vought.utils;

import java.util.concurrent.atomic.AtomicLong;

public class EmailUtils {

    private final static AtomicLong userCounter = new AtomicLong(0);
    private final static String EMAIL_TEMPLATE = "%s.%s%d@vought.com";

    public static String generateEmailForEmployee(String firstName, String lastName) {
        return String.format(EMAIL_TEMPLATE, firstName, lastName, userCounter.incrementAndGet());
    }

}
