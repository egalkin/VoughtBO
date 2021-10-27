package ru.ifmo.egalkin.vought.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.egalkin.vought.model.enums.IncidentType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentCreationRequest {
    @NotEmpty(message="Местоположение должно быть задано")
    String address;
    IncidentType incidentType;
    Integer armamentLevel;
    @Min(value = 1, message = "Число преступников должно быть положительно")
    @Max(value = 1000000, message = "Число преступников слишком велико, мы не справимся")
    Integer enemiesNumber;
    String info;
}
