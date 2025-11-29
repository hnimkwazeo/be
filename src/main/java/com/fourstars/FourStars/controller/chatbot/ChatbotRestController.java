package com.fourstars.FourStars.controller.chatbot;

import com.fourstars.FourStars.domain.DictationSentence;
import com.fourstars.FourStars.domain.request.chatbot.ChatRequestDTO;
import com.fourstars.FourStars.domain.request.chatbot.ExplainRequestDTO;
import com.fourstars.FourStars.domain.response.chatbot.ChatResponseDTO;
import com.fourstars.FourStars.repository.DictationSentenceRepository;
import com.fourstars.FourStars.service.ChatbotService;
import com.fourstars.FourStars.util.annotation.ApiMessage;
import com.fourstars.FourStars.util.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor 
public class ChatbotRestController {

    private final ChatbotService chatbotService;
    private final DictationSentenceRepository dictationSentenceRepository;
    @PostMapping("/explain-dictation")
    @ApiMessage("Explain dictation sentence")
    public ResponseEntity<ChatResponseDTO> explainDictation(@RequestBody ExplainRequestDTO request) {
        DictationSentence sentence = dictationSentenceRepository.findById(request.getSentenceId())
                .orElseThrow(() -> new ResourceNotFoundException("Sentence not found"));

        String prompt = buildExplanationPrompt(sentence.getCorrectText(), request.getUserText());
        ChatRequestDTO chatRequest = new ChatRequestDTO();
        chatRequest.setMessage(prompt);
        ChatResponseDTO response = chatbotService.getChatbotResponse(chatRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/explain")
    @ApiMessage("Generate AI explanation for quiz/grammar")
    public ResponseEntity<Map<String, String>> requestExplanation(@RequestBody ExplainRequestDTO request) {
        String explanation = chatbotService.requestExplanation(request);
        return ResponseEntity.ok(Map.of("reply", explanation));
    }

    private String buildExplanationPrompt(String correctText, String userText) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tôi đang luyện nghe chép chính tả câu này:\n");
        sb.append("\"").append(correctText).append("\"\n\n");
        
        if (userText != null && !userText.isBlank()) {
            sb.append("Tôi đã nghe và viết lại là: \"").append(userText).append("\"\n");
            sb.append("Hãy giải thích chi tiết ngữ pháp, từ vựng của câu đúng và chỉ ra lỗi sai của tôi (nếu có).");
        } else {
            sb.append("Hãy giải thích chi tiết cấu trúc ngữ pháp và từ vựng quan trọng trong câu này giúp tôi.");
        }
        return sb.toString();
    }
}