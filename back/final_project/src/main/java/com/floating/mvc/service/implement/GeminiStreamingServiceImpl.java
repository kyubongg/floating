//package com.floating.mvc.service.implement;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.stereotype.Service;
//
//import com.floating.mvc.service.GeminiStreamingService;
//
//import lombok.RequiredArgsConstructor;
//import reactor.core.publisher.Flux;
//
//@Service
//public class GeminiStreamingServiceImpl implements GeminiStreamingService {
//	
//	private final ChatClient chatClient;
//	
//	public GeminiStreamingServiceImpl(ChatClient chatClient) {
//        this.chatClient = chatClient;
//    }
//
//	@Override
//	public Flux<String> streamChat(String userMessage) {
//		
//		System.out.println("Gemini 스트리밍 응답 시작:");
//		
//		// prompt 생성 (사용자 메시지 포함)
//		Prompt prompt = new Prompt(userMessage);
//		
//		return chatClient.prompt(prompt)
//				.stream()
//				.content()
//				.doOnComplete(() -> System.out.println("\n\n-------------\n전체 응답 스트림 완료"));
//	}
//	
//	
//}
