package com.crypto.currencytrading.controller;

import com.crypto.currencytrading.service.KrakenTickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Controller
public class TickerController {

    private final KrakenTickerService tickerService;

    @Autowired
    public TickerController(KrakenTickerService tickerService) {
        this.tickerService = tickerService;
    }

    @SubscribeMapping("/ticker")
    @SendTo("/topic/ticker")
    public Collection<Double> sendInitialTickerData() {
        return tickerService.getCachedTickerData().values();
    }

    @GetMapping("/api/ticker")
    @ResponseBody
    public Collection<Double> getTickerDataRest() {
        return tickerService.getCachedTickerData().values();
    }
}