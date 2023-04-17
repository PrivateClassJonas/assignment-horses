package com.accenture.assignment.horsefeeder.Controller;

import com.accenture.assignment.horsefeeder.DTO.HorseDto;
import com.accenture.assignment.horsefeeder.Service.HorseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Die Klasse HorseController ist ein REST-Controller der HorseDto Objekte durch CRUD-Operationen bereitstellt/manipuliert/löscht
 */
@RestController
@RequestMapping("/horse")
public class HorseController {
    @Autowired
    private HorseService horseService;

    /**
     * HTTP GET Request, der eine Liste aller Pferde ausgibt
     * @return ResponseEntity<List<HorseDto>>
     *     ResponseEntity, dass eine Liste mit allen Pferde Objekten zurückgibt
     */
    @GetMapping("/")
    public ResponseEntity<List<HorseDto>> showHorses() {
        Optional<List<HorseDto>> response = horseService.showHorses();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP GET Request, der ein gewünschtes Pferd ausgibt
     * @param horseId
     *    Die GUID des Pferdes, dass man ausgeben will
     * @return  ResponseEntity<HorseDto>
     *     ResponseEntity, dass das geünschte Pferd zurückgibt
     */
    @GetMapping("/{horseId}")
    public ResponseEntity<HorseDto> showHorseById(
            @PathVariable("horseId") String horseId) {
        Optional<HorseDto> response = horseService.showHorseById(horseId);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP POST Request, der ein neues Pferd hinzufügt
     * @param horseDto
     *     Das Pferd, dass hinzugefügt werden soll
     * @return ResponseEntity<HorseDto>
     *     ResponseEntity, dass das neu hinzugefügte Pferd zurückgibt
     *
     */
    @PostMapping(path = "/")
    public @ResponseBody ResponseEntity<HorseDto> addNewHorse(
            @RequestBody HorseDto horseDto) {
        Optional<HorseDto> response = horseService.addNewHorse(horseDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP DELETE Request, der ein gewünschtes Pferd löscht
     * @param horseGUID
     *     Die GUID von dem Pferd, dass man löschen möchte
     * @return ResponseEntity<HorseDto>
     *     ResponseEntity, dass das gelöschte Pferd zurückgibt
     */
    @DeleteMapping(path = "/{horseId}")
    public @ResponseBody ResponseEntity<HorseDto> deleteHorse(
            @PathVariable("horseId") String horseGUID) {
        Optional<HorseDto> response = horseService.deleteHorse(horseGUID);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP PUT Request, der ein gewünschtes Pferd verändert
     * @param horseGUID
     *     Die GUID von dem Pferd, dass man ändern möchte
     * @param horseDto
     *     Das neue Pferd Objekt
     * @return ResponseEntity<HorseDto>
     *     ResponseEntity, dass das geänderte Pferd zurpckgibt
     */
    @RequestMapping(value = "/{horseID}", method = PUT)
    public @ResponseBody ResponseEntity<HorseDto> updateHorseByID(
            @PathVariable("horseID") String horseGUID,
            @RequestBody HorseDto horseDto) {
        Optional<HorseDto> response = horseService.updateHorseByID(horseGUID, horseDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }
}
