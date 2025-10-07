package com.debtgo.debtgo_backend.repository;

import com.debtgo.debtgo_backend.domain.simulation.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {
    List<Simulation> findByUserId(Long userId);
}