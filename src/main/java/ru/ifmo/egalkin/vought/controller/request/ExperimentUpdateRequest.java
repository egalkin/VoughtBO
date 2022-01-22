package ru.ifmo.egalkin.vought.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentUpdateRequest {
    private String goal;
    private String description;
}
