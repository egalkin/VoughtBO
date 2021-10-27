package ru.ifmo.egalkin.vought.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.ifmo.egalkin.vought.controller.request.dto.EventDateTimeDto;
import ru.ifmo.egalkin.vought.validation.EventDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroApplicationCreationRequest {
    @NotEmpty(message = "У заявки должно быть имя")
    private String name;

    private String description;

    @Valid
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "У встречи должна быть дата проведения")
    @EventDateTime(message = "Встреча должна начинаться не раньше текущего момента")
    private EventDateTimeDto eventDateTimeDto;

    private Long meetingAimEmployeeId;
}
