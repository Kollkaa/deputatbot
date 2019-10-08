package org.com.deputatbot.controller;

import org.com.deputatbot.domain.Deputat;
import org.com.deputatbot.domain.OkrugObl;
import org.com.deputatbot.domain.Partia;
import org.com.deputatbot.repos.DeputatRepo;
import org.com.deputatbot.repos.OkrugOblRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/okrugobl")
public class OkrugOblController {

    @Autowired
    private DeputatRepo deputatRepo;
    @Autowired
    private OkrugOblRepo okrugOblRepo;

    @GetMapping
    public String main( Model model) {
        model.addAttribute("okrugs", okrugOblRepo.findAll());
        model.addAttribute("repoDep",deputatRepo);
        return "okrugObl";
    }




    @GetMapping("{okrugobl}")
    public String editorobl(@PathVariable OkrugObl okrugobl, Model model) {

        List<Partia> partias=new ArrayList<>();
        for (Partia r:Partia.values())
        {partias.add(r);}
        model.addAttribute("okrugobl", okrugobl);
        model.addAttribute("partias", partias);
        model.addAttribute("deputats",deputatRepo);
        return "editorObl";
    }


    @PostMapping
    public String SaveObl(   @RequestParam String deputatname,
                             @RequestParam String deputatsurname,
                             @RequestParam String deputatpartional,
                             @RequestParam Partia partis,
                             @RequestParam("okrugId") OkrugObl okrugObl ) {
        List<Deputat> deputat=deputatRepo.findAllByOkrugObl(okrugObl);
        for (Deputat dep: deputat) {
            dep.setName(deputatname);
            dep.setSurname(deputatsurname);
            dep.setPartion(deputatpartional);
            dep.setPartia(partis);
            deputatRepo.saveAndFlush(dep);
        }
        return "redirect:/okrugobl";
    }


}
