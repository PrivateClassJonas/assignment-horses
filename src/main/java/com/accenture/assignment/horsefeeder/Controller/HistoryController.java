package com.accenture.assignment.horsefeeder.Controller;

import com.accenture.assignment.horsefeeder.DTO.*;
import com.accenture.assignment.horsefeeder.Service.HistoryService;
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
            @PathVariable("horseGuid") String horseGuid,
            @RequestBody TimeDto time) throws ParseException {
        Optional<HistoryDto> response = historyService.releaseFood(horseGuid, time);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @PostMapping(path = "/missed/{horseGuid}")
    public @ResponseBody ResponseEntity<HistoryDto> setMissedFeeding(
            @PathVariable("horseGuid") String horseGuid,
            @RequestBody TimeDto time) throws ParseException {
        Optional<HistoryDto> response = historyService.setMissedFeeding(horseGuid, time);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @GetMapping("/")
    public ResponseEntity<List<HistoryDto>> showHistories() {
        Optional<List<HistoryDto>> response = historyService.showHistories();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @GetMapping("/eligable")
    public ResponseEntity<List<MissedEligibleDto>> showMissedEligable(
            @RequestBody TimeDto time) throws ParseException {
        Optional<List<MissedEligibleDto>> response = historyService.showMissedEligible(time);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @GetMapping("/missed")
    public ResponseEntity<List<MissedFeedingDto>> showMissed() {
        Optional<List<MissedFeedingDto>> response = historyService.showMissed();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @GetMapping("/amount")
    public ResponseEntity<List<MissedAmountDto>> showMissedAmount() {
        Optional<List<MissedAmountDto>> response = historyService.showMissedAmount();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

}
