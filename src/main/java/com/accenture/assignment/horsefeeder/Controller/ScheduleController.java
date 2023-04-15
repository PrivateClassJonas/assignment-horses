package com.accenture.assignment.horsefeeder.Controller;

import com.accenture.assignment.horsefeeder.DTO.ScheduleDto;
import com.accenture.assignment.horsefeeder.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
