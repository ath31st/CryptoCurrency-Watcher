package ru.project.watcher.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
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
    private final Logger logger = LoggerFactory.getLogger(CurrencyService.class);
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    @Value("${currencies.id}")
    private String currenciesId;
    private final String URL = "https://api.coinlore.net/api/ticker/?id=";

    public CurrencyService(CurrencyRepository currencyRepository, UserRepository userRepository) {
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Currency> getCurrency(String username) {
        if (userRepository.findUserByUsernameIgnoreCase(username).isPresent()) {
            User user = userRepository.findUserByUsernameIgnoreCase(username).get();
            if (currencyRepository.findBySymbolIgnoreCase(user.getSymbol()).isPresent()) {
                return ResponseEntity.ok(currencyRepository.findBySymbolIgnoreCase(user.getSymbol()).get());
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not found or currency is not updated");
    }

    public List<Currency> getCurrenciesFromApi() {
        Currency[] currencies = null;
        final RestTemplate restTemplate = new RestTemplate();
        try {
            currencies = restTemplate.getForObject(URL + currenciesId, Currency[].class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        if (currencies != null) {
            return Arrays.stream(currencies).toList();
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "problem with coinlore.com");
        }
    }

    public void updateCurrenciesInDb(List<Currency> currencies) {
        List<Currency> tmpCurrencies = currencyRepository.findAll();
        for (Currency currency : currencies) {
            if (!tmpCurrencies.contains(currency)) {
                currencyRepository.save(currency);
                logger.atInfo().addArgument(currency).log("{} updated!");
            }
        }
    }

    public void checkChangeCurrencyPrice() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(users::add);
        List<Currency> currencies = getCurrenciesAccordingUsers(users);
        for (User user : users) {
            Currency currentCurrency = currencies.stream()
                    .filter(currency -> user.getSymbol().equals(currency.getSymbol()))
                    .findFirst()
                    .get();
            double result = moreThen01percent(user.getCurrency(), currentCurrency);
            printResult(result, user, currentCurrency);
            updateExchangeRateForUser(user, result);
        }
    }

    private List<Currency> getCurrenciesAccordingUsers(List<User> users) {
        List<Currency> currencies = new ArrayList<>();
        currencyRepository.findAll().iterator().forEachRemaining(currency -> {
            if (users.stream().anyMatch(user -> user.getSymbol().equals(currency.getSymbol()))) {
                currencies.add(currency);
            }
        });
        return currencies;
    }

    private double moreThen01percent(Currency userCurrency, Currency currentCurrency) {
        return ((userCurrency.getPriceUsd() / currentCurrency.getPriceUsd()) - 1) * 100;
    }

    private void printResult(double result, User user, Currency currency) {
        if (result >= 0.1 & user.getExchangeRate() != result) {
            logger.atWarn()
                    .addArgument(user.getUsername())
                    .addArgument(currency.getSymbol())
                    .addArgument(String.format("%.2f", Math.abs(result)))
                    .log("Result for {}. {} exchange rate has changed by -{}%");
            // System.out.printf("Result for %s. %s exchange rate has changed by -%.2f%%\n", user.getUsername(), currency.getSymbol(), Math.abs(result));
        } else if (result <= 0.1 & user.getExchangeRate() != result) {
            logger.atWarn()
                    .addArgument(user.getUsername())
                    .addArgument(currency.getSymbol())
                    .addArgument(String.format("%.2f", Math.abs(result)))
                    .log("Result for {}. {} exchange rate has changed by +{}%");
            //  System.out.printf("Result for %s. %s exchange rate has changed by +%.2f%%\n", user.getUsername(), currency.getSymbol(), Math.abs(result));
        } else {
            // System.out.printf("Result for %s. %s exchange rate has not changed\n", user.getUsername(), currency.getSymbol());
        }
    }

    private void updateExchangeRateForUser(User user, double result) {
        user.setExchangeRate(result);
        userRepository.save(user);
    }

}
