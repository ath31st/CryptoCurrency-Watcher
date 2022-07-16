package ru.project.watcher.entity;

import lombok.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Currency {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String symbol;
    private double price_usd;
}
