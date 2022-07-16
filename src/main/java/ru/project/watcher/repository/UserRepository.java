package ru.project.watcher.repository;

import org.springframework.data.repository.CrudRepository;
import ru.project.watcher.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,String> {
    Optional<User> findUserByUsernameIgnoreCase(String username);
}
