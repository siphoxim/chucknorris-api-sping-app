package com.chucknorrisapi.service;

import com.chucknorrisapi.exception.JokeException;
import com.chucknorrisapi.model.Joke;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class JokeService implements Serializable {

    @Autowired
    private final RestTemplate template = new RestTemplate();
    private final String baseUrl = "https://api.chucknorris.io/jokes";

    public Joke getFirstJoke() {
        return template.getForObject("https://api.chucknorris.io/jokes/random?category/0", Joke.class);
    }

    public List<String>  getAllCategories() {
        return template.getForObject(baseUrl+"/categories", List.class);
    }

    public List<Joke> searchJokes(String query) {
        try {
            StringBuilder url = new StringBuilder(baseUrl + "/search");
            url.append("?query=").append(urlEncode(query));
            HttpURLConnection conn = (HttpURLConnection) new URL(url.toString()).openConnection();
            int respCode = conn.getResponseCode();
            if (respCode == HttpURLConnection.HTTP_OK) {
                List<Joke> jokes = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(new JSONTokener(conn.getInputStream()));
                if (jsonObject.getInt("total") > 0) {
                    JSONArray jsonResult = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonResult.length(); i++) {
                        JSONObject jsonJoke = jsonResult.getJSONObject(i);
                        jokes.add(parseJoke(jsonJoke));
                    }
                }
                System.out.println(jokes);
                return jokes;
            } else {
                throw new JokeException("");
            }
        } catch (IOException e) {
            throw new JokeException("Error searching jokes", e);
        }
    }
    private Joke parseJoke(JSONObject jsonObject) {
        Joke joke = new Joke();
        joke.setUrl(jsonObject.optString("url"));
        joke.setIconUrl(jsonObject.optString("icon_url"));
        joke.setId(jsonObject.optString("id"));
        joke.setValue(jsonObject.optString("value"));
        joke.setCreatedAt(jsonObject.optString("created_at"));
        joke.setUpdatedAt(jsonObject.optString("updated_at"));
        JSONArray jsonCategories = jsonObject.optJSONArray("category");
        if (jsonCategories != null) {
            for (int i = 0; i < jsonCategories.length(); i++) {
                joke.addCategory(jsonCategories.getString(i));
            }
        }
        return joke;
    }
    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new JokeException("Unable to url encode string: " + s, e);
        }
    }

}
