package ru.ifmo.egalkin.vought.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.egalkin.vought.model.Application;
import ru.ifmo.egalkin.vought.model.enums.ApplicationType;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScientistApplicationRequest {

    @NotEmpty(message = "У заявки должно быть имя")
    private String name;
    ApplicationType applicationType;
    private String description;
}
