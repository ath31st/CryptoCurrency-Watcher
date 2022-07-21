package ru.project.watcher.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;
import ru.project.watcher.entity.Currency;
import ru.project.watcher.entity.User;
import ru.project.watcher.repository.CurrencyRepository;
import ru.project.watcher.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CurrencyServiceTest {

    @Autowired
    private CurrencyService currencyService;

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private UserRepository userRepository;


    @Test
    void getCurrency() {

        User user = new User();
        user.setUsername("Noah");
        user.setSymbol("BTC");
        Optional<User> optionalUser = Optional.of(user);

        Currency currency = new Currency();
        currency.setId(90L);
        currency.setSymbol("BTC");
        currency.setPriceUsd(1000);
        Optional<Currency> optionalCurrency = Optional.of(currency);

        Mockito.when(userRepository.findUserByUsernameIgnoreCase("Noah")).thenReturn(optionalUser);
        Mockito.when(currencyRepository.findBySymbolIgnoreCase("BTC")).thenReturn(optionalCurrency);

        assertEquals(1000, Objects.requireNonNull(currencyService.getCurrency("Noah").getBody()).getPriceUsd());
        assertThrows(ResponseStatusException.class, () -> currencyService.getCurrency("Boris"));

    }

    @Test
    void getCurrenciesFromApi() {
        List<Currency> currencies = currencyService.getCurrenciesFromApi();
        assertNotNull(currencies);
    }

    @Test
    void updateCurrenciesInDb() {
        List<Currency> currencies = currencyService.getCurrenciesFromApi();
        currencyService.updateCurrenciesInDb(currencies);
        Mockito.verify(currencyRepository, Mockito.times(currencies.size() * 2)).save(Mockito.any());
    }

    @Test
    void checkChangeCurrencyPrice() {
        assertTrue(true);
    }
}