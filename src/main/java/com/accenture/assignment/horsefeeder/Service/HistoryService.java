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
