package ru.project.watcher.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.project.watcher.entity.Currency;
import ru.project.watcher.entity.User;
import ru.project.watcher.repository.CurrencyRepository;
import ru.project.watcher.repository.UserRepository;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;

    public CurrencyService(CurrencyRepository currencyRepository, UserRepository userRepository) {
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Currency> getCurrency(String username) {
        if (userRepository.findUserByUsernameIgnoreCase(username).isPresent()) {
            User user = userRepository.findUserByUsernameIgnoreCase(username).get();
            if (currencyRepository.findBySymbolIgnoreCase(user.getSymbol()).isPresent()) {
                Currency currency = currencyRepository.findBySymbolIgnoreCase(user.getSymbol()).get();
                return ResponseEntity.ok(currency);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not found or currency is not updated");
    }
}
