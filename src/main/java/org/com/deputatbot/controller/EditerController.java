package org.com.deputatbot.controller;

import org.com.deputatbot.domain.*;
import org.com.deputatbot.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class EditerController {

    @Autowired
    private DeputatRepo deputatRepo;
    @Autowired
    private DilniziaRepo dilniziaRepo;
    @Autowired
    private OkrugNduRepo okrugNduRepo;
    @Autowired
    private OkrugOblRepo okrugOblRepo;
    @Autowired
    private OkrugCityRepo okrugCityRepo;

    @GetMapping("editer")
    public String editer(@RequestParam(required = false, defaultValue = " ") Long filter, Model model) {
        Iterable<Dilnizia> dilnizias = dilniziaRepo.findAll();

        if (filter != null) {
            dilnizias = dilniziaRepo.findAllByNumber(filter);
        } else {
            dilnizias = dilniziaRepo.findAll();
        }

        model.addAttribute("dilnizias", dilnizias);
        model.addAttribute("number", filter);

        return "editer";
    }


}