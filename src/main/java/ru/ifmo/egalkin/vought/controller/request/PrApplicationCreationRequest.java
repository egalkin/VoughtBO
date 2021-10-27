package ru.ifmo.egalkin.vought.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrApplicationCreationRequest {
    @NotEmpty(message = "У заявки должно быть имя")
    private String name;

    private String description;
}
