package org.com.deputatbot.controller;

import org.com.deputatbot.domain.*;
import org.com.deputatbot.repos.DeputatRepo;
import org.com.deputatbot.repos.OkrugCityRepo;
import org.com.deputatbot.repos.OkrugNduRepo;
import org.com.deputatbot.repos.OkrugOblRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("deputats")
public class DeputatController {


    @Autowired
    DeputatRepo deputatRepo;
    @Autowired
    OkrugNduRepo okrugNduRepo;
    @Autowired
    OkrugCityRepo okrugCityRepo;
    @Autowired
    OkrugOblRepo okrugOblRepo;

    @GetMapping
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Deputat> deputats = deputatRepo.findAll();

        if (filter != null && !filter.isEmpty()) {
            deputats = deputatRepo.findAllBySurnameContaining(filter);
        } else {
            deputats = deputatRepo.findAll();
        }

        model.addAttribute("okrugOblRepo",okrugOblRepo);
        model.addAttribute("okrugCityRepo",okrugCityRepo);
        model.addAttribute("okrugNduRepo",okrugNduRepo);
        model.addAttribute("deputats", deputats);
        model.addAttribute("filter", filter);

        return "deputats";
    }
    @GetMapping("{deputats}")
    public String editordeputat(@PathVariable Deputat deputats, Model model) {

        if (okrugNduRepo.findByDeputat(deputats)!=null)
        {
            model.addAttribute("okrug",okrugNduRepo.findByDeputat(deputats));
        }
        else if (deputats.getOkrugObl()!=null)
        {
            model.addAttribute("okrug",deputats.getOkrugObl());
        }else if (deputats.getOkrugCity()!=null)
        {
            model.addAttribute("okrug",deputats.getOkrugCity());
        }
            List<TypeOk>typeOks=new ArrayList<>();
        for (TypeOk r:TypeOk.values())
        {typeOks .add(r);}
        List<Partia>partias=new ArrayList<>();
        for (Partia r:Partia.values())
        {partias.add(r);}
        model.addAttribute("typeOk", typeOks);
        model.addAttribute("partias", partias);
        Integer num=0;
        model.addAttribute("num",num);
        model.addAttribute("deputat", deputats);


        return "editorDeputat";
    }
    @PostMapping
    public String changeDep(@RequestParam String depname,
                            @RequestParam String depsurname,
                            @RequestParam String deppartional,
                            @RequestParam Integer number_okrug,
                            @RequestParam TypeOk typs,
                            @RequestParam Partia partis,
                            @RequestParam("deputatId") Deputat deputat)
    {
        if (typs==TypeOk.NDY)
        {
            OkrugNdu okrug= okrugNduRepo.findByDeputat(deputat);
            OkrugNdu okruf=okrugNduRepo.findByNumber(number_okrug);
            Deputat dep= okruf.getDeputat();
            okruf.setDeputat(deputat);
            okrug.setDeputat(dep);
            okrugNduRepo.save(okrug);
            okrugNduRepo.save(okruf);
        }
        if(typs==TypeOk.OBLAST)
        {
            OkrugObl okrugObl= deputat.getOkrugObl();


        }
        if(typs==TypeOk.CITY)
        {
            OkrugCity okrugCity= deputat.getOkrugCity();

        }
        deputat.setName(depname);
        deputat.setSurname(depsurname);
        deputat.setPartion(deppartional);
        deputat.setPartia(partis);
        deputat.setTypeOk(typs);
        deputatRepo.save(deputat);



        return "redirect:/deputats";
    }

    @PostMapping("addDep")
    public String AddDep( Deputat deputat,
                          @RequestParam String name,
                          @RequestParam String surname,
                          @RequestParam String partion,
                          @RequestParam Partia partia,
                          @RequestParam Long id)
    {


        deputat.setName(name);
        deputat.setSurname(surname);
        deputat.setPartion(partion);
        deputat.setPartia(partia);
        deputat.setOkrugCity(okrugCityRepo.findById(id).get());
        deputat.setTypeOk(TypeOk.CITY);
        deputatRepo.save(deputat);

        return "redirect:/deputats";
    }
}
