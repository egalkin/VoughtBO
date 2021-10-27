package ru.ifmo.egalkin.vought.validation.validators;

import ru.ifmo.egalkin.vought.controller.request.dto.EventDateTimeDto;
import ru.ifmo.egalkin.vought.validation.EventDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class EventDateTimeValidator implements ConstraintValidator<EventDateTime, EventDateTimeDto> {

    @Override
    public boolean isValid(EventDateTimeDto eventDateTimeDto, ConstraintValidatorContext constraintValidatorContext) {
        if (eventDateTimeDto == null) {
            return true;
        }
        try {
            return eventDateTimeDto.toLocalDateTime().isAfter(LocalDateTime.now());
        } catch (DateTimeParseException ex) {
            return true;
        }
    }
}
