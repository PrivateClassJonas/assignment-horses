package com.accenture.assignment.horsefeeder.Service;

import com.accenture.assignment.horsefeeder.DTO.*;
import com.accenture.assignment.horsefeeder.Entities.*;
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
import java.util.*;

/**
 * Geschäftslogik aller Schedule Abfragen
 */
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

    /**
     * zeigt alle Schedule Objekte an
     * @return Optional<List<ScheduleDto>>
     *     Optional von einer Liste von allen Schedule Objekten
     */
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

    /**
     * zeigt alle Horse Objekte, die zu eriner bestimmten Uhrzeit essen dürfen
     * @param time
     *     Die Uhrzeit, zu der das Pferd essen soll
     * @return Optional<List<HorseDto>>
     *      Optional von einer Liste von allen Horse Objekten, die zu der gewünschten Uhrzeit essen dürfen
     * @throws ParseException
     */
    public Optional<List<HorseDto>> showEligableHorses(TimeDto time) throws ParseException {
        Date dtime = null;
        if (time == null || time.getTime() == null || time.getTime().length() == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, LocalTime.now().getHour());
            calendar.set(Calendar.MINUTE, LocalTime.now().getMinute());
            dtime = calendar.getTime();
        } else {
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
            if (compare1 == 0 || compare1 == compare2) {
                horseList.get().add((horseRepository.findByGuid(schedule.getHorse().getGuid())).get());
                break;
            }
        }
        if (horseList.get().isEmpty()) {
            return Optional.empty();
        }

        List<HorseDto> result = horseMapper.horseTohorseDtos(horseList.get());
        return Optional.ofNullable(result);
    }

    /**
     * zeigt Schedule Objekte, die zu einer bestimmten Uhrzeit in Frage kommen für die Futterausgabe
     * @param time
     *     Die Uhrzeit, um zu überprüfen, welche Schedule Objekte in Frage kommen
     * @return Optional<ScheduleDto>
     *     Optional von dem Schedule, dass zu einer bestimmten Uhrzeit in Frage kommt
     * @throws ParseException
     */
    public Optional<ScheduleDto> showScheduleForFeeding(TimeDto time) throws ParseException {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setStart(time.getTime());
        List<Schedule> scheduleList = scheduleRepository.findAll();
        Date timeToCheck = new SimpleDateFormat("HH:mm").parse(scheduleDto.getStart());

        for (Schedule schedule : scheduleList) {
            Date start = new SimpleDateFormat("HH:mm").parse(schedule.getStart());
            Date end = new SimpleDateFormat("HH:mm").parse(schedule.getEnd());
            if (timeToCheck.compareTo(start) == 0 || (timeToCheck.compareTo(start) > 0 && timeToCheck.compareTo(end) < 0)) {
                return Optional.ofNullable(scheduleMapper.scheduleToScheduleDto(schedule));
            }
        }
        return Optional.empty();
    }

    /**
     * fügt ein neues Schedule Objekt hinzu
     * @param scheduleDto
     *     Das neue Schedule Objekt
     * @return Optional<ScheduleDto>
     *     Optional von dem Schedule Objekt, dass hinzugefügt wurde
     * @throws ParseException
     */
    public Optional<ScheduleDto> addNewSchedule(ScheduleDto scheduleDto) throws ParseException {
        if (scheduleDto == null) {
            return Optional.empty();
        }
        if (!checkForOverlap(scheduleDto)) {
            return Optional.empty();
        }
        Date start = new SimpleDateFormat("HH:mm").parse(scheduleDto.getStart());
        Date end = new SimpleDateFormat("HH:mm").parse(scheduleDto.getEnd());
        if (start.after(end)) {
            return Optional.empty();
        }
        List<Schedule> schedules = scheduleRepository.findAll();
        int index = 0;
        for (Schedule schedule : schedules) {
            if (scheduleDto.getHorseGuid().equals(schedule.getHorse().getGuid())) {
                index++;
            }
        }
        if (index >= 5) {
            return Optional.empty();
        }
        Optional<Food> food = foodRepository.findById(scheduleDto.getFoodId());
        Optional<Horse> horse = horseRepository.findByGuid(scheduleDto.getHorseGuid());
        if (food.isEmpty() || horse.isEmpty()) {
            return Optional.empty();
        }
        Schedule schedule = scheduleMapper.scheduleDtoToSchedule(scheduleDto);
        schedule.setFood(food.get());
        schedule.setHorse(horse.get());

        Schedule savedSchedule = scheduleRepository.save(schedule);
        ScheduleDto result = scheduleMapper.scheduleToScheduleDto(savedSchedule);
        return Optional.ofNullable(result);

    }

    /**
     * löscht ein bestimmtes Schedule Objekt mit einer bestimmten ID
     * @param id
     *     Die ID von dem Schedule Objekt, dass gelöscht werden soll
     * @return Optional<ScheduleDto>
     *     Optional von dem SChedule Objekt, dass gelöscht wurde
     * @throws ParseException
     */
    public Optional<ScheduleDto> deleteScheduleById(Long id) throws ParseException {
        if (id == null) {
            return Optional.empty();
        }
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (optionalSchedule.isEmpty()) {
            return Optional.empty();
        }
        Schedule deleteSchedule = optionalSchedule.get();
        scheduleRepository.delete(deleteSchedule);
        ScheduleDto result = scheduleMapper.scheduleToScheduleDto(deleteSchedule);
        return Optional.ofNullable(result);
    }

    /**
     * überprüft, ob ein Schedule Objekt mit den anderen Schedule Objekten überlappt
     * @param scheduleDto
     *      Das Schedule Objekt, dass man aus Überlappungen überprüfen will
     * @return boolean
     *     true: Schedule Objekt überlappt sich, false: Schedule Objekt überlappt sich nicht
     * @throws ParseException
     */
    private boolean checkForOverlap(ScheduleDto scheduleDto) throws ParseException {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        Date startDto = new SimpleDateFormat("HH:mm").parse(scheduleDto.getStart());
        Date endDto = new SimpleDateFormat("HH:mm").parse(scheduleDto.getEnd());

        for (Schedule schedule : scheduleList) {
            Date start = new SimpleDateFormat("HH:mm").parse(schedule.getStart());
            Date end = new SimpleDateFormat("HH:mm").parse(schedule.getEnd());
            int compare1 = start.compareTo(endDto);
            int compare2 = startDto.compareTo(end);
            if (compare1 == compare2) {
                return false;
            }
        }
        return true;
    }


}
