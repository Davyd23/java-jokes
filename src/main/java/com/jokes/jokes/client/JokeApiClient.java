package com.jokes.jokes.client;

import com.jokes.jokes.model.Joke;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class JokeApiClient {
    private final WebClient webClient;

    public JokeApiClient() {
        this.webClient = WebClient.create("https://official-joke-api.appspot.com");
    }

    public Mono<Joke> getRandomJoke() {
        return webClient.get()
                .uri("/random_joke")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Joke.class);
    }
}
