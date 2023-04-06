package com.example.app1.repositories;

import com.example.app1.entities.Horse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HorseRepository extends JpaRepository<Horse, Long> {
    Optional<Horse> findById(Long id);

}
