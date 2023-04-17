package com.accenture.assignment.horsefeeder.Service;

import com.accenture.assignment.horsefeeder.DTO.FoodDto;
import com.accenture.assignment.horsefeeder.Entities.Food;
import com.accenture.assignment.horsefeeder.Mapper.FoodMapper;
import com.accenture.assignment.horsefeeder.Repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Die Geschäftslogik für alle Food Abfragen
 */
@Service
public class FoodService {
    @Autowired
    private FoodMapper foodMapper;
    private FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    /**
     * findet mit Hilfe der FoodRepository Klasse alle Food Objekte
     * @return Optional<List<FoodDto>>
     *     Optional von einer Liste von allen Food Objekten --> Optional ist leer, wenn nichts gefunden wird
     */
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

    /**
     * findet mithilfe einer ID und dem FoodRepository ein Food Objekt
     * @param foodId
     *     Die ID, von dem gewünschten Food Objekt
     * @return Optional<FoodDto>
     *     Optional von einem Food Objekt --> Optional ist leer, wenn nichts gefunden wird
     */
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

    /**
     * fügt ein neues Food Objekt hinzu
     * @param foodDto
     *     Das neue Food Objekt, dass hinzugefügt werden soll
     * @return Optional<FoodDto>
     *     Optional von dem neuen Food Objekt --> Optional ist leer wenn das Objekt nicht hinzugefügt werden konnte
     */
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

    /**
     * löscht ein Food Objekt mit einer bestimmten ID
     * @param foodId
     *     Die ID von dem Objekt, dass gelöscht werden soll
     * @return Optional<FoodDto>
     *     Optional von einem Food Objekt, dass gelöscht werden soll --> Optional ist leer wenn kein Objekt gelöscht werden konnte/nichts gefunden wurde zum löschen
     */
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

    /**
     * ändert ein bestimmtes Food Objekt
     * @param foodId
     *     Die ID von dem Food Objekt, dass geändert werden soll
     * @param foodDto
     *      Das neue Food Objekt
     * @return Optional<FoodDto>
     *     Optional von dem neuen Food Objekt --> Optional ist leer wenn das Objekt nicht geändert werden konnte/ nicht gefunden wurde
     */
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
