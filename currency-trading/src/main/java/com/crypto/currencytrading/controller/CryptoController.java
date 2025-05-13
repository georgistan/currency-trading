package com.crypto.currencytrading.controller;

import com.crypto.currencytrading.request.TradeRequest;
import com.crypto.currencytrading.service.CryptoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CryptoController {

    private final CryptoService service;

    public CryptoController(CryptoService service) {
        this.service = service;
    }

    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance() {
        return ResponseEntity.ok(service.getBalance());
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buy(@RequestBody TradeRequest req) {
        String result = service.buy(req);
        if (result.contains("Insufficient")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sell(@RequestBody TradeRequest req) {
        String result = service.sell(req);
        if (result.contains("Insufficient")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/transactions")
    public List<Map<String, Object>> getTransactions() {
        return service.getTransactions();
    }

    @PostMapping("/reset")
    public ResponseEntity<String> reset() {
        service.reset();
        return ResponseEntity.ok("Reset successful.");
    }

}