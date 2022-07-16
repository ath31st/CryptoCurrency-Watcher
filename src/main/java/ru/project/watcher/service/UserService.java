package ru.project.watcher.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.project.watcher.entity.User;
import ru.project.watcher.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<User> saveUser(User user) {
        if (userRepository.findUserByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            User tmpUser = userRepository.findUserByUsernameIgnoreCase(user.getUsername()).get();
            tmpUser.setSymbol(user.getSymbol());
            userRepository.save(tmpUser);
        } else {
            userRepository.save(user);
        }
        return ResponseEntity.ok(user);
    }
}
