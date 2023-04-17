package com.accenture.assignment.horsefeeder.Mapper;

import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StableMapper {
    List<StableDto> stableToStableDtos(List<Stable> stables);

    Stable stableDtoToStable(StableDto dto);

    StableDto stableToStableDto(Stable stable);


}
