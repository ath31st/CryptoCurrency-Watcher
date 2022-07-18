package ru.project.watcher.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
//@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Currency implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String symbol;
    @JsonProperty("price_usd")
    private double priceUsd;

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", priceUsd=" + priceUsd +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Double.compare(currency.priceUsd, priceUsd) == 0 && Objects.equals(id, currency.id) && Objects.equals(symbol, currency.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, symbol, priceUsd);
    }
}
