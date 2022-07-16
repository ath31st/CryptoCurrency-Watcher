package ru.project.watcher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.project.watcher.entity.Currency;
import ru.project.watcher.repository.CurrencyRepository;

@Configuration
public class CurrencyBeanConfig {

    private final CurrencyRepository currencyRepository;

    public CurrencyBeanConfig(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }
    @Bean
    void setCurrency(){
        Currency currencyBTC = new Currency();
        currencyBTC.setId(90L);
        currencyBTC.setSymbol("BTC");
        currencyBTC.setPrice_usd(0);

        Currency currencyETH = new Currency();
        currencyBTC.setId(80L);
        currencyBTC.setSymbol("ETH");
        currencyBTC.setPrice_usd(0);

        Currency currencySOL = new Currency();
        currencySOL.setId(48543L);
        currencySOL.setSymbol("SOL");
        currencySOL.setPrice_usd(0);

        currencyRepository.save(currencyBTC);
        currencyRepository.save(currencyETH);
        currencyRepository.save(currencySOL);
    }




}
