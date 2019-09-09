package org.com.deputatbot.controller;


import org.com.deputatbot.domain.OkrugObl;
import org.com.deputatbot.repos.OkrugOblRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class OkrugOblController {

    @Autowired
    private OkrugOblRepo okrugOblRepo;

    @GetMapping("/okrugobl")
    public  String okrugobl(@RequestParam(required = false,defaultValue = "")String filter, Model model)
    {
        Iterable<OkrugObl> okrugObls=okrugOblRepo.findAll();
        if (!filter.isEmpty()&&filter!=null)
        {
            okrugObls=okrugOblRepo.findAllByNumber(Integer.valueOf(filter));
        }else
        {
            okrugObls=okrugOblRepo.findAll();
        }
        model.addAttribute("okrugs",okrugObls);
        model.addAttribute("filter",filter);


        return "okrugobl";
    }
}
