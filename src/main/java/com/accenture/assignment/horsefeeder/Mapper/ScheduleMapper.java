package com.accenture.assignment.horsefeeder.Mapper;

import com.accenture.assignment.horsefeeder.DTO.ScheduleDto;
import com.accenture.assignment.horsefeeder.Entities.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    List<ScheduleDto> scheduleToscheduleDtos(List<Schedule> schedules);

    Schedule scheduleDtoToSchedule(ScheduleDto dto);

    @Mapping(target = "horseName", source = "horse.name")
    @Mapping(target = "horseGuid", source = "horse.guid")
    @Mapping(target = "foodName", source = "food.name")
    @Mapping(target = "foodId", source = "food.id")
    ScheduleDto scheduleToScheduleDto(Schedule schedule);

}
