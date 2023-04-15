package com.accenture.assignment.horsefeeder.Service;

import com.accenture.assignment.horsefeeder.DTO.*;
import com.accenture.assignment.horsefeeder.Entities.Food;
import com.accenture.assignment.horsefeeder.Entities.History;
import com.accenture.assignment.horsefeeder.Entities.Horse;
import com.accenture.assignment.horsefeeder.Mapper.HistoryMapper;
import com.accenture.assignment.horsefeeder.Mapper.HorseMapper;
import com.accenture.assignment.horsefeeder.Mapper.ScheduleMapper;
import com.accenture.assignment.horsefeeder.Repository.FoodRepository;
import com.accenture.assignment.horsefeeder.Repository.HistoryRepository;
import com.accenture.assignment.horsefeeder.Repository.HorseRepository;
import com.accenture.assignment.horsefeeder.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {
    @Autowired
    private HistoryMapper historyMapper;
    @Autowired
    private HorseMapper horseMapper;
    private HistoryRepository historyRepository;
    private ScheduleService scheduleService;
    private HorseRepository horseRepository;
    private FoodRepository foodRepository;

    public HistoryService(ScheduleService scheduleService, FoodRepository foodRepository, HorseRepository horseRepository, HistoryRepository historyRepository) {
        this.horseRepository = horseRepository;
        this.historyRepository = historyRepository;
        this.foodRepository = foodRepository;
        this.scheduleService = scheduleService;
    }

    //public Optional

    public Optional<HistoryDto> releaseFood(String guid) throws ParseException {
        TimeDto time = new TimeDto();
        time.setTime("14:00");
        Optional<List<HorseDto>> horses = scheduleService.showEligableHorses(time);
        if(horses.isEmpty()){
            return Optional.empty();
        }
        HorseDto feedHorse = null;
        for (HorseDto horseDto : horses.get()) {
            if(horseDto.getGuid().equals(guid)){
               feedHorse = horseDto;
               break;
            }
        }
        if(feedHorse==null){
            return Optional.empty();
        }
        int max= 10;
        int min= 0;
        int amountLeft = (int)(Math.random()*(max-min+1)+min);
        History history = new History();
        history.setAmount(amountLeft);
        history.setHorse(horseRepository.findByGuid(guid).get());
        history.setStatus("done");
        history.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        Food f = new Food();
        f = foodRepository.findById(1L).get();
        history.setFood(f);
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

    public Optional<List<MissedFeedingDto>> showMissed() {
        List<History> historyList = historyRepository.findAll();
        List<Horse> horseList = horseRepository.findAll();
        List<MissedFeedingDto> missedFeedingDtos = new ArrayList<>();
        if (historyList.isEmpty()) {
            return Optional.empty();
        }
        int missed = 0;
        int missedFood=0;
        for (Horse horse : horseList) {
            missed = 0;
            for (History history : historyList) {
                if(history.getHorse().getGuid().equals(horse.getGuid())){
                    if(history.getStatus().equals("missed")){
                       missed++;
                       missedFood+= history.getAmount();
                    }
                }

            }
            if(missed!=0 && missedFood!=0){
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
