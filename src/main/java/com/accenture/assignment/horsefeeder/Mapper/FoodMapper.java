package com.accenture.assignment.horsefeeder.Mapper;

import com.accenture.assignment.horsefeeder.DTO.FoodDto;
import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Entities.Food;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    List<FoodDto> foodToFoodDtos(List<Food> foods);

    //@Mapping(target = "ahAdresse", source = "adresse")
    Food foodDtoToFood(FoodDto dto);

    //@Mapping(target = "adresse", source = "ahAdresse")
    FoodDto foodToFoodDto(Food food);


}
