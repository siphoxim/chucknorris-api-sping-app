package com.chucknorrisapi.controller;

import com.chucknorrisapi.model.Joke;
import com.chucknorrisapi.service.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/")
public class JokeController {

    @Autowired
    private JokeService jokeService;

    @GetMapping
    public Joke getFirstJoke() {
        return jokeService.getFirstJoke();
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return jokeService.getAllCategories();
    }

    @GetMapping("/search")
    public @ResponseBody List<Joke> getJokes(@RequestParam String query) throws IOException {
        return jokeService.searchJokes(query);
    }

}
