package com.accenture.assignment.horsefeeder.Mapper;

import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StableMapper {
    List<StableDto> stableToStableDtos(List<Stable> stables);

    //@Mapping(target = "ahAdresse", source = "adresse")
    Stable stableDtoToStable(StableDto dto);

    //@Mapping(target = "adresse", source = "ahAdresse")
    StableDto stableToStableDto(Stable stable);


}
