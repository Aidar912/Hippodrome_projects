package com.example.app1.controllers;

import com.example.app1.entities.Bets;
import com.example.app1.entities.Horse;
import com.example.app1.entities.Races;
import com.example.app1.entities.User;
import com.example.app1.enums.BetType;
import com.example.app1.repositories.HorseRepository;
import com.example.app1.repositories.RaceRepository;
import com.example.app1.repositories.UserRepository;
import com.example.app1.service.BetService;
import com.example.app1.service.HorseService;
import com.example.app1.service.RaceService;
import com.example.app1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class MainController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private BetService betService;

    @Autowired
    private HorseService horseService;
    @Autowired
    private HorseRepository horseRepository;
    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private RaceService raceService;

    @GetMapping("/")
    public String getMainPage(Model model, Authentication authentication) {
        if (userService.isAdmin(authentication)) {
            List<Bets> bets = betService.getAllBets();
            List<Horse> horse = horseService.getAllHorse();
            List<Races> race = raceService.getAllRace();
            model.addAttribute("bets", bets);
            model.addAttribute("horses", horse);
            model.addAttribute("betType", BetType.values());
            model.addAttribute("races", race);

            return "adminPage";
        } else {
            List<Bets> bets = betService.getAllBets();
            List<Bets> currentUserBets = betService.getCurrentUserBets(authentication.getName());
            List<Races> race = raceService.getAllRace();
            List<Horse> horses = horseService.getAllHorse();
            model.addAttribute("bet", bets);
            model.addAttribute("currentUser", userRepository.findByUsername(authentication.getName()));
            model.addAttribute("myBets", currentUserBets);
            model.addAttribute("races", race);
            model.addAttribute("horses", horses);
            model.addAttribute("betType", BetType.values());
            model.addAttribute("horseService",horseService);
            return "index";
        }
    }


    @PostMapping("/addBet")
    public String voteForHourse(@RequestParam Long horse_ID, @RequestParam Long raceId, @RequestParam Long userId, @RequestParam BetType betType, @RequestParam Integer amount, @RequestParam double coefficient, Model model) {
        try {
            if (userService.WithdrawingMoney(amount, userId)) {
                betService.voteForHourse(raceId, userId, betType, amount, coefficient, horse_ID);
                return "redirect:/";
            }else {

                return "redirect:/?message=Недостаточно средств";
            }

        } catch (Exception e) {

            return "redirect:/?message=Ошибка.Попробуйте снова!";
        }
    }

    @PostMapping("/addHorse")
    public String addHorse(@RequestParam String name, @RequestParam Integer age) {
        Horse horse = new Horse();
        horse.setName(name);
        horse.setAge(age);
        horse.setWins(0);
        horse.setRace(0);
        horse.setVote(0);
        horseService.save(horse);
        return "redirect:/";
    }


    @PostMapping("/addRace")
    public String addRace(@RequestParam("name") String name, @RequestParam("horseIds") List<Long> horseIds) {
        List<Horse> horses = horseRepository.findAllById(horseIds);
        Races race = new Races();
        race.setName(name);
        race.setHorses(horses);
        race.setActive(true);

        for (Horse horse: horses) {
            horse.setInTheRace(true);
            horse.setRace(horse.getRace() + 1);
            horseRepository.save(horse);
        }


        raceRepository.save(race);

        return "redirect:/";

    }

    @GetMapping("/finish")
    public String finish() {
        Races race = raceService.getAllRace().get(0);
        List<Horse> horses = race.getHorses();
        Random random = new Random();
        int winnerIndex = random.nextInt(horses.size());
        int lastIndex = random.nextInt(horses.size() - 1);
        if (lastIndex >= winnerIndex) {
            lastIndex++;
        }
        Horse winner = horses.get(winnerIndex);
        Horse last = horses.get(lastIndex);
        winner.setWins(winner.getWins() + 1);
        race.setWinner(winner);
        race.setLast(last);
        betService.FinishFirstRace();

        for (Horse horse : race.getHorses()) {
            horse.setInTheRace(false);
            horse.setVote(0);
            horse.setBetSum(0);

            horseRepository.save(horse);
        }

        return "redirect:/";
    }


    @PostMapping("/horses")
    @ResponseBody
    public List<Horse> getHorsesForRace(@RequestParam("raceId") Long raceId) {
        return horseService.getHorsesForRace(raceId);
    }

    @PostMapping("/coefficient/win")
    @ResponseBody
    public Double calculateCoefficientWin(@RequestParam("horseId") Long horseId) {
        return horseService.calculateCoefficientWin(horseId);
    }

    @PostMapping("/coefficient/lose")
    @ResponseBody
    public Double calculateCoefficientLose(@RequestParam("horseId") Long horseId) {
        return horseService.calculateCoefficientLose(horseId);
    }

    @PostMapping("/changebalance")
    public String changeBalance(@RequestParam double balance, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        user.setBalance(user.getBalance() + balance);
        userRepository.save(user);
        return "redirect:/";
    }
}

