package com.LostAndFound.UserService.exceptions;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException() {
    }

    public PasswordMismatchException(String message) {
        super(message);
    }
}
