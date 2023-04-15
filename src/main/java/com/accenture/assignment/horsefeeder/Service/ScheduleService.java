package com.accenture.assignment.horsefeeder.Service;

import com.accenture.assignment.horsefeeder.DTO.HorseDto;
import com.accenture.assignment.horsefeeder.DTO.ScheduleDto;
import com.accenture.assignment.horsefeeder.Entities.Horse;
import com.accenture.assignment.horsefeeder.Entities.Schedule;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import com.accenture.assignment.horsefeeder.Mapper.ScheduleMapper;
import com.accenture.assignment.horsefeeder.Repository.HorseRepository;
import com.accenture.assignment.horsefeeder.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private ScheduleRepository scheduleRepository;
    private HorseRepository horseRepository;

    public ScheduleService(HorseRepository horseRepository, ScheduleRepository scheduleRepository) {
        this.horseRepository = horseRepository;
        this.scheduleRepository = scheduleRepository;
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
        return Optional.ofNullable(
                scheduleDtoList);
    }

    public Optional<HorseDto> addNewSchedule(ScheduleDto scheduleDto) {
        if (scheduleDto == null) {
            return Optional.empty();
        }
        Schedule schedule = scheduleMapper.scheduleDtoToSchedule(scheduleDto);
        String guid = UUID.randomUUID().toString();


        return Optional.ofNullable(null);
    }
}
