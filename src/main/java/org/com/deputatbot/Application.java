package org.com.deputatbot;

import org.com.deputatbot.domain.Role;
import org.com.deputatbot.domain.User;
import org.com.deputatbot.repos.UserRepo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application {
@Autowired
private UserRepo userRepo;


    public static void main (String...args) throws IOException {

        ApiContextInitializer.init();
        SpringApplication.run(Application.class, args);
        User user=new User();
        user.setUsername("obranetc");
        user.setPassword("2019");
        user.setActive(true);
        Set<Role> roles=new HashSet<>();
        roles.add(Role.ADMIN);
        roles.add(Role.USER);
        user.setRoles(new HashSet<Role>(){});

    }


}
