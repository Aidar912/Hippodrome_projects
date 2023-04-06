package com.example.app1.repositories;

import com.example.app1.entities.Bets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface BetRepository extends JpaRepository<Bets, Long> {

    List<Bets> findByUserUsername(String username);

}
