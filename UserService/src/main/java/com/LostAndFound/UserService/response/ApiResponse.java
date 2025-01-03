package com.LostAndFound.UserService.response;

import org.springframework.http.HttpStatus;

public class ApiResponse {

    private String message;
    private HttpStatus statusCode;
    private boolean success;

    private ApiResponse(Builder builder) {
        this.message = builder.message;
        this.statusCode = builder.statusCode;
        this.success = builder.success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class Builder {
        private String message;
        private HttpStatus statusCode;
        private boolean success;

        public Builder() {
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder statusCode(HttpStatus statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public ApiResponse build() {
            return new ApiResponse(this);
        }
    }
}
