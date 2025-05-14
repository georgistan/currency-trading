package com.crypto.currencytrading.service;

import com.crypto.currencytrading.client.KrakenWebSocketClient;
import com.crypto.currencytrading.domain.TickerDataDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KrakenTickerService {

    private static final String KRAKEN_URI = "wss://ws.kraken.com/v2";
    private final SimpMessagingTemplate messagingTemplate;
    private KrakenWebSocketClient client;
    private FrontendWebSocketService frontendWebSocketService;

    private final Map<String, Double> tickerCache = new ConcurrentHashMap<>();

    @Autowired
    public KrakenTickerService(
        SimpMessagingTemplate messagingTemplate,
        FrontendWebSocketService frontendWebSocketService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.frontendWebSocketService = frontendWebSocketService;
    }

    @PostConstruct
    public void init() {
        try {
            client = new KrakenWebSocketClient(new URI(KRAKEN_URI)) {
                @Override
                public void onMessage(String message) {
                    super.onMessage(message);
                    JsonNode extracted = extractTickerPrice(message);

                    if (extracted != null) {
                        System.out.println("Sending ticker data: " + extracted);

                        String symbol = extracted.path("symbol").asText(null);
                        double lastPrice = extracted.path("last").asDouble(Double.NaN);
                        TickerDataDTO tickerDataDTO = new TickerDataDTO();
                        tickerDataDTO.setSymbol(symbol);
                        tickerDataDTO.setPrice(lastPrice);

                        frontendWebSocketService.broadcastCryptoPrice(tickerDataDTO);

                        tickerCache.put(symbol, lastPrice);
                        System.out.println("Updated price for " + symbol + ": " + lastPrice);
                    }
                }
            };

            client.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode extractTickerPrice(String message) {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(message);

            if (jsonNode.isArray() && jsonNode.size() > 1 &&
                "ticker".equals(jsonNode.get(jsonNode.size() - 1).asText())
            ) {
                return jsonNode;
            }
        } catch (Exception ignored) {
            throw new RuntimeException(ignored);
        }

        return null;
    }

    public Map<String, Double> getCachedTickerData() {
        return tickerCache;
    }
}