package com.accenture.assignment.horsefeeder.Controller;

import com.accenture.assignment.horsefeeder.DTO.HistoryDto;
import com.accenture.assignment.horsefeeder.DTO.HorseDto;
import com.accenture.assignment.horsefeeder.DTO.MissedFeedingDto;
import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Service.HistoryService;
import com.accenture.assignment.horsefeeder.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @PostMapping(path = "/{horseGuid}")
    public @ResponseBody ResponseEntity<HistoryDto> releaseFood(
            @PathVariable("horseGuid") String horseGuid) throws ParseException {
        Optional<HistoryDto> response = historyService.releaseFood(horseGuid);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }
    @GetMapping("/")
    public ResponseEntity<List<HistoryDto>> showHistories() {
        Optional<List<HistoryDto>> response = historyService.showHistories();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }
    @GetMapping("/")
    public ResponseEntity<List<MissedFeedingDto>> showMissed() {
        Optional<List<MissedFeedingDto>> response = historyService.showMissed();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }




}
