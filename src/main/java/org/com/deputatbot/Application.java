package org.com.deputatbot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application {



    public static void main (String...args) throws IOException {

            ApiContextInitializer.init();
            SpringApplication.run(Application.class,args);


        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                try {
                    Test();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                System.out.println("1111111111111111111111111111");
            }
        }, 0, 20, TimeUnit.MINUTES);



    }
    public static void Test()
    {


        String title = "";

        Document doc;
        try {
            doc = Jsoup.connect("https://deputatbot.herokuapp.com/").get();
            title = doc.title();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Jsoup Can read HTML page from URL, title : " + title);


    }


}
