package com.chucknorrisapi.exception;

public class JokeException extends RuntimeException{

    public JokeException(String message) {
        super(message);
    }

    public JokeException(String message, Throwable cause) {
        super(message, cause);}
}
