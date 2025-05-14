package com.crypto.currencytrading.service;

import com.crypto.currencytrading.domain.TickerDataDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FrontendWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public FrontendWebSocketService(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    public void broadcastCryptoPrice(TickerDataDTO data) {
        try {
            String message = objectMapper.writeValueAsString(data);
            messagingTemplate.convertAndSend("/topic/prices", message);
        } catch (Exception e) {
            log.error("Error broadcasting message to WebSocket clients", e);
        }
    }
}