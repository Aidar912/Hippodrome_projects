package com.example.app1.entities;


import com.example.app1.enums.UserRoles;
import com.sun.istack.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name="username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "balance")
    double balance;

    @Builder.Default
    private Boolean enabled=true;

    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    UserRoles role= UserRoles.USER;



}
