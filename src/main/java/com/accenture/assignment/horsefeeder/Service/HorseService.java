package com.accenture.assignment.horsefeeder.Service;

import com.accenture.assignment.horsefeeder.DTO.HorseDto;
import com.accenture.assignment.horsefeeder.Entities.Horse;
import com.accenture.assignment.horsefeeder.Entities.Stable;
import com.accenture.assignment.horsefeeder.Mapper.HorseMapper;
import com.accenture.assignment.horsefeeder.Repository.HorseRepository;
import com.accenture.assignment.horsefeeder.Repository.StableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Geschäftslogik für alle Horse Abfragen
 */
@Service
public class HorseService {
    @Autowired
    private HorseMapper horseMapper;
    private HorseRepository horseRepository;
    private StableRepository stableRepository;

    public HorseService(HorseRepository horseRepository, StableRepository stableRepository) {
        this.horseRepository = horseRepository;
        this.stableRepository = stableRepository;
    }

    /**
     * gibt alle Horse Objekte zurück
     * @return Optional<List<HorseDto>>
     *     Optional von einer Liste aller Horse Objekte --> Optional ist leer wenn nciht gefunden wurde
     */
    public Optional<List<HorseDto>> showHorses() {
        List<Horse> horseList = horseRepository.findAll();
        if (horseList.isEmpty()) {
            return Optional.empty();
        }
        List<HorseDto> horseDtoList = horseMapper.horseTohorseDtos(horseList);
        if (horseDtoList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(horseDtoList);
    }

    /**
     * gibt ein Horse Objekt mit einer bestimmten ID zurück
     * @param horseGUID
     *     Die GUID von dem Horse Objekt, dass zurpckgegeben werden soll
     * @return Optional<HorseDto>
     *     Optional von dem Horse Objekt, dass zurückgegeben wird --> Optional ist leer wenn nichts gefunden wurde
     */
    public Optional<HorseDto> showHorseById(String horseGUID) {
        if (horseGUID == null || horseGUID.length() == 0) {
            return Optional.empty();
        }
        Optional<Horse> optionalHorse = horseRepository.findByGuid(horseGUID);
        if (optionalHorse.isEmpty()) {
            return Optional.empty();
        }
        Horse horse = optionalHorse.get();
        HorseDto horseDto = horseMapper.horseToHorseDto(horse);
        return Optional.ofNullable(horseDto);
    }

    /**
     * fügt ein neues Horse Objekt hinzu
     * @param horseDto
     *      Das neue Horse Objekt, dass hinzugefüngt werden soll
     * @return Optional<HorseDto>
     *     Optional von dem Horse Objekt, dass hinzugefügt wurde --> Optional ist leer wenn nciht hinzugefügt wurde
     */
    public Optional<HorseDto> addNewHorse(HorseDto horseDto) {
        if (horseDto == null) {
            return Optional.empty();
        }
        Horse horse = horseMapper.horseDtoToHorse(horseDto);
        String guid = UUID.randomUUID().toString();
        horse.setGuid(guid);
        Optional<Stable> optionalStable = stableRepository.findById(horseDto.getStableId());
        if (optionalStable.isEmpty()) {
            return Optional.empty();
        }
        Stable stable = optionalStable.get();
        horse.setStable(stable);
        Horse savedHorse = horseRepository.save(horse);
        HorseDto result = horseMapper.horseToHorseDto(savedHorse);
        return Optional.ofNullable(result);
    }

    /**
     * löscht ein Horse Objekt mit einer bestimmten ID
     * @param horseGUID
     *     Die GUID von dem Horse Objekt, dass gelöscht werden soll
     * @return Optional<HorseDto>
     *     Optional von dem Horse Objekt, dass gelöscht wurde
     */
    public Optional<HorseDto> deleteHorse(String horseGUID) {
        Optional<Horse> optionalHorse = horseRepository.findByGuid(horseGUID);
        if (optionalHorse.isEmpty()) {
            return Optional.empty();
        }
        Horse deleteHorse = optionalHorse.get();
        HorseDto result = horseMapper.horseToHorseDto(deleteHorse);
        horseRepository.delete(deleteHorse);
        return Optional.ofNullable(result);
    }

    /**
     * ändert ein Horse Objekt
     * @param horseGUID
     *     Die GUID von dem Horse Objekt, dass geändert werden soll
     * @param horseDto
     *     Das neue Horse Objekt
     * @return Optional<HorseDto>
     *     Optional von dem Horse Objekt, dass geändert wurde --> Optional ist leer wenn nichts geändert wurde
     */
    public Optional<HorseDto> updateHorseByID(String horseGUID, HorseDto horseDto) {
        if (horseGUID == null || horseDto == null) {
            return Optional.empty();
        } else if (horseDto.getGuid() == null || horseDto.getGuid().length() < 36 || horseDto.getGuid().length() > 36) {
            return Optional.empty();
        }
        Optional<Horse> optionalHorse = horseRepository.findByGuid(horseGUID);
        if (optionalHorse.isEmpty()) {
            return Optional.empty();
        }
        Horse updateHorse = optionalHorse.get();
        updateHorse.setName(horseDto.getName());
        updateHorse.setNickname(horseDto.getNickname());
        updateHorse.setOwner(horseDto.getOwner());
        updateHorse.setBreed(horseDto.getBreed());
        updateHorse.setGuid(horseDto.getGuid());
        Optional<Stable> newStable = null;
        if (horseDto.getStableId() != null) {
            newStable = stableRepository.findById(horseDto.getStableId());
        }
        if (newStable != null && !newStable.isEmpty()) {
            updateHorse.setStable(newStable.get());
        } else {
            return Optional.empty();
        }
        horseRepository.save(updateHorse);
        return Optional.ofNullable(horseMapper.horseToHorseDto(updateHorse));
    }

}
