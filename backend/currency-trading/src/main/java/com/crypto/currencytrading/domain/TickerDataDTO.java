package com.crypto.currencytrading.domain;

import lombok.Data;

@Data
public class TickerDataDTO {
    private String symbol;
    private Double price;
}