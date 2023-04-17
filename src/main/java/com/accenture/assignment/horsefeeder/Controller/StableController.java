package com.accenture.assignment.horsefeeder.Controller;

import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Service.StableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Die Klasse StableController ist ein REST-Controller der StableDto Objekte durch CRUD-Operationen bereitstellt/manipuliert/löscht
 */
@RestController
@RequestMapping("/stable")
public class StableController {

    @Autowired
    private StableService stableService;

    /**
     * HTTP GET Request, der alle Ställe ausgibt
     * @return ResponseEntity<List<StableDto>>
     *     ResponseEntity, dass eine Liste aller Ställe zurückgibt
     */
    @GetMapping("/")
    public ResponseEntity<List<StableDto>> showStables() {
        Optional<List<StableDto>> response = stableService.showStables();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP GET Request, dass einen bestimmten Stall ausgibt
     * @param stableId
     *     Die ID von dem Stall, der ausgegeben werden soll
     * @return ResponseEntity<StableDto>
     *     ResponseEntity, dass den gewuschten Stall zurpckgibt
     *
     */
    @GetMapping("/{stableId}")
    public ResponseEntity<StableDto> showStableById(
            @PathVariable("stableId") Long stableId) {
        Optional<StableDto> response = stableService.showStableById(stableId);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP POST Request, der einen neuen Stall hinzufügt
     * @param stableDto
     *     Der neue Stall, der hinzugefügt werden soll
     * @return ResponseEntity<StableDto>
     *     ResponseEntity, dass den neuen Stall zurückgibt
     */
    @PostMapping(path = "/")
    public @ResponseBody ResponseEntity<StableDto> addNewStable(
            @RequestBody StableDto stableDto) {
        Optional<StableDto> response = stableService.addNewStable(stableDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP DELETE Request, der einen bestimmten Stall löscht
     * @param stableId
     *     Die ID von dem Stall, der gelöscht werden soll
     * @return ResponseEntity<StableDto>
     *     ResponseEntity, dass den gelöschten Stall zurpckgibt
     */
    @DeleteMapping("/{stableId}")
    public @ResponseBody ResponseEntity<StableDto> deleteStableById(
            @PathVariable("stableId") Long stableId) {
        Optional<StableDto> response = stableService.deleteStableById(stableId);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP PUT Request, der einen bestimmten Stall ändert
     * @param stableId
     *     Die ID von dem Stall der geändert werden soll
     * @param stableDto
     *     Der neue Stall
     * @return ResponseEntity<StableDto>
     *     ResponseEntity, dass den neuen Stall zurückgibt
     */
    @RequestMapping(value = "/{stableID}", method = PUT)
    public @ResponseBody ResponseEntity<StableDto> updateStableByID(
            @PathVariable("stableID") Long stableId,
            @RequestBody StableDto stableDto) {
        Optional<StableDto> response = stableService.updateStableByID(stableId, stableDto);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }
}
