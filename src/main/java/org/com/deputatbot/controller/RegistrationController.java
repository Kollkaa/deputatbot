package org.com.deputatbot.controller;


import org.com.deputatbot.domain.Role;
import org.com.deputatbot.domain.User;
import org.com.deputatbot.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/login";
    }
}
/*
spring.datasource.url=jdbc:postgres://kmfatvgrousnuc:981a06c8e2e66ad9b752450442fd5e6254021a8bcf4bb750941519d44a8802a5@ec2-54-217-234-157.eu-west-1.compute.amazonaws.com:5432/d6ja97ahuklicl

spring.datasource.username=kmfatvgrousnuc
spring.datasource.password=981a06c8e2e66ad9b752450442fd5e6254021a8bcf4bb750941519d44a8802a5
spring.jpa.generate-ddl=true

 */