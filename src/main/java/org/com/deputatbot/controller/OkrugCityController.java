package org.com.deputatbot.controller;

import org.com.deputatbot.domain.Deputat;
import org.com.deputatbot.domain.OkrugCity;
import org.com.deputatbot.domain.OkrugNdu;
import org.com.deputatbot.domain.Partia;
import org.com.deputatbot.repos.DeputatRepo;
import org.com.deputatbot.repos.DilniziaRepo;
import org.com.deputatbot.repos.OkrugCityRepo;
import org.com.deputatbot.repos.OkrugNduRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/okrugcity")
public class OkrugCityController {
    @Autowired
    OkrugCityRepo okrugCityRepo;

    @Autowired
    private DeputatRepo deputatRepo;

    @GetMapping
    public String main( Model model) {
        model.addAttribute("okrugs", okrugCityRepo.findAll());
        return "okrugcity";
    }



//
    @GetMapping("{okrugcity}")
    public String editorcity(@PathVariable  OkrugCity okrugcity, Model model) {

        model.addAttribute("okrugcity", okrugcity);
        model.addAttribute("partias", Partia.values());

        return "editorcity";
    }


    @PostMapping
    public String SaveCity(   @RequestParam String deputatname,
                             @RequestParam String deputatsurname,
                             @RequestParam String deputatpartional,
                             @RequestParam("okrugcityId") OkrugCity okrugCity ) {
        Deputat deputat=new Deputat();
        deputat= deputatRepo.findById(okrugCity.getDeputat().getId()).get();
        deputat.setName(deputatname);
        deputat.setSurname(deputatsurname);
        deputat.setPartion(deputatpartional);
        deputatRepo.saveAndFlush(deputat);
        okrugCity.setDeputat(deputat);
        okrugCityRepo.saveAndFlush(okrugCity);





        return "redirect:/okrugcity";
    }


}

