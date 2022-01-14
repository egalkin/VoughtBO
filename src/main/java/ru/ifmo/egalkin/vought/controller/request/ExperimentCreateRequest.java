package ru.ifmo.egalkin.vought.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentCreateRequest {
    @NotEmpty(message = "У эксперимента должно быть название")
    private String name;

    private String description;

    private String goal;

}
