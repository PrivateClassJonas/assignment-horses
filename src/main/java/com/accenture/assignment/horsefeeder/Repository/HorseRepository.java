package com.accenture.assignment.horsefeeder.Repository;

import com.accenture.assignment.horsefeeder.Entities.Horse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HorseRepository extends JpaRepository<Horse, Long> {
    Optional<Horse> findByGuid(String guid);
}
