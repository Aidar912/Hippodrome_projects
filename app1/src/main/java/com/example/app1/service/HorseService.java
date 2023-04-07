package com.example.app1.service;

import com.example.app1.entities.Horse;
import com.example.app1.entities.Races;
import com.example.app1.repositories.HorseRepository;
import com.example.app1.repositories.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorseService {

    private static final double BASE_COEFFICIENT = 1.5;

    @Autowired
    HorseRepository horseRepository;

    @Autowired
    RaceRepository raceRepository;


    public void save(Horse Horse) {
        horseRepository.save(Horse);
    }

    public Horse findById(Long id) {
        return horseRepository.findById(id).orElse(null);
    }

    public List<Horse> getAllHorse() {
        return horseRepository.findAll();
    }

    public void deleteAll() {
        horseRepository.deleteAll();
    }

    public double calculateCoefficientWin(Long id) {
        Horse horse = horseRepository.findById(id).orElse(null);

        double coefficient = 1.0;

        assert horse != null;
        coefficient += 0.1 * horse.getWins();


        coefficient += 0.01 * horse.getRace();

        coefficient += 0.001 * horse.getVote();

        coefficient += 0.0001 * horse.getBetSum();

        coefficient -= 0.05 *horse.getAge();

        if (coefficient < 1.0) {
            coefficient = 1.0;
        }

        return coefficient;
    }

    public double calculateCoefficientLose(Long id) {
        Horse horse = horseRepository.findById(id).orElse(null);


        double coefficient = 1.0;

        assert horse != null;
        coefficient += 0.05 * (horse.getRace() - horse.getWins());

        coefficient += 0.01 * horse.getRace();


        coefficient += 0.001 * horse.getVote();


        coefficient += 0.0001 * horse.getBetSum();


        coefficient -= 0.05 * horse.getAge();


        if (coefficient < 1.0) {
            coefficient = 1.0;
        }

        return coefficient;
    }


    public List<Horse> getHorsesForRace(Long raceId) {
        Races race = raceRepository.findById(raceId).orElseThrow(() -> new RuntimeException("Race not found"));
        return race.getHorses();
    }

}
