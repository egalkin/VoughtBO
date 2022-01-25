package ru.ifmo.egalkin.vought.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static javax.servlet.RequestDispatcher.ERROR_STATUS_CODE;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        try {
            Object status = request.getAttribute(ERROR_STATUS_CODE);
            if (status != null) {
                HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(status.toString()));
                if (httpStatus == HttpStatus.NOT_FOUND) {
                    model.addAttribute("errorMessage", "Данная страница не найдена");
                } else if (httpStatus == HttpStatus.FORBIDDEN) {
                    model.addAttribute("errorMessage", "У вас нет доступа к данной странице");
                }
            }
        } finally {
            if (model.getAttribute("errorMessage") == null) {
                model.addAttribute("errorMessage", "Произошла неожиданная ошибка");
            }
        }
        return "error";
    }

}
