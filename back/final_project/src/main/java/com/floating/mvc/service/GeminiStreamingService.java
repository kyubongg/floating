package com.floating.mvc.service;

import reactor.core.publisher.Flux;

public interface GeminiStreamingService {
	Flux<String> streamChat(String userMessage);
}
