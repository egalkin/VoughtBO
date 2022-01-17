package ru.ifmo.egalkin.vought.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectUpdateRequest {
    @NotEmpty(message="Никнейм должен быть задан")
    String nickname;
}
