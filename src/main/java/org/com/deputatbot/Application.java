package org.com.deputatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.IOException;

@SpringBootApplication
public class Application {



    public static void main (String...args) throws IOException {

            ApiContextInitializer.init();
            SpringApplication.run(Application.class,args);


    }


}
