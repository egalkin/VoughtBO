package ru.ifmo.egalkin.vought.controller.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDateTimeDto {

    public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @NotNull(message = "У мероприятия должна быть задана дата проведения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate eventDate;

    @NotEmpty(message = "У мероприятия должно быть задано время проведения")
    String eventTime;

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.parse(eventDate + " " + eventTime, FORMATTER);
    }

}
