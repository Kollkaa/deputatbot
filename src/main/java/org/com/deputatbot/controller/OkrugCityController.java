package org.com.deputatbot.controller;

import org.com.deputatbot.domain.*;
import org.com.deputatbot.repos.DeputatRepo;
import org.com.deputatbot.repos.OkrugCityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/okrugcity")
public class OkrugCityController {
    @Autowired
    OkrugCityRepo okrugCityRepo;

    @Autowired
    private DeputatRepo deputatRepo;

    @GetMapping
    public String main( Model model,Integer number) {
        Iterable<OkrugCity> messages = okrugCityRepo.findAll();

        if (number != null ) {
            messages = okrugCityRepo.findAllByNumber(number);
        } else {
            messages = okrugCityRepo.findAll();
        }

        model.addAttribute("okrugs", okrugCityRepo.findAll());

        model.addAttribute("number",number);
        return "okrugCity";
    }




    @GetMapping("{okrugcity}")
    public String editorcity(@PathVariable  OkrugCity okrugcity, Model model) {

        List<Partia> partias=new ArrayList<>();
        for (Partia r:Partia.values())
        {partias.add(r);}
        model.addAttribute("okrugcity", okrugcity);
        model.addAttribute("deputats",deputatRepo);
        model.addAttribute("partias", partias);

        return "editorCity";
    }


    @PostMapping
    public String SaveCity(   @RequestParam String deputatname,
                             @RequestParam String deputatsurname,
                             @RequestParam String deputatpartional,
                             @RequestParam Partia partis,
                             @RequestParam("okrugcityId") OkrugCity okrugCity ) {
        Deputat deputat=deputatRepo.findByOkrugCity(okrugCity);
        deputat.setName(deputatname);
        deputat.setSurname(deputatsurname);
        deputat.setPartion(deputatpartional);
        deputat.setPartia(partis);
        deputatRepo.saveAndFlush(deputat);
        return "redirect:/okrugcity";
    }


}

