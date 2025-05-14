package com.crypto.currencytrading.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class KrakenWebSocketClient extends WebSocketClient {

    public KrakenWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
        System.out.println("Connected to Kraken WebSocket");

        List<String> pairs = List.of(
            "XBT/USD", "ETH/USD", "USDT/USD", "BNB/USD", "XRP/USD",
            "ADA/USD", "DOGE/USD", "SOL/USD", "DOT/USD", "LTC/USD",
            "LINK/USD", "BCH/USD", "XLM/USD", "UNI/USD", "ATOM/USD",
            "TRX/USD", "ETC/USD", "XMR/USD", "EOS/USD", "FIL/USD"
        );

        String symbolsJsonArray = pairs.stream()
            .map(s -> "\"" + s + "\"")
            .collect(Collectors.joining(", ", "[", "]"));

        String subscribeMessage = String.format("""
            {
              "method": "subscribe",
              "params": {
                "channel": "ticker",
                "symbol": %s
              }
            }
            """, symbolsJsonArray);

        send(subscribeMessage);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Message received: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }
}