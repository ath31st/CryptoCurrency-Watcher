package ru.project.watcher.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    @JsonIgnore
    private Long id;
    @NotBlank
    @NotNull
    private String username;
    @NotNull
    private String symbol;
    @JsonIgnore
    private double exchangeRate;

    private Currency currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(symbol, user.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, symbol);
    }
}
