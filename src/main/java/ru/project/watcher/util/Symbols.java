package ru.project.watcher.util;

public enum Symbols {
    BTC(90L),
    ETH(80L),
    SOL(48543L);

    private final Long id;

    Symbols(Long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }
}
