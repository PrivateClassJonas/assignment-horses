package com.accenture.assignment.horsefeeder.Controller;

import com.accenture.assignment.horsefeeder.DTO.HorseDto;
import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Service.HorseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/horse")
public class HorseController {
    @Autowired
    private HorseService horseService;
    @GetMapping("/")
    public ResponseEntity<List<HorseDto>> showHorses() {
        Optional<List<HorseDto>> response = horseService.showHorses();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @GetMapping("/{horseId}")
    public ResponseEntity<HorseDto> showHorseById(
            @PathVariable("horseId") String horseId) {
        Optional<HorseDto> response = horseService.showHorseById(horseId);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @PostMapping(path = "/")
    public @ResponseBody ResponseEntity<HorseDto> addNewHorse(
           @RequestBody HorseDto horseDto) {
        Optional<HorseDto> response = horseService.addNewHorse(horseDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @DeleteMapping(path = "/{horseId}")
    public @ResponseBody ResponseEntity<HorseDto> deleteHorse(
            @PathVariable("horseId") String horseGUID) {
        Optional<HorseDto> response = horseService.deleteHorse(horseGUID);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    @RequestMapping(value = "/{horseID}", method = PUT)
    public @ResponseBody ResponseEntity<HorseDto> updateHorseByID(
            @PathVariable("horseID") String horseGUID,
            @RequestBody HorseDto horseDto) {
        Optional<HorseDto> response = horseService.updateHorseByID(horseGUID, horseDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }
}
