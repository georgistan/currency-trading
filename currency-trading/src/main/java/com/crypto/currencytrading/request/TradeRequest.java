package com.crypto.currencytrading.request;

import lombok.Data;

@Data
public class TradeRequest {
    private String symbol;
    private String name;
    private double price;
    private double quantity;
}