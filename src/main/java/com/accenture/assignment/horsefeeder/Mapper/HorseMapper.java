package com.accenture.assignment.horsefeeder.Mapper;

import com.accenture.assignment.horsefeeder.DTO.HorseDto;
import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Entities.Horse;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HorseMapper {
    List<HorseDto> horseTohorseDtos(List<Horse> horses);

    Horse horseDtoToHorse(HorseDto dto);

    @Mapping(target = "stableName", source = "stable.name")
    @Mapping(target = "stableId", source = "stable.id")
    HorseDto horseToHorseDto(Horse horse);


}
