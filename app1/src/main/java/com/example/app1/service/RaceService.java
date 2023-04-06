package com.example.app1.service;

import com.example.app1.entities.Horse;
import com.example.app1.entities.Races;
import com.example.app1.repositories.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceService {
    @Autowired
    private RaceRepository raceRepository;

    public List<Races> getAllRace() {
        return raceRepository.findAll();
    }

    public void clearAll(){
        raceRepository.deleteAll();
    }

    public void deleteById(Long id){
        raceRepository.deleteById(id);
    }

}
