//package com.floating.mvc.controller;
//
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.floating.mvc.service.GeminiStreamingService;
//
//import lombok.RequiredArgsConstructor;
//import reactor.core.publisher.Flux;
//
//@RestController
//public class StreamingController {
//	
//	private final GeminiStreamingService streamingService;
//	
//	public StreamingController(GeminiStreamingService streamingService) {
//        this.streamingService = streamingService;
//    }
//	
//	@GetMapping(value = "/stream/hello", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//	public Flux<String> streamHello(){
//		String message = "안녕? 오늘 기분이 어때?";
//		
//		System.out.println("요청 메세지: " + message);
//		
//		return streamingService.streamChat(message);
//	}
//}
