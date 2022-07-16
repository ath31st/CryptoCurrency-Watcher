package ru.project.watcher.entity;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class User {
    @Id
    @Column(name = "username", nullable = false)
    private String username;
    private String symbol;

}
