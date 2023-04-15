package com.accenture.assignment.horsefeeder.Repository;

import com.accenture.assignment.horsefeeder.Entities.Stable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StableRepository extends JpaRepository<Stable, Long> {
    Optional<Stable> findById(Long id);
}
