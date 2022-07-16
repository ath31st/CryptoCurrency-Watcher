package ru.project.watcher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.watcher.entity.User;
import ru.project.watcher.service.CurrencyService;
import ru.project.watcher.service.UserService;

import javax.validation.Valid;

@RestController
public class RegisterController {
    private final CurrencyService currencyService;
    private final UserService userService;

    public RegisterController(CurrencyService currencyService, UserService userService) {
        this.currencyService = currencyService;
        this.userService = userService;
    }

    @PostMapping("/notify")
    public ResponseEntity<User> notify(@RequestBody @Valid User user) {
        return userService.saveUser(user);
    }

}
