package com.example.app1.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;


@Entity
@Table(name = "horse")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Horse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    Integer age;

    @Column(columnDefinition = "integer default 0")
    Integer wins;

    @Column(columnDefinition = "integer default 0")
    Integer race;

    @Column
    String name;

    @Column(columnDefinition = "integer default 0")
    Integer vote;

    @Column(columnDefinition = "integer default 0")
    Integer betSum;

    @Column
    boolean inTheRace;


}
