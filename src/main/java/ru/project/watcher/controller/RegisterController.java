package ru.project.watcher.controller;

import org.springframework.web.bind.annotation.*;
import ru.project.watcher.entity.User;
import ru.project.watcher.service.CurrencyService;

@RestController
public class RegisterController {
    private final CurrencyService currencyService;

    public RegisterController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/notify")
    public void notify(@RequestBody User user) {

    }

}
