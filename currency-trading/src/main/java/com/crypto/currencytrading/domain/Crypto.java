package com.crypto.currencytrading.domain;

public record Crypto(
    String symbol,
    String name,
    double quantity,
    double price
) {
}
