package com.LostAndFound.UserService.commonClasses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {
    @NotBlank(message = "Product Name is mandatory")
    private String name;
    private String description;
    private String category;
    private String status;
    @Pattern(
            regexp = "^[a-zA-Z0-9\\s]+,\\s[a-zA-Z\\s]+,\\s[a-zA-Z\\s]+,\\s\\d{6}$",
            message = "Location must be in the format: 'Street Address, City, State, PIN_CODE'"
    )
    private String location;

    public ProductDto(String product2, String s, String string, String s1) {
    }

    public ProductDto(String name, String description, String category, String status, String location) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.status = status;
        this.location = location;
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
