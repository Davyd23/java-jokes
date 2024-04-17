package com.jokes.jokes.controller;

import com.jokes.jokes.model.Joke;
import com.jokes.jokes.service.JokeService;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JokeController {
    private final JokeService jokeService;

    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @GetMapping("jokes")
    public List<Joke> getJokes(@RequestParam(defaultValue = "5")
                                 @Min(value = 1, message = "Number of jokes should be greater than 0")
                                 @Min(value = 1, message = "You can get no more than 100 jokes at a time") Integer count) {


        return jokeService.getJokes(count);
    }
}
