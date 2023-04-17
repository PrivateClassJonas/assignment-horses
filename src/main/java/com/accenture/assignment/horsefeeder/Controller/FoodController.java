package com.accenture.assignment.horsefeeder.Controller;

import com.accenture.assignment.horsefeeder.DTO.FoodDto;
import com.accenture.assignment.horsefeeder.Service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/food")
public class FoodController {
    @Autowired
    private FoodService foodService;

    @GetMapping("/")
    public ResponseEntity<List<FoodDto>> showFoods() {
        Optional<List<FoodDto>> response = foodService.showFoods();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @GetMapping("/{foodId}")
    public ResponseEntity<FoodDto> showFoodById(
            @PathVariable("foodId") Long foodId) {
        Optional<FoodDto> response = foodService.showFoodById(foodId);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @PostMapping(path = "/")
    public @ResponseBody ResponseEntity<FoodDto> addNewFood(
            @RequestBody FoodDto foodDto) {
        Optional<FoodDto> response = foodService.addNewFood(foodDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @DeleteMapping("/{foodId}")
    public @ResponseBody ResponseEntity<FoodDto> deleteFoodById(
            @PathVariable("foodId") Long foodId) {
        Optional<FoodDto> response = foodService.deleteFoodById(foodId);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @RequestMapping(value = "/{foodID}", method = PUT)
    public @ResponseBody ResponseEntity<FoodDto> updateFoodByID(
            @PathVariable("foodID") Long foodId,
            @RequestBody FoodDto foodDto) {
        Optional<FoodDto> response = foodService.updateFoodByID(foodId, foodDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }
}
