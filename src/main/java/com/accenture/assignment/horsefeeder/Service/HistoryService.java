package com.accenture.assignment.horsefeeder.Service;

import com.accenture.assignment.horsefeeder.Mapper.HistoryMapper;
import com.accenture.assignment.horsefeeder.Mapper.HorseMapper;
import com.accenture.assignment.horsefeeder.Mapper.ScheduleMapper;
import com.accenture.assignment.horsefeeder.Repository.FoodRepository;
import com.accenture.assignment.horsefeeder.Repository.HistoryRepository;
import com.accenture.assignment.horsefeeder.Repository.HorseRepository;
import com.accenture.assignment.horsefeeder.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {
    @Autowired
    private HistoryMapper historyMapper;
    @Autowired
    private HorseMapper horseMapper;
    private HistoryRepository historyRepository;
    private HorseRepository horseRepository;
    private FoodRepository foodRepository;

    public HistoryService(FoodRepository foodRepository, HorseRepository horseRepository, HistoryRepository historyRepository) {
        this.horseRepository = horseRepository;
        this.historyRepository = historyRepository;
        this.foodRepository = foodRepository;
    }
}
