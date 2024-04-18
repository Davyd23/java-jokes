package com.jokes.jokes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("jokes")
public record Joke(@Id String id, String type, String setup, String punchline) {
}
