import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import type { Currency } from "./types/currency";
import CurrencyTable from "./components/CurrencyTable";
import SockJS from 'sockjs-client/dist/sockjs';

const WebSocketComponent = () => {
  const [tickers, setTickers] = useState<Currency[]>([]);

  useEffect(() => {
    const fetchInitialPrices = async () => {
      try {
        const response = await fetch("/api/initialPrices")

        if (!response.ok) {
            throw new Error("Failed to fetch initial prices")
        }
        
        const data = await response.json()
        setTickers(data)
      } catch (error) {
        console.error("Error fetching initial prices:", error)
      }
    }

    fetchInitialPrices()

    const socket = new SockJS("http://localhost:8080/ws");
    const stompClient = new Client({
      webSocketFactory: () => socket,
      onConnect: () => stompClient.subscribe("/topic/ticker", (msg) => {
            const data = JSON.parse(msg.body);
            console.log("Received data:", data); 
        
            setTickers((prev) => {
              const updated = [...prev.filter((t) => t.symbol !== data.symbol), data];
              return updated.sort((a, b) => a.symbol.localeCompare(b.symbol));
            });
        }),
      onWebSocketError: (err) => {
        console.error("WebSocket error:", err);
      },
    });
  
    stompClient.activate();
  
    return () => {
      stompClient.deactivate();
    };
  }, []);  

  return (
    <div>
      <CurrencyTable initialCurrencies={tickers}/>
    </div>
  );
};

export default WebSocketComponent;