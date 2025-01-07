package com.LostAndFound.UserService.exceptions;

public class UserAccountTemporaryClosedException extends RuntimeException{
    public UserAccountTemporaryClosedException()
    {
        super();
    }

    public UserAccountTemporaryClosedException(String msg)
    {
        super(msg);
    }
}
