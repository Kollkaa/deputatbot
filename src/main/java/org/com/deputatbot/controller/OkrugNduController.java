package org.com.deputatbot.controller;

import org.com.deputatbot.domain.Deputat;
import org.com.deputatbot.domain.OkrugNdu;
import org.com.deputatbot.domain.Partia;
import org.com.deputatbot.repos.DeputatRepo;
import org.com.deputatbot.repos.DilniziaRepo;
import org.com.deputatbot.repos.OkrugNduRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/okrugndu")
public class OkrugNduController {

    @Autowired
    private DeputatRepo deputatRepo;
    @Autowired
    private DilniziaRepo dilniziaRepo;
    @Autowired
    private OkrugNduRepo okrugNduRepo;

    @GetMapping
    public String main( Model model) {
        model.addAttribute("okrugs", okrugNduRepo.findAll());
        return "okrugNdu";
    }




    @GetMapping("{okrugndu}")
    public String editorndu(@PathVariable OkrugNdu okrugndu, Model model) {

        List<Partia> partias=new ArrayList<>();
        for (Partia r:Partia.values())
        {partias.add(r);}
        model.addAttribute("okrugndu", okrugndu);
        model.addAttribute("partis", partias);
        return "editorNdu";
    }


    @PostMapping
    public String SaveNdu(   @RequestParam String deputatname,
                             @RequestParam String deputatsurname,
                             @RequestParam String deputatpartional,
                             @RequestParam Partia partias,

                             @RequestParam("okrugId") OkrugNdu okrugNdu ) {
        Deputat deputat=new Deputat();
        deputat= deputatRepo.findById(okrugNdu.getDeputat().getId()).get();
        deputat.setName(deputatname);
        deputat.setSurname(deputatsurname);
        deputat.setPartion(deputatpartional);
        deputat.setPartia(partias);
        deputatRepo.saveAndFlush(deputat);
        okrugNdu.setDeputat(deputat);
        okrugNduRepo.saveAndFlush(okrugNdu);





        return "redirect:/okrugndu";
    }


}
