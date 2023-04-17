package com.accenture.assignment.horsefeeder.Service;

import com.accenture.assignment.horsefeeder.DTO.*;
import com.accenture.assignment.horsefeeder.Entities.Food;
import com.accenture.assignment.horsefeeder.Entities.History;
import com.accenture.assignment.horsefeeder.Entities.Horse;
import com.accenture.assignment.horsefeeder.Entities.Schedule;
import com.accenture.assignment.horsefeeder.Mapper.HistoryMapper;
import com.accenture.assignment.horsefeeder.Repository.FoodRepository;
import com.accenture.assignment.horsefeeder.Repository.HistoryRepository;
import com.accenture.assignment.horsefeeder.Repository.HorseRepository;
import com.accenture.assignment.horsefeeder.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Geschäftslogik gür alle History Abfragen
 */
@Service
public class HistoryService {
    @Autowired
    private HistoryMapper historyMapper;
    private HistoryRepository historyRepository;
    private ScheduleService scheduleService;
    private ScheduleRepository scheduleRepository;
    private HorseRepository horseRepository;
    private FoodRepository foodRepository;

    public HistoryService(ScheduleRepository scheduleRepository, ScheduleService scheduleService, FoodRepository foodRepository, HorseRepository horseRepository, HistoryRepository historyRepository) {
        this.horseRepository = horseRepository;
        this.historyRepository = historyRepository;
        this.foodRepository = foodRepository;
        this.scheduleService = scheduleService;
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * erstellt einen neuen History Eintrag für ein bestimmtes Horse Objekt zu einer bestimmten Uhrzeit und setzt diesen auf "missed" --> Das Pferd hat das Essen verpasst
     * @param guid
     *     Die GUID von dem Pferd, dass sein Essen verpasst hat
     * @param time
     *     Die Uhrzeit, um zu überprüfen, ob das Pferd sein Futter wurklich verpasst hat --> nachschauen bei allen Schedule Objekten
     * @return Optional<HistoryDto>
     *     Optional von einem History Objekt, dass den neuen Eintraf anzeigt --> Optional leer wenn nichts eingetragen wurde
     * @throws ParseException
     */
    public Optional<HistoryDto> setMissedFeeding(String guid, TimeDto time) throws ParseException {
        Optional<ScheduleDto> schedule = scheduleService.showScheduleForFeeding(time);
        Horse horse = horseRepository.findByGuid(guid).get();
        if (!schedule.get().getHorseGuid().equals(guid)) {
            return Optional.empty();
        }
        History history = new History();
        history.setTime(time.getTime());
        history.setStatus("missed");
        history.setHorse(horse);
        history.setAmount(20);
        Food food = foodRepository.findById(schedule.get().getFoodId()).get();
        history.setFood(food);

        History savedHistory = historyRepository.save(history);
        HistoryDto result = historyMapper.scheduleToScheduleDto(savedHistory);
        return Optional.ofNullable(result);
    }

    /**
     * zeigt wieviel Futter ein bestimmtes Pferd verpasst hat
     * @return Optional<List<MissedAmountDto>>
     *     Optional von einer Liste von MissedAmountDto Objekten
     */
    public Optional<List<MissedAmountDto>> showMissedAmount() {
        List<History> historyList = historyRepository.findAll();
        List<Horse> horseList = horseRepository.findAll();
        List<MissedAmountDto> missedAmountDtos = new ArrayList<>();
        if (historyList.isEmpty()) {
            return Optional.empty();
        }
        int missedFood = 0;
        for (Horse horse : horseList) {
            for (History history : historyList) {
                if (history.getHorse().getGuid().equals(horse.getGuid())) {
                    missedFood += history.getAmount();
                }
            }
            missedAmountDtos.add(new MissedAmountDto(horse.getGuid(), missedFood));
            missedFood = 0;
        }

        if (missedAmountDtos.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(missedAmountDtos);
    }

    /**
     * überprüft, ob ein bestimmtes Pferd zu einer bestimmten Zeit essen darf, und ob das Pferd schon gegessen hat und trägt anschließen. wenn das Pferd essen darf
     * einen neuen History Eintrag ein
     * @param guid
     *     GUID von dem Pferd, dass essen will
     * @param time
     *     Uhrzeit, an der das Pferd essen will
     * @return Optional<HistoryDto>
     *     Optional von einem neuen History Objekt --> Optional leer wenn kein Futter freigegeben wurde
     * @throws ParseException
     */
    public Optional<HistoryDto> releaseFood(String guid, TimeDto time) throws ParseException {
        List<History> histories = historyRepository.findAll();
        Optional<List<HorseDto>> horses = scheduleService.showEligableHorses(time);
        Optional<ScheduleDto> schedule = scheduleService.showScheduleForFeeding(time);
        if (!horses.get().get(0).getGuid().equals(schedule.get().getHorseGuid())) {
            return Optional.empty();
        }
        if (!histories.isEmpty()) {
            for (History history : histories) {
                if (history.getTime().equals(schedule.get().getStart())) {
                    return Optional.empty();
                }
            }
        }
        if (horses.isEmpty()) {
            return Optional.empty();
        }
        HorseDto feedHorse = null;
        for (HorseDto horseDto : horses.get()) {
            if (horseDto.getGuid().equals(guid)) {
                feedHorse = horseDto;
                break;
            }
        }
        if (feedHorse == null) {
            return Optional.empty();
        }
        int max = 10;
        int min = 0;
        int amountLeft = (int) (Math.random() * (max - min + 1) + min);
        History history = new History();
        history.setAmount(amountLeft);
        history.setHorse(horseRepository.findByGuid(guid).get());
        history.setStatus("done");
        history.setTime(schedule.get().getStart());
        List<Food> foods = foodRepository.findAll();
        Food foodToSave = null;
        for (Food food : foods) {
            if (food.getId() == schedule.get().getFoodId()) {
                foodToSave = foodRepository.findById(food.getId()).get();
            }
        }
        history.setFood(foodToSave);
        History savedHistory = historyRepository.save(history);
        HistoryDto result = historyMapper.scheduleToScheduleDto(savedHistory);
        return Optional.ofNullable(result);
    }

    /**
     * zeigt alle History Objekte
     * @return Optional<List<HistoryDto>>
     *     Optional von einer Liste von History Objekten
     */
    public Optional<List<HistoryDto>> showHistories() {
        List<History> historyList = historyRepository.findAll();
        if (historyList.isEmpty()) {
            return Optional.empty();
        }
        List<HistoryDto> historyDtoList = historyMapper.historyToHistoryDtos(historyList);
        if (historyDtoList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(historyDtoList);
    }

    /**
     * zeigt welche Pferde Ihr Essen verpasst haben und für wieviele Stunden diese nicht gegessen haben
     * @param time
     *      uhrzeit an dem alles überprüft wird
     * @return Optional<List<MissedEligibleDto>>
     *     Optional von einer Liste von MissedEligableDtos
     * @throws ParseException
     */
    public Optional<List<MissedEligibleDto>> showMissedEligible(TimeDto time) throws ParseException {
        Optional<List<HorseDto>> horses = scheduleService.showEligableHorses(time);
        List<History> historyList = historyRepository.findAll();
        List<Schedule> schedulesToCheck = scheduleRepository.findAll();
        List<Schedule> schedulesToSave = new ArrayList<>();
        Date timeToCheck = new SimpleDateFormat("HH:mm").parse(time.getTime());
        for (Schedule schedule : schedulesToCheck) {
            Date start = new SimpleDateFormat("HH:mm").parse(schedule.getEnd());
            if (timeToCheck.after(start)) {
                boolean isFed = false;
                for (History history : historyList) {
                    if (history.getTime().equals(schedule.getStart())) {
                        isFed = true;
                        break;
                    }
                }
                if (!isFed) {
                    schedulesToSave.add(schedule);
                }
            }
        }
        List<MissedEligibleDto> missedEligible = new ArrayList<>();
        for (Schedule schedule : schedulesToSave) {
            MissedEligibleDto missedEligibleDto = new MissedEligibleDto();
            Date start = new SimpleDateFormat("HH:mm").parse(schedule.getEnd());
            Long missedHours = (timeToCheck.getTime() - start.getTime()) / 1000 / 60 / 60;
            missedEligibleDto.setAmountTime(missedHours.toString());
            missedEligibleDto.setHorseGuid(schedule.getHorse().getGuid());
            missedEligible.add(missedEligibleDto);
        }
        return Optional.ofNullable(missedEligible);
    }

    /**
     * zeigt welche Pferde Ihr Futter verpasst haben, wie oft sie es verpasst haben und wieviel sie verpasst haben
     * @return Optional<List<MissedFeedingDto>>
     *     Optional von einer Liste von MissedFeedingDtos
     */
    public Optional<List<MissedFeedingDto>> showMissed() {
        List<History> historyList = historyRepository.findAll();
        List<Horse> horseList = horseRepository.findAll();
        List<MissedFeedingDto> missedFeedingDtos = new ArrayList<>();
        if (historyList.isEmpty()) {
            return Optional.empty();
        }
        int missed = 0;
        int missedFood = 0;
        for (Horse horse : horseList) {
            missed = 0;
            for (History history : historyList) {
                if (history.getHorse().getGuid().equals(horse.getGuid())) {
                    if (history.getStatus().equals("missed")) {
                        missed++;
                        missedFood += history.getAmount();
                    }
                }

            }
            if (missed != 0 && missedFood != 0) {
                MissedFeedingDto dto = new MissedFeedingDto();
                dto.setAmountFood(missedFood);
                dto.setAmountMissed(missed);
                dto.setHorseGuid(horse.getGuid());
                dto.setHorseName(horse.getName());
                missedFeedingDtos.add(dto);
            }
        }

        if (missedFeedingDtos.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(missedFeedingDtos);
    }


}
