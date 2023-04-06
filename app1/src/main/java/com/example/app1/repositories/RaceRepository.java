package com.example.app1.repositories;

import com.example.app1.entities.Races;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceRepository extends JpaRepository<Races, Long> {

}
