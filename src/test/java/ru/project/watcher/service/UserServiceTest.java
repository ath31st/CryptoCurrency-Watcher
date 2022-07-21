package ru.project.watcher.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.project.watcher.entity.User;
import ru.project.watcher.repository.CurrencyRepository;
import ru.project.watcher.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void saveUser() {
        User tmpUser = new User();
        tmpUser.setUsername("Pavel");
        tmpUser.setSymbol("BTC");

        assertNotNull(userService.saveUser(tmpUser).getBody());
        User user = userService.saveUser(tmpUser).getBody();
        assertEquals("BTC", user.getSymbol());
        assertEquals("Pavel", user.getUsername());
        assertEquals(0, user.getExchangeRate());
        assertNotNull(user.getCurrency());
    }

    @Test
    public void saveUserFail() {
        User tmpUser = new User();
        tmpUser.setUsername("Pavel");
        tmpUser.setSymbol("BTC");

        Mockito.doReturn(Optional.of(new User()))
                .when(userRepository)
                .findUserByUsernameIgnoreCase("Pavel");

        assertNotNull(userService.saveUser(tmpUser).getBody());
        User user = userService.saveUser(tmpUser).getBody();
        assertEquals("BTC", user.getSymbol());
        assertEquals("Pavel", user.getUsername());
        assertEquals(0, user.getExchangeRate());
    }
}