package ru.ifmo.egalkin.vought.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ifmo.egalkin.vought.model.enums.Department;

@Data
@AllArgsConstructor
public class HeroUpdateRequest {
    private String firstName;
    private String lastName;
    private String nickname;
    private String powerDescription;
}
