package com.fourstars.FourStars.controller.chatbot;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.fourstars.FourStars.domain.request.chatbot.ChatRequestDTO;
import com.fourstars.FourStars.domain.response.chatbot.ChatResponseDTO;
import com.fourstars.FourStars.service.ChatbotService;

@Controller
public class ChatbotSocketController {

    private final ChatbotService chatbotService;
    private final SimpMessageSendingOperations messagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ChatbotSocketController.class);

    @Autowired
    public ChatbotSocketController(ChatbotService chatbotService, SimpMessageSendingOperations messagingTemplate) {
        this.chatbotService = chatbotService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage") 
    public void handleChatMessage(@Payload ChatRequestDTO chatRequest, Principal principal) {
        String userEmail = principal.getName();
        
        try {
            ChatResponseDTO response = chatbotService.getChatbotResponse(chatRequest);
            messagingTemplate.convertAndSendToUser(userEmail, "/queue/chat.reply", response);

        } catch (Exception e) {
            logger.error("!!! LỖI KHI CHẠY CHATBOT SERVICE: {}", e.getMessage(), e);
            ChatResponseDTO errorResponse = new ChatResponseDTO();
            errorResponse.setAssistantResponse("Rất tiếc, đã có lỗi máy chủ: " + e.getMessage());
            errorResponse.setConversationId(chatRequest.getConversationId()); 

            messagingTemplate.convertAndSendToUser(userEmail, "/queue/chat.reply", errorResponse);
        }
    }
}