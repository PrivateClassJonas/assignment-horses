package com.accenture.assignment.horsefeeder.Service;

import com.accenture.assignment.horsefeeder.DTO.StableDto;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import com.accenture.assignment.horsefeeder.Mapper.StableMapper;
import com.accenture.assignment.horsefeeder.Repository.StableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 *Geschäftslogik für alle Stable Abfragen
 *
 */
@Service
public class StableService {
    @Autowired
    private StableMapper stableMapper;
    private StableRepository stableRepository;

    public StableService(StableRepository stableRepository) {
        this.stableRepository = stableRepository;
    }

    /**
     * zeigt alle Stable Objekte an
     * @return Optional<List<StableDto>>
     *     Optional von einer Liste von Stable Objekten --> Optional ist leer wenn nichts gefunden wurde
     */
    public Optional<List<StableDto>> showStables() {
        List<Stable> stableList = stableRepository.findAll();
        if (stableList.isEmpty()) {
            return Optional.empty();
        }
        List<StableDto> stableDtoList = stableMapper.stableToStableDtos(stableList);
        if (stableDtoList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(stableDtoList);
    }

    /**
     * zeigt einen Stable mit einer bestimmten ID
     * @param stableId
     *     Die ID von dem Stable Objekt, dass ausgegeben werden soll
     * @return Optional<StableDto>
     *     Optional von einem Stable Objekt --> Optional ist leer wenn nichts gefunden wurde
     */
    public Optional<StableDto> showStableById(Long stableId) {
        if (stableId == null) {
            return Optional.empty();
        }
        Optional<Stable> optionalStable = stableRepository.findById(stableId);
        if (optionalStable.isEmpty()) {
            return Optional.empty();
        }
        Stable stable = optionalStable.get();
        StableDto stableDto = stableMapper.stableToStableDto(stable);
        return Optional.ofNullable(stableDto);
    }

    /**
     * fügt ein neues Stable Objekt hinzu
     * @param stableDto
     *     Das Stable Objekt, dass hinzugefügt werden soll
     * @return Optional<StableDto>
     *     Optional von einem Stable Objekt, dass hinzugefügt wurde --> Optional ist leer wenn das Stable Objekt nicht hinzugefügt werden konnte
     */
    public Optional<StableDto> addNewStable(StableDto stableDto) {
        if (stableDto == null) {
            return Optional.empty();
        }
        Stable stable = stableMapper.stableDtoToStable(stableDto);
        Stable savedStable = stableRepository.save(stable);
        if (savedStable == null) {
            return Optional.empty();
        }
        StableDto result = stableMapper.stableToStableDto(savedStable);
        return Optional.ofNullable(result);
    }

    /**
     * löscht ein Stable Objekt mit einer bestimmten ID
     * @param stableId
     *     Die ID von dem Stable Objekt, dass gelöscht werden soll
     * @return Optional<StableDto>
     *     Optional von dem Stable Objekt, dass gelöscht wurde --> Optional ist leer wenn ncihts gelöscht wurde
     */
    public Optional<StableDto> deleteStableById(Long stableId) {
        if (stableId == null) {
            return Optional.empty();
        }
        Optional<Stable> optionalStable = stableRepository.findById(stableId);
        if (optionalStable.isEmpty()) {
            return Optional.empty();
        }
        Stable deleteStable = optionalStable.get();
        stableRepository.delete(deleteStable);
        StableDto result = stableMapper.stableToStableDto(deleteStable);
        return Optional.ofNullable(result);
    }

    /**
     * ändert ein bestimmtes Stable Objekt mit einer bestimmten ID
     * @param stableId
     *     Die ID von dem Stable Objekt, dass geändert werden soll
     * @param stableDto
     *     Das neue Stable Objekt
     * @return Optional<StableDto>
     *     Optional von dem neuen Stable Objekt --> Optional ist leer wenn nichts geändert wurde
     */
    public Optional<StableDto> updateStableByID(Long stableId, StableDto stableDto) {
        if (stableId == null || stableDto == null) {
            return Optional.empty();
        }
        Optional<Stable> optionalStable = stableRepository.findById(stableId);
        if (optionalStable.isEmpty()) {
            return Optional.empty();
        }
        Stable updateStable = optionalStable.get();
        updateStable.setName(stableDto.getName());
        stableRepository.save(updateStable);
        return Optional.ofNullable(stableMapper.stableToStableDto(updateStable));
    }
}
