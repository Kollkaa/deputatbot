package org.com.deputatbot.controller;

import org.com.deputatbot.domain.Dilnizia;
import org.com.deputatbot.repos.DilniziaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DilniziaController {

    @Autowired
    private   DilniziaRepo dilniziaRepo;

    @GetMapping("/dilniziar")
    public String dilniziar(@RequestParam(required = false, defaultValue = "") String regions,Model model)
    {
        Iterable<Dilnizia> dilnizias = dilniziaRepo.findAll();
        if (regions != null && !regions.equals("")) {
            dilnizias = dilniziaRepo.findAllByRegionContaining(regions);
        }
        else {
            dilnizias = dilniziaRepo.findAll();
        }
        model.addAttribute("dilnizias", dilnizias);
        model.addAttribute("regions", regions);
        return "dilniziaf";
    }
    @GetMapping("/dilniziaf")
    public String dilniziaf(@RequestParam(required = false, defaultValue = "") Long number,
                            Model model)
    {
        Iterable<Dilnizia> dilnizias = dilniziaRepo.findAll();

        if (number != null ) {
            dilnizias = dilniziaRepo.findAllByNumber(number);
        } else {
            dilnizias = dilniziaRepo.findAll();
        }

        model.addAttribute("dilnizias", dilnizias);
        model.addAttribute("number", number);



        return "dilniziaf";

    }
}
