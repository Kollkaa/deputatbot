package org.com.deputatbot.controller;

import org.com.deputatbot.domain.Dilnizia;
import org.com.deputatbot.repos.DilniziaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EditerController {

    @Autowired
    private DilniziaRepo dilniziaRepo;
@GetMapping("/editer")
public String editer(@RequestParam(required = false, defaultValue = " ")Long filter, Model model)
{ Iterable<Dilnizia> dilnizias = dilniziaRepo.findAll();

    if (filter != null ) {
        dilnizias = dilniziaRepo.findAllByNumber(filter);
    } else {
        dilnizias = dilniziaRepo.findAll();
    }

    model.addAttribute("dilnizias", dilnizias);
    model.addAttribute("number", filter);

    return "editer";
}

}
