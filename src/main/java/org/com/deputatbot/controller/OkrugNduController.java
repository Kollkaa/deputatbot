package org.com.deputatbot.controller;

import org.com.deputatbot.domain.OkrugNdu;
import org.com.deputatbot.repos.DeputatRepo;
import org.com.deputatbot.repos.DilniziaRepo;
import org.com.deputatbot.repos.OkrugNduRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OkrugNduController {

    @Autowired
    OkrugNduRepo okrugNduRepo;
    @Autowired
    DilniziaRepo dilniziaRepo;
    @Autowired
    DeputatRepo deputatRepo;
    @GetMapping("/okrugndu")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {


        Iterable<OkrugNdu> okrugs = okrugNduRepo.findAll();

        if (filter != null && !filter.isEmpty()) {
            okrugs = okrugNduRepo.findAll();
        } else {
            okrugs = okrugNduRepo.findAll();
        }

        model.addAttribute("okrugs", okrugs);
        model.addAttribute("filter", filter);

        return "okrugndu";
    }
}
