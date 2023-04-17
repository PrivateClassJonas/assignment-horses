package com.accenture.assignment.horsefeeder;
import com.accenture.assignment.horsefeeder.DTO.FoodDto;
import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Entities.Food;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import com.accenture.assignment.horsefeeder.Mapper.FoodMapper;
import com.accenture.assignment.horsefeeder.Mapper.StableMapper;
import com.accenture.assignment.horsefeeder.Repository.FoodRepository;
import com.accenture.assignment.horsefeeder.Repository.StableRepository;
import com.accenture.assignment.horsefeeder.Service.FoodService;
import com.accenture.assignment.horsefeeder.Service.StableService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class FoodJunitTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private FoodService foodService;
    @Autowired
    private FoodMapper foodMapper;

    @BeforeEach
    void databaseLoads(){
        foodRepository.deleteAll();
        foodRepository.save(createFood("TestFood1"));
    }
    @Test
    public void showFoodsTrue()throws Exception{
        Food saveFood = createFood("TestFood2");
        foodRepository.save(saveFood);

        MvcResult mvcResult = null;
        mvcResult = mvc.perform(get("/food/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<FoodDto> actual = new ObjectMapper().readValue(content, new TypeReference<List<FoodDto>>(){});

        assertEquals("TestFood1",actual.get(0).getName());
        assertEquals("TestFood2",actual.get(1).getName());
    }

    @Test
    public void showFoodsFalse()throws Exception{
        foodRepository.deleteAll();
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(get("/food/"))
                    .andDo(print())
                    .andExpect(status().is(500))
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }
    }

    @Test
    public void addNewFoodTrue()throws Exception{
        foodRepository.deleteAll();
        FoodDto foodToBeSavedFirst = foodMapper.foodToFoodDto(createFood("FoodToBeSaved1"));
        FoodDto foodToBeSavedSecond = foodMapper.foodToFoodDto(createFood("FoodToBeSaved2"));
        foodService.addNewFood(foodToBeSavedFirst);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(foodToBeSavedSecond);
        MvcResult result = mvc.perform(post("/food/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andReturn();
        List<Food> checkFoods = foodRepository.findAll();
        assertEquals("FoodToBeSaved1", checkFoods.get(0).getName());
        assertEquals("FoodToBeSaved2", checkFoods.get(1).getName());

        FoodDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), FoodDto.class);
        int status = result.getResponse().getStatus();
        assertEquals("FoodToBeSaved2", actual.getName());
        assertEquals(200, status);
    }

    @Test
    public void findFoodByIdTrue()throws Exception{
        foodRepository.deleteAll();
        FoodDto foodToBeSaved = foodMapper.foodToFoodDto(createFood("FoodToBeSaved"));
        foodService.addNewFood(foodToBeSaved);
        List<Food> checkFoods = foodRepository.findAll();
        MvcResult mvcResult = null;
        mvcResult = mvc.perform(get("/food/"+checkFoods.get(0).getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        FoodDto actual = new ObjectMapper().readValue(content, FoodDto.class);

        assertEquals("FoodToBeSaved",actual.getName());
    }
    @Test
    public void findFoodByIdFalse()throws Exception{
        foodRepository.deleteAll();
        FoodDto foodToBeSaved = foodMapper.foodToFoodDto(createFood("FoodToBeSaved"));
        foodService.addNewFood(foodToBeSaved);
        List<Food> checkFoods = foodRepository.findAll();
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(get("/food/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }
    }

    @Test
    public void deleteFoodByIdTrue()throws Exception{
        foodRepository.deleteAll();
        FoodDto foodToBeDeleted = foodMapper.foodToFoodDto(createFood("FoodToBeDeleted"));
        foodService.addNewFood(foodToBeDeleted);
        List<Food> checkFoods = foodRepository.findAll();
        MvcResult mvcResult = null;
        mvcResult = mvc.perform(delete("/food/"+checkFoods.get(0).getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        FoodDto actual = new ObjectMapper().readValue(content, FoodDto.class);

        assertEquals("FoodToBeDeleted",actual.getName());
    }

    @Test
    public void updateFoodByIdTrue()throws Exception{
        foodRepository.deleteAll();
        FoodDto foodToBeUpdated = foodMapper.foodToFoodDto(createFood("FoodToBeUpdated1"));
        foodService.addNewFood(foodToBeUpdated);
        List<Food> checkFoods = foodRepository.findAll();
        assertEquals("FoodToBeUpdated1",checkFoods.get(0).getName());

        foodToBeUpdated.setName("FoodWasUpdated");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(foodToBeUpdated);

        MvcResult result = mvc.perform(put("/food/"+checkFoods.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andReturn();
        FoodDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), FoodDto.class);
        assertEquals("FoodWasUpdated", actual.getName());
    }

    @AfterEach
    void databaseDrop() {
        foodRepository.deleteAll();
    }



    private Food createFood(String name){
        Food food = new Food();
        food.setName(name);
        return food;
    }

}
