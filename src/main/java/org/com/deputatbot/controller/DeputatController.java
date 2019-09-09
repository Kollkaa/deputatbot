package org.com.deputatbot.controller;

import org.com.deputatbot.domain.Deputat;
import org.com.deputatbot.domain.Message;
import org.com.deputatbot.repos.DeputatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class DeputatController {


    @Autowired
    DeputatRepo deputatRepo;
    @GetMapping("/deputats")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Deputat> deputats = deputatRepo.findAll();

        if (filter != null && !filter.isEmpty()) {
            deputats = deputatRepo.findAllByName(filter);
        } else {
            deputats = deputatRepo.findAll();
        }

        model.addAttribute("deputats", deputats);
        model.addAttribute("filter", filter);

        return "deputats";
    }
}
