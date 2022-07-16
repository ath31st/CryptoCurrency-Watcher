package ru.project.watcher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.project.watcher.entity.Currency;
import ru.project.watcher.service.CurrencyService;

@RestController
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/notify/{username}")
    public ResponseEntity<Currency> getCurrency(@PathVariable String username) {
       return currencyService.getCurrency(username);
    }

}
