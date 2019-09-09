package org.com.deputatbot.controller;

import org.com.deputatbot.domain.OkrugCity;
import org.com.deputatbot.repos.OkrugCityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OkrugCityController {
    @Autowired
    OkrugCityRepo okrugCityRepo;
    @GetMapping("/okrugcity")
    public String okrugcity(@RequestParam(defaultValue = "",required = false)String filter, Model model)
    {
        Iterable<OkrugCity> okrugCities=okrugCityRepo.findAll();
        if (!filter.isEmpty()&&filter!=null)
        {
            okrugCities=okrugCityRepo.findAllByCity_Name(filter);
        }else
        {
            okrugCities=okrugCityRepo.findAll();
        }
        model.addAttribute("okrugs",okrugCities);
        model.addAttribute("filter",filter);
        return "okrugcity";
    }
}
