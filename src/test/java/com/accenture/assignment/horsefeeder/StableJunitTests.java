package com.accenture.assignment.horsefeeder;

import com.accenture.assignment.horsefeeder.DTO.FoodDto;
import com.accenture.assignment.horsefeeder.DTO.HorseDto;
import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Entities.Food;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import com.accenture.assignment.horsefeeder.Mapper.StableMapper;
import com.accenture.assignment.horsefeeder.Repository.*;
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
public class StableJunitTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private StableRepository stableRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private HorseRepository horseRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private StableService stableService;
    @Autowired
    private StableMapper stableMapper;

    @BeforeEach
    void databaseLoads(){
        scheduleRepository.deleteAll();
        foodRepository.deleteAll();
        horseRepository.deleteAll();
        stableRepository.deleteAll();

        stableRepository.save(createStable("Teststable1"));

    }

    @Test
    public void showStablesTrue()throws Exception{
        Stable saveStable = createStable("ToBeTested1");
        stableRepository.save(saveStable);

        MvcResult mvcResult = null;
        mvcResult = mvc.perform(get("/stable/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<StableDto> actual = new ObjectMapper().readValue(content, new TypeReference<List<StableDto>>(){});

        assertEquals("Teststable1",actual.get(0).getName());
        assertEquals("ToBeTested1",actual.get(1).getName());
    }

    @Test
    public void showStablesFalse()throws Exception{
        stableRepository.deleteAll();
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(get("/stable/"))
                    .andDo(print())
                    .andExpect(status().is(500))
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }
    }

    @Test
    public void addNewStableTrue()throws Exception{
        stableRepository.deleteAll();
        Stable stableToBeAddedFirst = createStable("ThisWasJustAddedFirst");
        StableDto stableToBeAddedSecond = stableMapper.stableToStableDto(createStable("ThisWasJustAddedSecond"));
        stableService.addNewStable(stableMapper.stableToStableDto(stableToBeAddedFirst));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(stableToBeAddedSecond);
        MvcResult result = mvc.perform(post("/stable/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andReturn();
        List<Stable> checkStables = stableRepository.findAll();
        assertEquals("ThisWasJustAddedFirst", checkStables.get(0).getName());
        assertEquals("ThisWasJustAddedSecond", checkStables.get(1).getName());

        StableDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), StableDto.class);
        int status = result.getResponse().getStatus();
        assertEquals("ThisWasJustAddedSecond", actual.getName());
        assertEquals(200, status);
    }
    @Test
    public void findStableByIdTrue()throws Exception{
        stableRepository.deleteAll();
        StableDto stableToBeSaved = stableMapper.stableToStableDto(createStable("StableToBeSaved"));
        stableService.addNewStable(stableToBeSaved);
        List<Stable> checkStables = stableRepository.findAll();
        MvcResult mvcResult = null;
        mvcResult = mvc.perform(get("/stable/"+checkStables.get(0).getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        StableDto actual = new ObjectMapper().readValue(content, StableDto.class);

        assertEquals("StableToBeSaved",actual.getName());
    }
    @Test
    public void findStableByIdFalse()throws Exception{
        stableRepository.deleteAll();
        StableDto stableToBeSaved = stableMapper.stableToStableDto(createStable("StableToBeSaved"));
        stableService.addNewStable(stableToBeSaved);
        List<Stable> checkStables = stableRepository.findAll();
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(get("/stable/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }

    }
    @Test
    public void deleteStableByIdTrue()throws Exception{
        stableRepository.deleteAll();
        StableDto stableToBeDeleted = stableMapper.stableToStableDto(createStable("StableToBeDeleted"));
        stableService.addNewStable(stableToBeDeleted);
        List<Stable> checkStables = stableRepository.findAll();
        MvcResult mvcResult = null;
        mvcResult = mvc.perform(delete("/stable/"+checkStables.get(0).getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        StableDto actual = new ObjectMapper().readValue(content, StableDto.class);

        assertEquals("StableToBeDeleted",actual.getName());
    }
    @Test
    public void deleteStableByIdFalse()throws Exception{
        stableRepository.deleteAll();
        StableDto stableToBeDeleted = stableMapper.stableToStableDto(createStable("StableToBeDeleted"));
        stableService.addNewStable(stableToBeDeleted);
        List<Stable> checkStables = stableRepository.findAll();
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(delete("/stable/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }

    }
    @Test
    public void updateStableByIdTrue()throws Exception{
        stableRepository.deleteAll();
        StableDto stableToBeUpdated = stableMapper.stableToStableDto(createStable("StableToBeUpdated1"));
        stableService.addNewStable(stableToBeUpdated);
        List<Stable> checkStables = stableRepository.findAll();
        assertEquals("StableToBeUpdated1",checkStables.get(0).getName());

        stableToBeUpdated.setName("StableWasUpdated");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(stableToBeUpdated);

        MvcResult result = mvc.perform(put("/stable/"+checkStables.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andReturn();
        StableDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), StableDto.class);
        assertEquals("StableWasUpdated", actual.getName());
    }
    @Test
    public void updateStableByIdFalse()throws Exception{
        stableRepository.deleteAll();
        StableDto stableToBeUpdated = stableMapper.stableToStableDto(createStable("StableToBeUpdated1"));
        stableService.addNewStable(stableToBeUpdated);
        List<Stable> checkStables = stableRepository.findAll();
        assertEquals("StableToBeUpdated1",checkStables.get(0).getName());

        stableToBeUpdated.setName("StableWasUpdated");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(stableToBeUpdated);
        try {
            MvcResult result = mvc.perform(put("/stable/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .characterEncoding("utf-8"))
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }

    }




    @AfterEach
    void databaseDrop(){
        scheduleRepository.deleteAll();
        foodRepository.deleteAll();
        horseRepository.deleteAll();
        stableRepository.deleteAll();
    }

    private Stable createStable(String name){
        Stable stable = new Stable();
        stable.setName(name);
        return stable;
    }



}
