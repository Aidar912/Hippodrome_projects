package com.example.app1.service;

import com.example.app1.entities.User;
import com.example.app1.enums.UserRoles;
import com.example.app1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(String name, String username, String password) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public boolean isAdmin(Authentication authentication) {
        String username=authentication.getName();
        UserRoles role= userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("not found "+username)).getRole();
        return role == UserRoles.ADMIN;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean WithdrawingMoney(double amount , Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.get().getBalance() >= amount){
            user.get().setBalance(user.get().getBalance() - amount);
            return true;
        }else{
            return false;
        }

    }



}
