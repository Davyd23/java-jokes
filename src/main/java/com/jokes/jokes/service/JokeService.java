package com.jokes.jokes.service;

import com.jokes.jokes.client.JokeApiClient;
import com.jokes.jokes.dto.JokeDTO;
import com.jokes.jokes.model.Joke;
import com.jokes.jokes.repository.JokeRepository;
import com.mongodb.MongoTimeoutException;
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
    private final JokeRepository jokeRepository;

    public JokeService(JokeApiClient jokeApiClient,
                       JokeRepository jokeRepository) {
        this.jokeApiClient = jokeApiClient;
        this.jokeRepository = jokeRepository;
    }

    public List<JokeDTO> getJokes(int count) {
        long startTime = System.currentTimeMillis();
        List<JokeDTO> jokeDTOList = new ArrayList<>(count);
        for (int i = 0; i < count; i += BATCH_SIZE) {
            int batchSize = Math.min(BATCH_SIZE, count - i);
            jokeDTOList.addAll(jokeApiClient.getRandomJokes(batchSize));
        }

        try {
            jokeRepository.saveAll(jokeDTOList.stream().map(this::convertDto).toList());
        } catch (MongoTimeoutException ex) {
            log.error("Could not store data into the db", ex);
        }

        log.debug("Calling external service {} times, got {} success responses, took {} seconds",
                count, jokeDTOList.size(), TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime));
        return jokeDTOList;
    }

    private Joke convertDto(JokeDTO jokeDTO) {
        return new Joke(String.valueOf(jokeDTO.hashCode()), jokeDTO.type(), jokeDTO.setup(), jokeDTO.punchline());
    }

}
