package com.jokes.jokes.repository;

import com.jokes.jokes.model.Joke;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JokeRepository extends MongoRepository<Joke, String> {
}
