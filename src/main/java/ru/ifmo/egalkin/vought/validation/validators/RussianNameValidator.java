package ru.ifmo.egalkin.vought.validation.validators;

import ru.ifmo.egalkin.vought.validation.RussianName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RussianNameValidator implements ConstraintValidator<RussianName, String> {

    private static final String RUS_NAME_REG_EXP = "[А-ЯЁ][-А-яЁё]+";
    private Pattern ruNamePattern;

    @Override
    public void initialize(RussianName constraintAnnotation) {
        this.ruNamePattern = Pattern.compile(RUS_NAME_REG_EXP);
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (name == null || name.isEmpty()) {
            return true;
        }
        return isRusName(name) ;
    }

    private boolean isRusName(String name) {
        Matcher matcher = ruNamePattern.matcher(name);
        return matcher.matches();
    }
}
