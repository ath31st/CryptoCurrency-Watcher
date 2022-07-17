package ru.project.watcher.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.project.watcher.entity.Currency;
import ru.project.watcher.entity.User;
import ru.project.watcher.repository.CurrencyRepository;
import ru.project.watcher.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    public UserService(UserRepository userRepository, CurrencyRepository currencyRepository) {
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
    }

    public ResponseEntity<User> saveUser(User user) {
        double defaultRate = 0;
        if (userRepository.findUserByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            User tmpUser = userRepository.findUserByUsernameIgnoreCase(user.getUsername()).get();

            checkExistingUser(user, tmpUser);
            checkExistingSymbol(user.getSymbol());

            tmpUser.setSymbol(user.getSymbol());
            tmpUser.setCurrency(currencyRepository.findBySymbolIgnoreCase(tmpUser.getSymbol()).get());
            tmpUser.setExchangeRate(defaultRate);
            userRepository.save(tmpUser);
        } else {
            checkExistingSymbol(user.getSymbol());

            user.setCurrency(currencyRepository.findBySymbolIgnoreCase(user.getSymbol()).get());
            user.setExchangeRate(defaultRate);
            userRepository.save(user);
        }
        return ResponseEntity.ok(user);
    }

    private void checkExistingSymbol(String symbol) {
        List<Currency> currencies = currencyRepository.findAll();
        if (currencies.stream().noneMatch(currency -> currency.getSymbol().equals(symbol))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this symbol not found!");
        }
    }

    private void checkExistingUser(User user, User tmpUser) {
        if (tmpUser.equals(user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "this user already have this symbol!");
        }
    }
}
