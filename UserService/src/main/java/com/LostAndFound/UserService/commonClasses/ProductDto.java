package com.LostAndFound.UserService.commonClasses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ProductDto {
    @NotBlank(message = "Product Name is mandatory")
    private String name;
    private String description;
    private String category;
    private String status;
    @Pattern(
            regexp = "^(.*),\\s*(\\w+),\\s*(\\w+),\\s*\\d{6}$",
            message = "Location must be in the format: 'Street Address, City, State, PIN_CODE'"
    )
    private String location;

    public ProductDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
