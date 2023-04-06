package com.example.app1.entities;

import com.example.app1.enums.BetType;
import com.example.app1.enums.Result;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;


@Entity
@Table(name = "bets")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Bets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    User user;

    @Enumerated(EnumType.STRING)
    BetType betType;

    @Column
    Double amount;

    @Column
    Double coefficient;

    @ManyToOne
    Races race;

    @ManyToOne
    Horse horse;

    @Enumerated(EnumType.STRING)
    Result result;

}
