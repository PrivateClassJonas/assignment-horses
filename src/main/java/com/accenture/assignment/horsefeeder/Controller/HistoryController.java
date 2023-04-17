package com.accenture.assignment.horsefeeder.Controller;

import com.accenture.assignment.horsefeeder.DTO.*;
import com.accenture.assignment.horsefeeder.Service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
/**
 * Die Klasse HistoryController ist ein REST-Controller der HistoryDto, MissedEligibleDto, MissedFeedingDto
 * und MissedAmountDto Objekte durch CRUD-Operationen bereitstellt/manipuliert/löscht
 */
@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    /**
     * HTTP POST Request, der Futter für ein bestimmtes Pferd zu einer bestimmten Zeit ausgibt
     * @param horseGuid
     *     Die GUID des Pferdes, dass Futter möchte
     * @param time
     *     Die Uhrzeit (aktuell), um zu überprüfen, ob das Pferd zugelassen ist zu essen
     * @return ResponseEntity<HistoryDto>
     *     Ein ResponseEntity, dass den neuen 'feeding_history' Eintrag zeigt, falls das Pferd fressen darf
     *
     * @throws ParseException
     */
    @PostMapping(path = "/{horseGuid}")
    public @ResponseBody ResponseEntity<HistoryDto> releaseFood(
            @PathVariable("horseGuid") String horseGuid,
            @RequestBody TimeDto time) throws ParseException {
        Optional<HistoryDto> response = historyService.releaseFood(horseGuid, time);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP POST Request, der einen eintrag macht, wenn ein Pferd zu einer Gewissen Uhrzeit sein Essen verpasst hat
     * @param horseGuid
     *     Die GUID des Pferdes, dass sein Essen verpasst hat
     * @param time
     *     Die Uhrzeit (aktuell) an der das Pferd sein Essen verpasst hat
     * @return ResponseEntity<HistoryDto>
     *     ResponseEntity, dass den Eintrag in der Tabelle zurückgibt
     * @throws ParseException
     */
    @PostMapping(path = "/missed/{horseGuid}")
    public @ResponseBody ResponseEntity<HistoryDto> setMissedFeeding(
            @PathVariable("horseGuid") String horseGuid,
            @RequestBody TimeDto time) throws ParseException {
        Optional<HistoryDto> response = historyService.setMissedFeeding(horseGuid, time);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP GET Request, der alle History Einträge in der Datenbank zurückgibt
     * @return ResponseEntity<List<HistoryDto>>
     *     ResponseEntity, dass eine Liste von allen History Einträgen zurückgibt
     *
     */
    @GetMapping("/")
    public ResponseEntity<List<HistoryDto>> showHistories() {
        Optional<List<HistoryDto>> response = historyService.showHistories();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP GET Request, der augibt wenn Pferde nicht gefressen haben und für wie lange das Pferd nicht gefressen hat
     * @param time
     *     Die Uhtzeit (aktuell), zu der nachgesehen wird, ob Pferde nicht gefressen haben
     * @return
     * @throws ParseException
     */
    @GetMapping("/eligable")
    public ResponseEntity<List<MissedEligibleDto>> showMissedEligable(
            @RequestBody TimeDto time) throws ParseException {
        Optional<List<MissedEligibleDto>> response = historyService.showMissedEligible(time);
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }
    /**
     * HTTP GET Request, der augibt welche Pferde nicht gefressen haben, und wie oft sie nicht gefressen haben
     * @return ResponseEntity<List<MissedFeedingDto>>
     *     ResponseEntity, dass eine Liste ausgibt in der alle Pferde aufgeleistet sind, die nicht gegessen haben, und wie oft diese nicht gegessen haben
     */
    @GetMapping("/missed")
    public ResponseEntity<List<MissedFeedingDto>> showMissed() {
        Optional<List<MissedFeedingDto>> response = historyService.showMissed();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

    /**
     * HTTP GET Request, der ausgibt welche Pferde nicht gegessen haben, und wieviel sie nicht gegessen haben
     * @return ResponseEntity<List<MissedAmountDto>>
     *     ResponseEntity, dass eine Liste aller Pferde auusgibt, die nicht gegessen haben, und wieviel sie nicht gegessen haben
     */
    @GetMapping("/amount")
    public ResponseEntity<List<MissedAmountDto>> showMissedAmount() {
        Optional<List<MissedAmountDto>> response = historyService.showMissedAmount();
        if (response.isEmpty()) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response.get());
    }

}
