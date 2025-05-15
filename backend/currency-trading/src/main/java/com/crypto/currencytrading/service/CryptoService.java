package com.crypto.currencytrading.service;

import com.crypto.currencytrading.request.TradeRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CryptoService {

    private final JdbcTemplate jdbc;
    private final KrakenTickerService krakenTickerService;

    public CryptoService(JdbcTemplate jdbc, KrakenTickerService krakenTickerService) {
        this.jdbc = jdbc;
        this.krakenTickerService = krakenTickerService;
    }

    public double getBalance() {
        return jdbc.queryForObject("SELECT balance FROM users WHERE username = 'testuser'", Double.class);
    }

    public String buy(TradeRequest req) {
        double price = req.getPrice();
        double qty = req.getQuantity();
        double total = price * qty;

        double balance = getBalance();
        if (total > balance) {
            return "Insufficient balance.";
        }

        jdbc.update("UPDATE users SET balance = balance - ? WHERE username = 'testuser'", total);
        jdbc.update("INSERT INTO transactions (user_id, crypto_symbol, crypto_name, quantity, price, total, type) " +
                "VALUES (1, ?, ?, ?, ?, ?, 'BUY')", req.getSymbol(), req.getName(), qty, price, total);
        jdbc.update("INSERT INTO holdings (user_id, crypto_symbol, quantity) " +
                "VALUES (1, ?, ?) ON CONFLICT (user_id, crypto_symbol) DO UPDATE SET quantity = holdings.quantity + ?",
                req.getSymbol(), qty, qty);

        return "Purchase successful.";
    }

    public String sell(TradeRequest req) {
        double price = req.getPrice();
        double qty = req.getQuantity();
        double total = price * qty;

        Double heldQty = jdbc.queryForObject("SELECT quantity FROM holdings WHERE user_id = 1 AND crypto_symbol = ?",
                new Object[]{req.getSymbol()}, Double.class);

        if (heldQty == null || heldQty < qty) {
            return "Insufficient crypto holdings.";
        }

        jdbc.update("UPDATE users SET balance WHERE username = 'testuser'", total);
        jdbc.update("INSERT INTO transactions (user_id, crypto_symbol, crypto_name, quantity, price, total, type) " +
                "VALUES (1, ?, ?, ?, ?, ?, 'SELL')", req.getSymbol(), req.getName(), qty, price, total);
        jdbc.update("UPDATE holdings SET quantity = quantity - ? WHERE user_id = 1 AND crypto_symbol = ?",
            qty,
            req.getSymbol()
        );

        return "Sell successful.";
    }

    public List<Map<String, Object>> getTransactions() {
        return jdbc.queryForList("SELECT * FROM transactions WHERE user_id = 1 ORDER BY timestamp DESC");
    }

    public void reset() {
        jdbc.update("UPDATE users SET balance = 10000 WHERE username = 'testuser'");
        jdbc.update("DELETE FROM transactions WHERE user_id = 1");
        jdbc.update("DELETE FROM holdings WHERE user_id = 1");
    }

    public Map<String, Double> getInitialPrices() {
        return krakenTickerService.getTickerCache();
    }
}