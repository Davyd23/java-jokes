package com.jokes.jokes.service;

import com.jokes.jokes.client.JokeApiClient;
import com.jokes.jokes.model.Joke;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JokeService {

    private final JokeApiClient jokeApiClient;

    public JokeService(JokeApiClient jokeApiClient) {
        this.jokeApiClient = jokeApiClient;
    }

    public List<Joke> getJokes(int count) {
        return List.of(jokeApiClient.getRandomJoke().block());
    }
}
