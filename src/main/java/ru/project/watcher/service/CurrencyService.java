package ru.project.watcher.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.project.watcher.entity.Currency;
import ru.project.watcher.entity.User;
import ru.project.watcher.repository.CurrencyRepository;
import ru.project.watcher.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    final String url = "https://api.coinlore.net/api/ticker/?id=90,80,48543";

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

    public List<Currency> getCurrenciesFromApi() {
        Currency[] currencies = null;
        final RestTemplate restTemplate = new RestTemplate();
        try {
            currencies = restTemplate.getForObject(url, Currency[].class);
        } catch (HttpServerErrorException e) {
            e.printStackTrace();
        }
        if (currencies != null) {
            return Arrays.stream(currencies).toList();
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "problem with coinlore.com");
        }
    }

    public void updateCurrenciesInDb(List<Currency> currencies) {
        List<Currency> tmpCurrencies = new ArrayList<>();
        currencyRepository.findAll().iterator().forEachRemaining(tmpCurrencies::add);
        for (Currency currency : currencies) {
            if (!tmpCurrencies.contains(currency)) {
                currencyRepository.save(currency);
                System.out.println(currency + " updated!");
            }
        }
    }

}
