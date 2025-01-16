package com.LostAndFound.UserService.dto;

import com.LostAndFound.UserService.commonClasses.ProductDto;
import jakarta.validation.Valid;

import java.util.List;

public class UserProductDto {
    @Valid
    private UserDto user;
    @Valid
    private List<ProductDto> products;

    public UserProductDto() {
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
