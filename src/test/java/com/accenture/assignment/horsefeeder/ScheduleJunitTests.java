package com.accenture.assignment.horsefeeder;
import com.accenture.assignment.horsefeeder.DTO.HorseDto;
import com.accenture.assignment.horsefeeder.DTO.ScheduleDto;
import com.accenture.assignment.horsefeeder.DTO.TimeDto;
import com.accenture.assignment.horsefeeder.Entities.Food;
import com.accenture.assignment.horsefeeder.Entities.Horse;
import com.accenture.assignment.horsefeeder.Entities.Schedule;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import com.accenture.assignment.horsefeeder.Mapper.HorseMapper;
import com.accenture.assignment.horsefeeder.Mapper.ScheduleMapper;
import com.accenture.assignment.horsefeeder.Repository.FoodRepository;
import com.accenture.assignment.horsefeeder.Repository.HorseRepository;
import com.accenture.assignment.horsefeeder.Repository.ScheduleRepository;
import com.accenture.assignment.horsefeeder.Repository.StableRepository;
import com.accenture.assignment.horsefeeder.Service.HorseService;
import com.accenture.assignment.horsefeeder.Service.ScheduleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleJunitTests {
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
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @BeforeEach
    void databaseLoads(){
        scheduleRepository.deleteAll();
        foodRepository.deleteAll();
        horseRepository.deleteAll();

        Stable stable = createStable("TestStable1");
        stableRepository.save(stable);
        Horse horse = createHorse("123456789", "Testname1", "Testnickname1", "Testbreed1", "Testowner1",stable);
        horseRepository.save(horse);
        Food food = createFood("TestFood1");
        foodRepository.save(food);
        scheduleRepository.save(createSchedule("12:00","12:30",horse,food));
    }

    @Test
    public void showAllSchedulesTrue()throws Exception{
        MvcResult mvcResult = null;
        mvcResult = mvc.perform(get("/schedule/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<ScheduleDto> actual = new ObjectMapper().readValue(content, new TypeReference<List<ScheduleDto>>(){});

        assertEquals("12:00",actual.get(0).getStart());
        assertEquals("12:30",actual.get(0).getEnd());
        assertEquals("Testname1",actual.get(0).getHorseName());
        assertEquals("TestFood1",actual.get(0).getFoodName());

    }
    @Test
    public void showAllSchedulesFalse()throws Exception{
        scheduleRepository.deleteAll();
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(get("/schedule/"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }
    }

    @Test
    public void showScheduleForFeedingTrue()throws Exception{
        TimeDto time = new TimeDto();
        time.setTime("12:05");
        ScheduleDto scheduleDto = scheduleService.showScheduleForFeeding(time).get();
        assertEquals("12:00",scheduleDto.getStart());
        assertEquals("12:30",scheduleDto.getEnd());

    }
    @Test
    public void showScheduleForFeedingFalse()throws Exception{
        TimeDto time = new TimeDto();
        time.setTime("13:05");
        Optional<ScheduleDto> scheduleDto = scheduleService.showScheduleForFeeding(time);
        assertTrue(scheduleDto.isEmpty());
    }

    @Test
    public void showEligableHorsesTrue()throws Exception{
        TimeDto time = new TimeDto();
        time.setTime("12:00");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(time);
        MvcResult mvcResult = mvc.perform(get("/schedule/eligable/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<HorseDto> actual = new ObjectMapper().readValue(content, new TypeReference<List<HorseDto>>(){});

        assertEquals("Testname1",actual.get(0).getName());
        assertEquals("123456789",actual.get(0).getGuid());
    }
    @Test
    public void showEligableHorsesFalse()throws Exception{
        TimeDto time = new TimeDto();
        time.setTime("15:00");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(time);
        try {
            MvcResult mvcResult = mvc.perform(get("/schedule/eligable/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .characterEncoding("utf-8"))
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }
    }
    @Test
    public void showEligableHorsesNullFalse()throws Exception{
        TimeDto time = new TimeDto();
        time.setTime(null);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(time);
        try {
            MvcResult mvcResult = mvc.perform(get("/schedule/eligable/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .characterEncoding("utf-8"))
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }
    }
    @Test
    public void addNewScheduleTrue()throws Exception{
        Stable stable = createStable("TestStable2");
        stableRepository.save(stable);
        Horse horse = createHorse("1111111", "Testname2", "Testnickname2", "Testbreed2", "Testowner2",stable);
        horseRepository.save(horse);
        Food food = createFood("TestFood2");
        foodRepository.save(food);
        Schedule schedule = new Schedule();
        schedule.setStart("12:30");
        schedule.setEnd("13:00");
        schedule.setHorse(horse);
        schedule.setFood(food);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(scheduleMapper.scheduleToScheduleDto(schedule));
        MvcResult result = mvc.perform(post("/schedule/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andReturn();
        ScheduleDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), ScheduleDto.class);

        assertEquals("12:30",actual.getStart());
        assertEquals("13:00",actual.getEnd());
        assertEquals("1111111",actual.getHorseGuid());
    }

    @Test
    public void addNewScheduleFalse()throws Exception{
        Stable stable = createStable("TestStable2");
        stableRepository.save(stable);
        Horse horse = createHorse("1111111", "Testname2", "Testnickname2", "Testbreed2", "Testowner2",stable);
        horseRepository.save(horse);
        Food food = createFood("TestFood2");
        foodRepository.save(food);
        Schedule schedule = new Schedule();
        schedule.setStart("12:10");
        schedule.setEnd("12:40");
        schedule.setHorse(horse);
        schedule.setFood(food);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(scheduleMapper.scheduleToScheduleDto(schedule));
        try {
            MvcResult result = mvc.perform(post("/schedule/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .characterEncoding("utf-8"))
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }

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
    @AfterEach
    void databaseDrop(){
        scheduleRepository.deleteAll();
        foodRepository.deleteAll();
        horseRepository.deleteAll();
        stableRepository.deleteAll();
    }

}
