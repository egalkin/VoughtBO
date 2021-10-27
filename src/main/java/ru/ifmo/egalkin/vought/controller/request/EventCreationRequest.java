package ru.ifmo.egalkin.vought.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.egalkin.vought.controller.request.dto.EventDateTimeDto;
import ru.ifmo.egalkin.vought.validation.EventDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCreationRequest {
    @NotEmpty(message = "У мероприятия должно быть имя")
    private String name;
    private int priority;

    @Valid
    @NotNull(message = "Мероприятию должна быь проставлена дата начала")
    @EventDateTime(message = "Мероприятие должно начинаться не раньше текущего момента")
    private EventDateTimeDto eventDateTimeDto;

    @NotEmpty(message = "У мероприятия должно быть задано местоположение")
    private String address;
    private String description;
    private List<Long> heroesIds;
}
