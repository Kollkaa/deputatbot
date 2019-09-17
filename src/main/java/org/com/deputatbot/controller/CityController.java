package org.com.deputatbot.controller;

import org.com.deputatbot.domain.*;
import org.com.deputatbot.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cities")
public class CityController {




        @Autowired
        private MerRepo merRepo;
        @Autowired
        private CityRepo cityRepo;

        @GetMapping
        public String main( Model model) {
            model.addAttribute("cities", cityRepo.findAll());
            return "cities";
        }




        @GetMapping("{cities}")
        public String editorcities(@PathVariable City cities, Model model) {

            model.addAttribute("city", cities);

            return "editorcities";
        }


        @PostMapping
        public String SaveCities(   @RequestParam String mername,
                                 @RequestParam String mersurname,
                                 @RequestParam String merpartional,
                                 @RequestParam("cityId") City city ) {
            Mer mer=new Mer();
            mer= merRepo.findById(city.getMer().getId()).get();
            mer.setName(mername);
            mer.setSurname(mersurname);
            mer.setPartion(merpartional);
            merRepo.saveAndFlush(mer);
            city.setMer(mer);
            cityRepo.saveAndFlush(city);





            return "redirect:/cities";
        }


    }


