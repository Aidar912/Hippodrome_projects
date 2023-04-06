package com.example.app1.service;

import com.example.app1.entities.Horse;
import com.example.app1.entities.Races;
import com.example.app1.repositories.HorseRepository;
import com.example.app1.repositories.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorseService {

    @Autowired
    HorseRepository horseRepository;

    @Autowired
    RaceRepository raceRepository;



    public void save(Horse Horse){
        horseRepository.save(Horse);
    }

    public Optional<Horse> findById(Long id){
        return horseRepository.findById(id);
    }
    public List<Horse> getAllHorse() {
        return horseRepository.findAll();
    }

    public void deleteAll() {
        horseRepository.deleteAll();
    }

    public double calculateCoefficient(Long id) {
        Optional<Horse> horse = horseRepository.findById(id);
        double winRate = (double) horse.get().getWins() / horse.get().getRace();
        double ageFactor = 1.0 + ((double) horse.get().getAge() / 10.0); // увеличиваем коэффициент с возрастом лошади
        double coefficient = winRate * ageFactor;
        return coefficient < 1 ? 1 : coefficient;
    }


    public List<Horse> getHorsesForRace(Long raceId) {
        Races race = raceRepository.findById(raceId).orElseThrow(() -> new RuntimeException("Race not found"));
        return race.getHorses();
    }

}
