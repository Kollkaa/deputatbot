package org.com.deputatbot.controller;

import org.com.deputatbot.domain.Deputat;
import org.com.deputatbot.domain.OkrugNdu;
import org.com.deputatbot.domain.OkrugObl;
import org.com.deputatbot.domain.Partia;
import org.com.deputatbot.repos.DeputatRepo;
import org.com.deputatbot.repos.DilniziaRepo;
import org.com.deputatbot.repos.OkrugNduRepo;
import org.com.deputatbot.repos.OkrugOblRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "okrugobl";
    }




    @GetMapping("{okrugobl}")
    public String editorobl(@PathVariable OkrugObl okrugobl, Model model) {

        model.addAttribute("okrugobl", okrugobl);
        model.addAttribute("partias", Partia.values());
        return "editorobl";
    }


    @PostMapping
    public String SaveObl(   @RequestParam String deputatname,
                             @RequestParam String deputatsurname,
                             @RequestParam String deputatpartional,
                             @RequestParam("okrugId") OkrugObl okrugObl ) {
        Deputat deputat=new Deputat();
        deputat= deputatRepo.findById(okrugObl.getDeputat().getId()).get();
        deputat.setName(deputatname);
        deputat.setSurname(deputatsurname);
        deputat.setPartion(deputatpartional);
        deputatRepo.saveAndFlush(deputat);
        okrugObl.setDeputat(deputat);
        okrugOblRepo.saveAndFlush(okrugObl);





        return "redirect:/okrugobl";
    }


}
