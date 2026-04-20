package com.example.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ChatBridgeApp {
    public static void main(String[] args) {
        SpringApplication.run(ChatBridgeApp.class, args);
    }
}

// 1. WebSocket Configuration: This allows the Web UI to connect to Spring Boot
@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The Web UI will connect here
        registry.addEndpoint("/ws-bridge").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}

// 2. The Bridge Controller: It acts as a "Client" to your Raw Server
@Controller
class ChatBridgeController {

    private final SimpMessagingTemplate messagingTemplate;
    private BufferedWriter serverWriter;
    private BufferedReader serverReader;
    private Socket socket;

    public ChatBridgeController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostConstruct
    public void init() {
        // Start the connection to your existing Server in a separate thread
        new Thread(this::connectToRawServer).start();
    }

    private void connectToRawServer() {
        int retryCount = 0;
        while (true) {
            try {
                // Connect to your Server.java logic on port 5000
                socket = new Socket("localhost", 5000);
                serverWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                System.out.println("✅ [Bridge] Connected to your Raw Java Server on port 5000");

                String line;
                // Equivalent to your Message_reciever.java run() logic
                while ((line = serverReader.readLine()) != null) {
                    // Send message from Raw Server -> Web UI
                    messagingTemplate.convertAndSend("/topic/messages", line);
                }
            } catch (IOException e) {
                System.err.println("❌ [Bridge] Raw Server not found. Retrying in 5s... (" + (++retryCount) + ")");
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            }
        }
    }

    // This handles messages coming FROM the Web UI
    @MessageMapping("/chat.send")
    public void handleWebMessage(String message) {
        try {
            if (serverWriter != null) {
                // Forward Web Message -> Raw Server
                // Equivalent to your Message_sender.java run() logic
                serverWriter.write(message);
                serverWriter.newLine();
                serverWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("Error forwarding to Raw Server: " + e.getMessage());
        }
    }
}