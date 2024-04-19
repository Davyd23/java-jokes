package com.jokes.jokes.controller;

import com.jokes.jokes.client.JokeApiClient;
import com.jokes.jokes.dto.JokeDTO;
import com.jokes.jokes.repository.JokeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class JokeControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private JokeApiClient jokeApiClient;
    @MockBean
    private JokeRepository jokeRepository;

    @BeforeEach
    public void beforeEach() {
        Mockito.when(jokeApiClient.getRandomJokes(anyInt()))
                .thenAnswer(count ->
                        IntStream.range(0, count.getArgument(0))
                                .boxed()
                                .map(i -> new JokeDTO("i", "i", "i"))
                                .toList()
                );
    }


    @Test
    void givenGetJokesIsCalled_whenCountIsLessThan1_thenReturnStatus400() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/jokes?count=0", String.class);
        assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode(), "Expected status does not match");
    }

    @Test
    void givenGetJokesIsCalled_whenCountIsBiggerThan100_thenReturnStatus400() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/jokes?count=101", String.class);
        assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode(), "Expected status does not match");
    }

    @Test
    void givenGetJokesIsCalled_whenARequestIsMadeWithoutCount_thenReturn5Jokes() {
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("/jokes", List.class);
        assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode(), "Expected status does not match");
        assertEquals(5, responseEntity.getBody().size(), "Number of elements do not match");
        Mockito.verify(jokeApiClient).getRandomJokes(5);
    }

    @Test
    void givenGetJokesIsCalled_whenARequestIsMadeWitCount45_thenReturn45JokesAndGetMethodIsCalled5Times() {
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("/jokes?count=45", List.class);
        assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode(), "Expected status does not match");
        assertEquals(45, responseEntity.getBody().size(), "Number of elements do not match");
        Mockito.verify(jokeApiClient, times(4)).getRandomJokes(10);
        Mockito.verify(jokeApiClient).getRandomJokes(5);

        Mockito.verify(jokeRepository).saveAll(any());
    }
}
