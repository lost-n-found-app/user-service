package com.LostAndFound.UserService.commonClasses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class UserProducerMessage {
    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("products")
    private List<ProductDto> products;

    public UserProducerMessage() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
