package ru.project.watcher.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.project.watcher.entity.Currency;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    Optional<Currency> findById(Long id);
    Optional<Currency> findBySymbolIgnoreCase(String symbol);
    List<Currency> findAll();
}
