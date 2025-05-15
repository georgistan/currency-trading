package com.crypto.currencytrading.service;

import com.crypto.currencytrading.client.KrakenWebSocketClient;
import com.crypto.currencytrading.domain.TickerDataDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KrakenTickerService {

    private static final String KRAKEN_URI = "wss://ws.kraken.com/v2";

    private KrakenWebSocketClient client;
    private FrontendWebSocketService frontendWebSocketService;
    private ObjectMapper objectMapper;

    private final Map<String, Double> tickerCache = new ConcurrentHashMap<>();

    @Autowired
    public KrakenTickerService(
        FrontendWebSocketService frontendWebSocketService,
        ObjectMapper objectMapper
    ) {
        this.frontendWebSocketService = frontendWebSocketService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        try {
            client = new KrakenWebSocketClient(new URI(KRAKEN_URI)) {
                @Override
                public void onMessage(String message) {
                    JsonNode extracted = null;

                    try {
                        extracted = objectMapper.readTree(message);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    extracted = extracted.path("data");

                    if (!extracted.isEmpty() && extracted.isArray()) {
                        extracted = extracted.get(0);

                        TickerDataDTO mappedData = mapData(extracted);

                        broadcastAndSaveData(mappedData);
                    }
                }
            };

            client.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Double> getTickerCache() {
        return Collections.unmodifiableMap(tickerCache);
    }

    private TickerDataDTO mapData(JsonNode extracted) {
        TickerDataDTO tickerDataDTO = new TickerDataDTO();
        tickerDataDTO.setSymbol(extracted.path("symbol").asText(null));
        tickerDataDTO.setPrice(extracted.path("last").asDouble(Double.NaN));

        return tickerDataDTO;
    }

    private void broadcastAndSaveData(TickerDataDTO tickerDataDTO) {
        frontendWebSocketService.broadcastCryptoPrice(tickerDataDTO);
        tickerCache.put(tickerDataDTO.getSymbol(), tickerDataDTO.getPrice());
    }
}