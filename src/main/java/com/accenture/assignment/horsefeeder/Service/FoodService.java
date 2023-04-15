package com.accenture.assignment.horsefeeder.Service;

import com.accenture.assignment.horsefeeder.DTO.FoodDto;
import com.accenture.assignment.horsefeeder.Entities.Food;
import com.accenture.assignment.horsefeeder.Mapper.FoodMapper;
import com.accenture.assignment.horsefeeder.Repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {
    @Autowired
    private FoodMapper foodMapper;
    private FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public Optional<List<FoodDto>> showFoods() {
        List<Food> foodList = foodRepository.findAll();
        if (foodList.isEmpty()) {
            return Optional.empty();
        }
        List<FoodDto> foodDtoList = foodMapper.foodToFoodDtos(foodList);
        if (foodDtoList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(foodDtoList);
    }

    public Optional<FoodDto> showFoodById(Long foodId) {
        if (foodId == null) {
            return Optional.empty();
        }
        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if (optionalFood.isEmpty()) {
            return Optional.empty();
        }
        Food food = optionalFood.get();
        FoodDto foodDto = foodMapper.foodToFoodDto(food);
        return Optional.ofNullable(foodDto);
    }

    public Optional<FoodDto> addNewFood(FoodDto foodDto) {
        if (foodDto == null) {
            return Optional.empty();
        }
        Food food = foodMapper.foodDtoToFood(foodDto);
        Food savedFood = foodRepository.save(food);
        if (savedFood == null) {
            return Optional.empty();
        }
        FoodDto result = foodMapper.foodToFoodDto(savedFood);
        return Optional.ofNullable(result);
    }

    public Optional<FoodDto> deleteFoodById(Long foodId) {
        if (foodId == null) {
            return Optional.empty();
        }
        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if (optionalFood.isEmpty()) {
            return Optional.empty();
        }
        Food deleteFood = optionalFood.get();
        foodRepository.delete(deleteFood);
        FoodDto result = foodMapper.foodToFoodDto(deleteFood);
        return Optional.ofNullable(result);
    }

    public Optional<FoodDto> updateFoodByID(Long foodId, FoodDto foodDto) {
        if (foodId == null || foodDto == null) {
            return Optional.empty();
        }
        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if (optionalFood.isEmpty()) {
            return Optional.empty();
        }
        Food updateFood = optionalFood.get();
        updateFood.setName(foodDto.getName());
        foodRepository.save(updateFood);
        return Optional.ofNullable(foodMapper.foodToFoodDto(updateFood));
    }

}
