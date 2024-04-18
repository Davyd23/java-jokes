package com.jokes.jokes.service;

import com.jokes.jokes.client.JokeApiClient;
import com.jokes.jokes.model.Joke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class JokeService {

    private static final int BATCH_SIZE = 10;
    private final Logger log = LoggerFactory.getLogger(JokeService.class);
    private final JokeApiClient jokeApiClient;

    public JokeService(JokeApiClient jokeApiClient) {
        this.jokeApiClient = jokeApiClient;
    }

    public List<Joke> getJokes(int count) {
        long startTime = System.currentTimeMillis();
        List<Joke> jokeList = new ArrayList<>(count);
        for (int i = 0; i < count; i += BATCH_SIZE) {
            int batchSize = Math.min(BATCH_SIZE, count - i);
            jokeList.addAll(jokeApiClient.getRandomJokes(batchSize));
        }
        log.debug("Calling external service {} times, got {} success responses, took {} seconds",
                count, jokeList.size(), TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime));
        return jokeList;
    }

}
