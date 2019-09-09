package org.com.deputatbot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CityController {


    @GetMapping("/cities")
    public String cities()
    {

        return "cities";
    }
}
