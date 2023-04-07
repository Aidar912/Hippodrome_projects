package com.example.app1.service;

import com.example.app1.entities.Bets;
import com.example.app1.entities.Races;
import com.example.app1.entities.User;
import com.example.app1.entities.Horse;
import com.example.app1.enums.BetType;
import com.example.app1.enums.Result;
import com.example.app1.repositories.BetRepository;
import com.example.app1.repositories.RaceRepository;
import com.example.app1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BetService {
    @Autowired
    private BetRepository betRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HorseService horseService;
    @Autowired
    private RaceService raceService;

    public void save(Bets bets) {
        betRepository.save(bets);
    }

    public List<Bets> getAllBets() {
        return betRepository.findAll();
    }

    public void deleteAll() {
        betRepository.deleteAll();
    }


    public void voteForHourse(Long race_ID, Long user_ID, BetType betType, Integer amount, Double coefficient, Long hourse_Id) {
        Optional<Races> races = raceRepository.findById(race_ID);
        Optional<User> user = userRepository.findById(user_ID);
        Horse horse = horseService.findById(hourse_Id);
        horse.setVote(horse.getVote() + 1);
        horse.setBetSum(horse.getBetSum() + amount);
        Bets bet = Bets.builder()
                .user(null)
                .betType(betType)
                .amount(Double.valueOf(amount))
                .coefficient(coefficient)
                .race(null)
                .horse(horse)
                .build();
        user.ifPresent(bet::setUser);
        races.ifPresent(bet::setRace);
        horseService.save(horse);
        betRepository.save(bet);


    }


    public List<Bets> getCurrentUserBets(String username) {

        return betRepository.findByUserUsername(username);
    }

    public void FinishFirstRace() {
        List<Races> races = raceService.getAllRace();
        for (Races race : races) {
            if (race.isActive()){
                Horse winner = race.getWinner();
                Horse loser = race.getLast();
                List<Bets> bets = betRepository.findAll();
                for (Bets bet : bets) {

                    if(bet.getRace().getId() == race.getId()){



                        if (bet.getBetType() == BetType.LAST){
                            if (bet.getHorse() == loser){
                                userService.getUserById(bet.getUser().getId()).setBalance(userService.getUserById(bet.getUser().getId()).getBalance() + (bet.getCoefficient() * bet.getAmount() *0.1));
                                bet.setResult(Result.WIN);
                            }else {
                                bet.setResult(Result.LOSE);
                            }
                        }else{
                            if (bet.getBetType() == BetType.WINNER){
                                if (bet.getHorse() == winner){
                                    userService.getUserById(bet.getUser().getId()).setBalance(userService.getUserById(bet.getUser().getId()).getBalance() + (bet.getCoefficient() * bet.getAmount()));
                                    bet.setResult(Result.WIN);
                                }else {
                                    bet.setResult(Result.LOSE);
                                }
                            }
                        }
                    }
                }
                race.setActive(false);
                raceRepository.save(race);
            }
        }
    }


}
