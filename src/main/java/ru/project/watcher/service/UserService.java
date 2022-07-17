package ru.project.watcher.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.project.watcher.entity.User;
import ru.project.watcher.repository.CurrencyRepository;
import ru.project.watcher.repository.UserRepository;
import ru.project.watcher.util.Symbols;

import java.util.Arrays;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    public UserService(UserRepository userRepository, CurrencyRepository currencyRepository) {
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
    }

    public ResponseEntity<User> saveUser(User user) {
        if (userRepository.findUserByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            User tmpUser = userRepository.findUserByUsernameIgnoreCase(user.getUsername()).get();

            checkExistingUser(user, tmpUser);
            checkExistingSymbol(user.getSymbol());

            tmpUser.setSymbol(user.getSymbol());
            tmpUser.setCurrency(currencyRepository.findBySymbolIgnoreCase(tmpUser.getSymbol()).get());
            userRepository.save(tmpUser);
        } else {
            checkExistingSymbol(user.getSymbol());

            user.setCurrency(currencyRepository.findBySymbolIgnoreCase(user.getSymbol()).get());
            userRepository.save(user);
        }
        return ResponseEntity.ok(user);
    }

    private void checkExistingSymbol(String symbol) {
        if (Arrays.stream(Symbols.values()).noneMatch(symbols -> symbols.name().equals(symbol))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this symbol not found!");
        }
    }

    private void checkExistingUser(User user, User tmpUser) {
        if (tmpUser.equals(user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "this user already have this symbol!");
        }
    }
}
