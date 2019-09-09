package org.com.deputatbot.controller;

import org.com.deputatbot.bot.Parser;
import org.com.deputatbot.domain.Mer;
import org.com.deputatbot.repos.CityRepo;
import org.com.deputatbot.repos.MerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;


@Controller
public class MerController {

    @Autowired
    private MerRepo merRepo;
    @Autowired
    private CityRepo cityRepo;

    @GetMapping("/mer")
    public String mers(@RequestParam(required = false, defaultValue = "")String filter, Model model) throws IOException {

         Iterable<Mer> mers =merRepo.findAll();
         if (!filter.isEmpty()&&filter!=null)
         {
             mers=merRepo.findAllByCity_Name(filter);
         }else
         {
             mers=merRepo.findAll();
         }
        model.addAttribute("mers",mers);
         model.addAttribute("filter",filter);
        return "mer";
    }

}
