package ru.project.watcher.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.project.watcher.service.CurrencyService;


@Configuration
@EnableScheduling
public class ScheduledUpdateCurrency {

    private final CurrencyService currencyService;

    public ScheduledUpdateCurrency(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleFixedRateTask() {
        currencyService.updateCurrenciesInDb(currencyService.getCurrenciesFromApi());
        currencyService.checkChangeCurrencyPrice();
    }
}
