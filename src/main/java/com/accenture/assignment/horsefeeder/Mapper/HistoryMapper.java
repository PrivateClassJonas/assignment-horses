package com.accenture.assignment.horsefeeder.Mapper;

import com.accenture.assignment.horsefeeder.DTO.HistoryDto;
import com.accenture.assignment.horsefeeder.DTO.ScheduleDto;
import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Entities.History;
import com.accenture.assignment.horsefeeder.Entities.Schedule;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HistoryMapper {
    List<HistoryDto> historyToHistoryDtos(List<History> histories);

    @Mapping(target = "horseName", source = "horse.name")
    @Mapping(target = "horseGuid", source = "horse.guid")
    HistoryDto scheduleToScheduleDto(History history);

}
