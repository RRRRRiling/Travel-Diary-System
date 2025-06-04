// FoodController.java
package org.example.traveldiary.controller;

import org.example.traveldiary.model.Food;
import org.example.traveldiary.service.FoodService;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {
    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public List<Food> getAllFoods() throws IOException {
        return foodService.getAllFoods();
    }

    @GetMapping("/search")
    public List<Food> searchFoods(@RequestParam String keyword) throws IOException {
        return foodService.searchFoods(keyword);
    }

    @GetMapping("/calories")
    public List<Food> searchByCalories(@RequestParam int max) throws IOException {
        return foodService.searchByCalories(max);
    }
}
