package com.accenture.assignment.horsefeeder.Controller;

import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Service.StableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/stable")
public class StableController {

    @Autowired
    private StableService stableService;

    @GetMapping("/")
    public ResponseEntity<List<StableDto>> showStables() {
        Optional<List<StableDto>> response = stableService.showStables();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @GetMapping("/{stableId}")
    public ResponseEntity<StableDto> showStableById(
            @PathVariable("stableId") Long stableId) {
        Optional<StableDto> response = stableService.showStableById(stableId);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @PostMapping(path = "/")
    public @ResponseBody ResponseEntity<StableDto> addNewStable(
            @RequestBody StableDto stableDto) {
        Optional<StableDto> response = stableService.addNewStable(stableDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @DeleteMapping("/{stableId}")
    public @ResponseBody ResponseEntity<StableDto> deleteStableById(
            @PathVariable("stableId") Long stableId) {
        Optional<StableDto> response = stableService.deleteStableById(stableId);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @RequestMapping(value = "/{stableID}", method = PUT)
    public @ResponseBody ResponseEntity<StableDto> updateStableByID(
            @PathVariable("stableID") Long stableId,
            @RequestBody StableDto stableDto) {
        Optional<StableDto> response = stableService.updateStableByID(stableId, stableDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }
}
