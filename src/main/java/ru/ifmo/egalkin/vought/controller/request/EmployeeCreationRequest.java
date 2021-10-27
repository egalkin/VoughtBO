package ru.ifmo.egalkin.vought.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ifmo.egalkin.vought.model.enums.Department;
import ru.ifmo.egalkin.vought.validation.RussianName;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class EmployeeCreationRequest {
    @RussianName(message = "Введите корректное русское имя")
    @NotEmpty(message = "Имя не может быть пусто")
    private String firstName;
    @RussianName(message = "Введите корректную русскую фамилию")
    @NotEmpty(message = "Фамилия не должна быть пуста")
    private String lastName;
    private Department department;
}
