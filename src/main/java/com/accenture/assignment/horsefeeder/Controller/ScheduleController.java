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
/**
 * Die Klasse HorseController ist ein REST-Controller der ScheduleDto und HorseDto Objekte durch CRUD-Operationen bereitstellt/manipuliert/löscht
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    /**
     * HTTP GET Request, der alle Pläne zurückgibt
     * @return ResponseEntity<List<ScheduleDto>>
     *     ResponseEntity, dass eine Liste mit allen Plänen zurückgibt
     */
    @GetMapping("/")
    public ResponseEntity<List<ScheduleDto>> showHorses() {
        Optional<List<ScheduleDto>> response = scheduleService.showSchedules();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP POST Request, der einen neuen Plan hinzufügt
     * @param scheduleDto
     *     Der neue Plan, der hinzugefügt werden soll
     * @return ResponseEntity<ScheduleDto>
     *     ResponseEntity, dass den neuen Plan zurückgibt
     * @throws ParseException
     */
    @PostMapping(path = "/")
    public @ResponseBody ResponseEntity<ScheduleDto> addNewSchedule(
            @RequestBody ScheduleDto scheduleDto) throws ParseException {
        Optional<ScheduleDto> response = scheduleService.addNewSchedule(scheduleDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP DELETE Request, der einen bestimmten Plan löscht
     * @param id
     *     Die ID von dem Plan, den man löschen möchte
     * @return ResponseEntity<ScheduleDto>
     *     ReponseEntity, dass den gelöschten Plan zurückgibt
     * @throws ParseException
     */
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<ScheduleDto> deleteScheduleById(
            @PathVariable Long id) throws ParseException {
        Optional<ScheduleDto> response = scheduleService.deleteScheduleById(id);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP GET Request, der alle Pferde ausgibt, die zu einer gewissen Uhrzeit essen dürfen
     * @param time
     *     Die Uhrzeit, an der die Pferde essen dürfen
     * @return ResponseEntity<List<HorseDto>>
     *     ResponseEntity, dass eine Liste von allen Pferden zurpckgibt, die zu einer gewissen Uhrzeit essen dürfen
     * @throws ParseException
     */
    @GetMapping(path = "/eligable/")
    public @ResponseBody ResponseEntity<List<HorseDto>> showEligableHorses(
            @RequestBody TimeDto time) throws ParseException {
        Optional<List<HorseDto>> response = scheduleService.showEligableHorses(time);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }
}
