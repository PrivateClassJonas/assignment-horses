package com.accenture.assignment.horsefeeder.Controller;

import com.accenture.assignment.horsefeeder.DTO.HorseDto;
import com.accenture.assignment.horsefeeder.DTO.ScheduleDto;
import com.accenture.assignment.horsefeeder.DTO.TimeDto;
import com.accenture.assignment.horsefeeder.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/")
    public ResponseEntity<List<ScheduleDto>> showHorses() {
        Optional<List<ScheduleDto>> response = scheduleService.showSchedules();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @PostMapping(path = "/")
    public @ResponseBody ResponseEntity<ScheduleDto> addNewSchedule(
            @RequestBody ScheduleDto scheduleDto) throws ParseException {
        Optional<ScheduleDto> response = scheduleService.addNewSchedule(scheduleDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }
    @GetMapping(path="/eligable/")
    public @ResponseBody ResponseEntity<List<HorseDto>> showEligableHorses(
            @RequestBody TimeDto time) throws ParseException {
        Optional<List<HorseDto>> response = scheduleService.showEligableHorses(time);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

}
