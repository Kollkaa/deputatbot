package org.com.deputatbot.controller;

import org.com.deputatbot.domain.Dilnizia;
import org.com.deputatbot.domain.OkrugCity;
import org.com.deputatbot.domain.OkrugNdu;
import org.com.deputatbot.domain.OkrugObl;
import org.com.deputatbot.repos.DilniziaRepo;
import org.com.deputatbot.repos.OkrugCityRepo;
import org.com.deputatbot.repos.OkrugNduRepo;
import org.com.deputatbot.repos.OkrugOblRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dilnizias")
public class DilniziaController {

    @Autowired
    private   DilniziaRepo dilniziaRepo;
    @Autowired
    private OkrugNduRepo okrugNduRepo;
    @Autowired
    private OkrugOblRepo okrugOblRepo;
    @Autowired
    private OkrugCityRepo okrugCityRepo;
    @GetMapping
    public String dil(Model model)
    {
        model.addAttribute("dilnizias",dilniziaRepo.findAll());
        return "dilnizias";
    }
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
        return "dilnizias";
    }
    @GetMapping("/dilniziaf")
    public String dilniziaf(@RequestParam(required = false, defaultValue = "") Long number,
                            Model model)
    {
        Iterable<Dilnizia> dilnizias = dilniziaRepo.findAll();
        System.out.println(number);
        if (number != null ) {
            dilnizias = dilniziaRepo.findAllByNumber(Integer.valueOf(String.valueOf(number).split("\\.")[0]));
        } else {
            dilnizias = dilniziaRepo.findAll();
        }

        model.addAttribute("dilnizias", dilnizias);
        model.addAttribute("number", number);



        return "dilnizias";

    }
    @GetMapping("/dilniziaNdu")
    public String dilniziaNDu(@RequestParam(required = false, defaultValue = "") Integer number,
                            Model model)
    {
        Iterable<Dilnizia> dilnizias = dilniziaRepo.findAll();

        if (number != null ) {
            dilnizias = dilniziaRepo.findAllByOkrugNduNumber(number);


        } else {
            dilnizias = dilniziaRepo.findAll();
        }

        model.addAttribute("dilnizias", dilnizias);
        model.addAttribute("number", number);



        return "dilnizias";

    }
    @GetMapping("/dilniziaObl")
    public String dilniziaObl(@RequestParam(required = false, defaultValue = "") Integer number,
                            Model model)
    {
        Iterable<Dilnizia> dilnizias = dilniziaRepo.findAll();

        if (number != null ) {
            dilnizias = dilniziaRepo.findAllByOkrugOblNumber(number);
        } else {
            dilnizias = dilniziaRepo.findAll();
        }

        model.addAttribute("dilnizias", dilnizias);
        model.addAttribute("number", number);



        return "dilnizias";

    }
    @GetMapping("/dilniziaCity")
    public String dilniziaCity(@RequestParam(required = false, defaultValue = "") Integer number,
                            Model model)
    {
        Iterable<Dilnizia> dilnizias = dilniziaRepo.findAll();

        if (number != null ) {
            dilnizias = dilniziaRepo.findAllByOkrugCityNumber(number);
        } else {
            dilnizias = dilniziaRepo.findAll();
        }

        model.addAttribute("dilnizias", dilnizias);
        model.addAttribute("number", number);



        return "dilnizias";

    }
    @GetMapping("{dilnizia}")
    public String getDelnizia(@PathVariable Dilnizia dilnizia,Model model)
    {
        model.addAttribute("dilnizias",dilnizia);
        return "editorDilnizia";
    }
    @PostMapping
    public String editDilnizia(@RequestParam Integer ndunumber,
                               @RequestParam Integer oblnumber,
                               @RequestParam Integer citynumber,
                               @RequestParam String region,
                               @RequestParam("dilniziaId")Dilnizia dilnizia)
    {
        OkrugNdu okrugNdu=okrugNduRepo.findByNumber(ndunumber);
        OkrugObl okrugObl=okrugOblRepo.findByNumber(oblnumber);
        OkrugCity okrugCity=okrugCityRepo.findByNumber(citynumber);
        try {
            dilnizia.setRegion(region);
        }catch (Exception e){}
        try {
            dilnizia.setOkrugNdu(okrugNdu);
        }catch (Exception e){}
        try {
            dilnizia.setOkrugObl(okrugObl);
        }catch (Exception e){}
        try {
            dilnizia.setOkrugCity(okrugCity);
        }catch (Exception e){}
        dilniziaRepo.save(dilnizia);
        return "redirect:/dilnizias";
    }
}
