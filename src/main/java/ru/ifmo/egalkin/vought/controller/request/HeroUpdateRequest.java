package ru.ifmo.egalkin.vought.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.egalkin.vought.model.enums.Department;
import ru.ifmo.egalkin.vought.validation.RussianName;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroUpdateRequest {
    @RussianName(message = "Введите корректное русское имя")
    @NotEmpty(message = "Имя не может быть пусто")
    private String firstName;
    @RussianName(message = "Введите корректную русскую фамилию")
    @NotEmpty(message = "Фамилия не должна быть пуста")
    private String lastName;
    private String nickname;
    private String powerDescription;
}
