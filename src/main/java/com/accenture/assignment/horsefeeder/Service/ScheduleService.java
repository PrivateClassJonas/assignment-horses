package com.accenture.assignment.horsefeeder.Service;

import com.accenture.assignment.horsefeeder.DTO.HorseDto;
import com.accenture.assignment.horsefeeder.DTO.ScheduleDto;
import com.accenture.assignment.horsefeeder.DTO.TimeDto;
import com.accenture.assignment.horsefeeder.Entities.Food;
import com.accenture.assignment.horsefeeder.Entities.Horse;
import com.accenture.assignment.horsefeeder.Entities.Schedule;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import com.accenture.assignment.horsefeeder.Mapper.HorseMapper;
import com.accenture.assignment.horsefeeder.Mapper.ScheduleMapper;
import com.accenture.assignment.horsefeeder.Repository.FoodRepository;
import com.accenture.assignment.horsefeeder.Repository.HorseRepository;
import com.accenture.assignment.horsefeeder.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private HorseMapper horseMapper;
    private ScheduleRepository scheduleRepository;
    private HorseRepository horseRepository;
    private FoodRepository foodRepository;

    public ScheduleService(FoodRepository foodRepository, HorseRepository horseRepository, ScheduleRepository scheduleRepository) {
        this.horseRepository = horseRepository;
        this.scheduleRepository = scheduleRepository;
        this.foodRepository = foodRepository;
    }

    public Optional<List<ScheduleDto>> showSchedules() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        if (scheduleList.isEmpty()) {
            return Optional.empty();
        }
        List<ScheduleDto> scheduleDtoList = scheduleMapper.scheduleToscheduleDtos(scheduleList);
        if (scheduleDtoList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(scheduleDtoList);
    }

    public Optional<List<HorseDto>> showEligableHorses(TimeDto time) throws ParseException {
        Date dtime = null;
        if(time == null || time.getTime() == null ||  time.getTime().length()==0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, LocalTime.now().getHour());
            calendar.set(Calendar.MINUTE, LocalTime.now().getMinute());
            dtime = calendar.getTime();
        }else {
            try {
                dtime = new SimpleDateFormat("HH:mm").parse(time.getTime());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Optional<List<Horse>> horseList = Optional.of(new ArrayList<Horse>());
        List<Schedule> scheduleList = scheduleRepository.findAll();
        for (Schedule schedule : scheduleList) {
            Date start = new SimpleDateFormat("HH:mm").parse(schedule.getStart());
            Date end = new SimpleDateFormat("HH:mm").parse(schedule.getEnd());
            int compare1 = start.compareTo(dtime);
            int compare2 = dtime.compareTo(end);
            if( compare1 == 0 || compare1 == compare2){
                horseList.get().add((horseRepository.findByGuid(schedule.getHorse().getGuid())).get());
            }
        }
        if(horseList.get().isEmpty()){
            return Optional.empty();
        }

        List<HorseDto> result = horseMapper.horseTohorseDtos(horseList.get());
        return Optional.ofNullable(result);
    }

    public Optional<ScheduleDto> addNewSchedule(ScheduleDto scheduleDto) throws ParseException {
        if (scheduleDto == null) {
            return Optional.empty();
        }
        if(!checkForOverlap(scheduleDto)){
            return Optional.empty();
        }
        Optional<Food> food = foodRepository.findById(scheduleDto.getFoodId());
        Optional<Horse> horse = horseRepository.findByGuid(scheduleDto.getHorseGuid());
        if(food.isEmpty() || horse.isEmpty()){
            return Optional.empty();
        }
        Schedule schedule = scheduleMapper.scheduleDtoToSchedule(scheduleDto);
        schedule.setFood(food.get());
        schedule.setHorse(horse.get());
        Schedule savedSchedule = scheduleRepository.save(schedule);
        ScheduleDto result = scheduleMapper.scheduleToScheduleDto(savedSchedule);
        return Optional.ofNullable(result);

    }

    private boolean checkForOverlap(ScheduleDto scheduleDto) throws ParseException {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        Date startDto = new SimpleDateFormat("HH:mm").parse(scheduleDto.getStart());
        Date endDto = new SimpleDateFormat("HH:mm").parse(scheduleDto.getEnd());

        for (Schedule schedule : scheduleList) {
            Date start = new SimpleDateFormat("HH:mm").parse(schedule.getStart());
            Date end = new SimpleDateFormat("HH:mm").parse(schedule.getEnd());
            int compare1 = start.compareTo(endDto);
            int compare2 = startDto.compareTo(end);
            if(compare1 == compare2){
                return false;
            }
        }
        return true;
    }


}
