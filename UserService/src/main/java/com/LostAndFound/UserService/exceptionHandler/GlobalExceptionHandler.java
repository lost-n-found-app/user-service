package com.LostAndFound.UserService.exceptionHandler;


import com.LostAndFound.UserService.exceptions.*;
import com.LostAndFound.UserService.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiResponse> resourceNotFoundException(ResourceNotFoundException e) {
        ApiResponse response = new ApiResponse.Builder().message(e.getMessage()).statusCode(HttpStatus.NOT_FOUND).success(false).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse> userAlreadyExistsException(UserAlreadyExistsException e) {
        ApiResponse response = new ApiResponse.Builder().message(e.getMessage()).statusCode(HttpStatus.NOT_FOUND).success(false).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse> roleNotFoundException(RoleNotFoundException e) {
        ApiResponse response = new ApiResponse.Builder().message(e.getMessage()).statusCode(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse> passwordIsIncorrectException(PasswordMismatchException e) {
        ApiResponse response = new ApiResponse.Builder().message(e.getMessage()).statusCode(HttpStatus.CONFLICT).build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
