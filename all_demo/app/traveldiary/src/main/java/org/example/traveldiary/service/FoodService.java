// FoodService.java
package org.example.traveldiary.service;

import org.example.traveldiary.model.Food;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {
    private final ObjectMapper objectMapper;

    @Value("${app.data.foods-file}")
    private String foodsFile;

    public FoodService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Food> getAllFoods() throws IOException {
        return objectMapper.readValue(
                new File(foodsFile),
                new TypeReference<List<Food>>() {}
        );
    }

    public List<Food> searchFoods(String keyword) throws IOException {
        String lowerKeyword = keyword.toLowerCase();
        return getAllFoods().stream()
                .filter(food -> food.getName().toLowerCase().contains(lowerKeyword) ||
                        food.getOrigin().toLowerCase().contains(lowerKeyword) ||
                        food.getType().toLowerCase().contains(lowerKeyword) ||
                        food.getIngredients().stream()
                                .anyMatch(ing -> ing.toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }

    public List<Food> searchByCalories(int maxCalories) throws IOException {
        return getAllFoods().stream()
                .filter(food -> food.getCalories() <= maxCalories)
                .collect(Collectors.toList());
    }
}