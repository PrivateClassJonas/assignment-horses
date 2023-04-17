package com.accenture.assignment.horsefeeder.Repository;

import com.accenture.assignment.horsefeeder.Entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    Optional<History> findById(Long id);
}
