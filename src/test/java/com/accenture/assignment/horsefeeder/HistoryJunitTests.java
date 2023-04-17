package com.accenture.assignment.horsefeeder;

import com.accenture.assignment.horsefeeder.DTO.*;
import com.accenture.assignment.horsefeeder.Entities.*;
import com.accenture.assignment.horsefeeder.Mapper.HistoryMapper;
import com.accenture.assignment.horsefeeder.Mapper.ScheduleMapper;
import com.accenture.assignment.horsefeeder.Repository.*;
import com.accenture.assignment.horsefeeder.Service.HistoryService;
import com.accenture.assignment.horsefeeder.Service.ScheduleService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class HistoryJunitTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private HorseRepository horseRepository;
    @Autowired
    private StableRepository stableRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private HistoryMapper historyMapper;


    @BeforeEach
    void databaseLoads(){
        scheduleRepository.deleteAll();
        historyRepository.deleteAll();
        horseRepository.deleteAll();
        stableRepository.deleteAll();
        foodRepository.deleteAll();
        Stable stable = createStable("TestStable1");
        stableRepository.save(stable);
        Horse horse = createHorse("123456789", "Testname1", "Testnickname1", "Testbreed1", "Testowner1",stable);
        horseRepository.save(horse);
        Food food = createFood("TestFood1");
        foodRepository.save(food);
        scheduleRepository.save(createSchedule("12:00","12:30",horse,food));
        historyRepository.save(createHistory("12:00",4,"done",horse,food));
    }

    @Test
    public void showAllHistoriesTrue()throws Exception{
        MvcResult mvcResult = null;
        mvcResult = mvc.perform(get("/history/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<HistoryDto> actual = new ObjectMapper().readValue(content, new TypeReference<List<HistoryDto>>(){});

        assertEquals("12:00",actual.get(0).getTime());
        assertEquals("done",actual.get(0).getStatus());
        assertEquals("Testname1",actual.get(0).getHorseName());

    }
    @Test
    public void showAllHistoriesFalse()throws Exception{
        historyRepository.deleteAll();
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(get("/history/"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }
    }

    @Test
    public void setMissedFeedingGetMissedFeedingTrue() throws Exception {
        historyRepository.deleteAll();
        List<Horse> horse = horseRepository.findAll();
        TimeDto time = new TimeDto();
        time.setTime("12:00");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(time);
        MvcResult result = mvc.perform(post("/history/missed/"+ horse.get(0).getGuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        HistoryDto actual = new ObjectMapper().readValue(content, HistoryDto.class);
        assertEquals("123456789",actual.getHorseGuid());
        assertEquals("12:00",actual.getTime());
        assertEquals("missed",actual.getStatus());

        MvcResult mvcResult = null;
        mvcResult = mvc.perform(get("/history/missed"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content2 = mvcResult.getResponse().getContentAsString();

        List<MissedFeedingDto> missedFeedings = new ObjectMapper().readValue(content2, new TypeReference<List<MissedFeedingDto>>(){});
        assertEquals(1, missedFeedings.get(0).getAmountMissed());
        assertEquals("123456789", missedFeedings.get(0).getHorseGuid());
    }


    @Test
    public void getMissedAmountTrue()throws Exception{
        historyRepository.deleteAll();
        List<Horse> horse = horseRepository.findAll();
        TimeDto time = new TimeDto();
        time.setTime("12:00");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(time);
        mvc.perform(post("/history/missed/"+ horse.get(0).getGuid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"));
        MvcResult mvcResult = null;
        mvcResult = mvc.perform(get("/history/amount"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content2 = mvcResult.getResponse().getContentAsString();
        List<MissedAmountDto> missedAmounts = new ObjectMapper().readValue(content2, new TypeReference<List<MissedAmountDto>>(){});
        assertEquals("123456789", missedAmounts.get(0).getGuid());
    }

    @Test
    public void releaseFoodTrue()throws Exception{
        scheduleRepository.deleteAll();
        historyRepository.deleteAll();
        horseRepository.deleteAll();
        stableRepository.deleteAll();
        foodRepository.deleteAll();
        Stable stable = createStable("TestStable1");
        stableRepository.save(stable);
        Horse horse = createHorse("123456789", "Testname1", "Testnickname1", "Testbreed1", "Testowner1",stable);
        horseRepository.save(horse);
        Food food = createFood("TestFood1");
        foodRepository.save(food);
        scheduleRepository.save(createSchedule("12:00","12:30",horse,food));
        TimeDto time = new TimeDto();
        time.setTime("12:00");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(time);
        MvcResult result = mvc.perform(post("/history/123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        HistoryDto actual = new ObjectMapper().readValue(content, HistoryDto.class);
        assertEquals("done",actual.getStatus());
        assertEquals("123456789",actual.getHorseGuid());
        assertEquals("12:00",actual.getTime());

    }
    private Horse createHorse(String guid, String name, String nickname, String breed, String owner, Stable stable){
        Horse horse = new Horse();
        horse.setGuid(guid);
        horse.setBreed(breed);
        horse.setName(name);
        horse.setNickname(nickname);
        horse.setOwner(owner);
        horse.setStable(stable);
        return horse;
    }
    private Food createFood(String name){
        Food food = new Food();
        food.setName(name);
        return food;
    }
    private Schedule createSchedule(String start, String end, Horse horse, Food food){
        Schedule schedule = new Schedule();
        schedule.setFood(food);
        schedule.setHorse(horse);
        schedule.setEnd(end);
        schedule.setStart(start);
        return schedule;
    }
    private Stable createStable(String name){
        Stable stable = new Stable();
        stable.setName(name);
        return stable;
    }

    private History createHistory(String time, int amount, String status, Horse horse, Food food){
        History history = new History();
        history.setTime(time);
        history.setStatus(status);
        history.setAmount(amount);
        history.setHorse(horse);
        history.setFood(food);
        return history;
    }
    @AfterEach
    void databaseDrop(){
        historyRepository.deleteAll();
        scheduleRepository.deleteAll();
        foodRepository.deleteAll();
        horseRepository.deleteAll();
        stableRepository.deleteAll();
    }
}
