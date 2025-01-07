package com.LostAndFound.UserService.exceptionHandler;


import com.LostAndFound.UserService.exceptions.UserAccountTemporaryClosedException;
import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.exceptions.ResourceNotFoundException;
import com.LostAndFound.UserService.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiResponse> resourceNotFoundException(ResourceNotFoundException e)
    {
        ApiResponse response= new ApiResponse.Builder().message(e.getMessage())
                .statusCode(HttpStatus.NOT_FOUND)
                .success(false)
                .build();
        return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse> userAlreadyExistsException(UserAlreadyExistsException e) {
        ApiResponse response = new ApiResponse.Builder().message(e.getMessage())
                .statusCode(HttpStatus.NOT_FOUND)
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }




    @ExceptionHandler(UserAccountTemporaryClosedException.class)
    public ResponseEntity<ApiResponse> userAccountTemporaryClosedException(UserAccountTemporaryClosedException e) {
        ApiResponse response = new ApiResponse.Builder().message(e.getMessage())
                .statusCode(HttpStatus.FORBIDDEN)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
