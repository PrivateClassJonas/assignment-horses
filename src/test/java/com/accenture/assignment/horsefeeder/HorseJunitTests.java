package com.accenture.assignment.horsefeeder;
import com.accenture.assignment.horsefeeder.DTO.HorseDto;
import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Entities.Horse;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import com.accenture.assignment.horsefeeder.Mapper.HorseMapper;
import com.accenture.assignment.horsefeeder.Repository.HorseRepository;
import com.accenture.assignment.horsefeeder.Repository.StableRepository;
import com.accenture.assignment.horsefeeder.Service.HorseService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class HorseJunitTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private HorseRepository horseRepository;
    @Autowired
    private StableRepository stableRepository;
    @Autowired
    private HorseService horseService;
    @Autowired
    private HorseMapper horseMapper;

    @BeforeEach
    void databaseLoads(){
        horseRepository.deleteAll();
        Stable stable = new Stable();
        stable.setName("TestStable1");
        stableRepository.save(stable);
        horseRepository.save(createHorse("123456789", "Testname1", "Testnickname1", "Testbreed1", "Testowner1",stable));
    }

    @Test
    public void showHorsesTrue()throws Exception{
        MvcResult mvcResult = null;
        mvcResult = mvc.perform(get("/horse/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<HorseDto> actual = new ObjectMapper().readValue(content, new TypeReference<List<HorseDto>>(){});

        assertEquals("123456789",actual.get(0).getGuid());
        assertEquals("Testbreed1",actual.get(0).getBreed());
        assertEquals("Testname1",actual.get(0).getName());
        assertEquals("Testnickname1",actual.get(0).getNickname());
        assertEquals("Testowner1",actual.get(0).getOwner());
    }

    @Test
    public void deleteHorseTrue()throws Exception{
        MvcResult mvcResult = null;
        mvcResult = mvc.perform(delete("/horse/123456789"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        HorseDto actual = new ObjectMapper().readValue(content, HorseDto.class);
        assertEquals("123456789", actual.getGuid());
    }
    @Test
    public void deleteHorseByIdFalse()throws Exception{
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(delete("/horse/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }
    }


    @Test
    public void showHorseByIdTrue()throws Exception{
        MvcResult mvcResult = null;
        mvcResult = mvc.perform(get("/horse/123456789"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        HorseDto actual = new ObjectMapper().readValue(content, HorseDto.class);
        assertEquals("123456789", actual.getGuid());
    }

    @Test
    public void showHorseByIdFalse()throws Exception{
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(get("/horse/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
        }catch(Exception e){
            assertEquals("Request processing failed: java.util.NoSuchElementException: No value present",e.getLocalizedMessage());
        }
    }

    @Test
    public void createHorsesTrue()throws Exception{
        List<Stable> stables = stableRepository.findAll();
        Horse horse = createHorse("12345678", "Testname2", "Testnickname2", "Testbreed2", "Testowner2",stables.get(0));
        HorseDto horseToBeSaved = horseMapper.horseToHorseDto(horse);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(horseToBeSaved);
        MvcResult result = mvc.perform(post("/horse/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andReturn();


        HorseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), HorseDto.class);
        int status = result.getResponse().getStatus();
        assertEquals("Testname2", actual.getName());
        assertEquals("Testnickname2", actual.getNickname());
        assertEquals("Testowner2", actual.getOwner());
        assertEquals(200, status);
    }

    @Test
    public void updateHorseTrue()throws Exception{
        List<Stable> stables = stableRepository.findAll();
        Horse horse = createHorse("123456789812736473829182736473928172", "Testname2", "Testnickname2", "Testbreed2", "Testowner2",stables.get(0));
        HorseDto horseToBeSaved = horseMapper.horseToHorseDto(horse);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(horseToBeSaved);

        MvcResult result = mvc.perform(put("/horse/123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andReturn();
        HorseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), HorseDto.class);
        assertEquals("123456789812736473829182736473928172", actual.getGuid());


    }
    @Test
    public void updateHorseFalse()throws Exception{
        List<Stable> stables = stableRepository.findAll();
        Horse horse = createHorse("12345678981", "Testname2", "Testnickname2", "Testbreed2", "Testowner2",stables.get(0));
        HorseDto horseToBeSaved = horseMapper.horseToHorseDto(horse);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(horseToBeSaved);
        try {

            MvcResult result = mvc.perform(put("/horse/123456789")
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
    private Stable createStable(String name){
        Stable stable = new Stable();
        stable.setName(name);
        return stable;
    }

    @AfterEach
    void dropDatabase(){
        horseRepository.deleteAll();
        stableRepository.deleteAll();
    }

}
