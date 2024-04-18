package com.jokes.jokes.client;

import com.jokes.jokes.model.Joke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Service
public class JokeApiClient {
    private final Logger log = LoggerFactory.getLogger(JokeApiClient.class);

    private static final String JOKE_URL = "https://official-joke-api.appspot.com/random_joke";
    private final RestTemplate restTemplate;

    public JokeApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<Joke> getRandomJokes(int count) {
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            return IntStream.range(0, count).boxed()
                    .map(i -> executorService.submit(() -> restTemplate.getForObject(JOKE_URL, Joke.class)))
                    .map(future -> {
                        try {
                            return future.get(10, TimeUnit.SECONDS);
                        } catch (InterruptedException | ExecutionException | TimeoutException e) {
                            log.error("There was an issue communicating with external service", e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
        }
    }
}
