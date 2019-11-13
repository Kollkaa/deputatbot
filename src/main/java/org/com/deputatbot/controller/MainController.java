package org.com.deputatbot.controller;



import org.com.deputatbot.bot.Bot;

import org.com.deputatbot.bot.Parser;
import org.com.deputatbot.domain.Message;
import org.com.deputatbot.domain.Role;
import org.com.deputatbot.domain.User;
import org.com.deputatbot.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private Bot bot;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private DeputatRepo deputatRepo;
    @Autowired
    private OkrugNduRepo okrugNduRepo;
    @Autowired
    private DilniziaRepo dilniziaRepo;
    @Autowired
    private OkrugOblRepo okrugOblRepo;
    @Autowired
    private OkrugCityRepo okrugCityRepo;
    @Autowired
    private MerRepo merRepo;
    @Autowired
    private CityRepo cityRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) throws IOException {

        if (okrugNduRepo.findAll().size() > 1) {
        }else
        {
            Parser parser = new Parser();

            parser.allNduOkrug(okrugNduRepo, dilniziaRepo, deputatRepo);
            parser.ParserExelNDU(okrugNduRepo, deputatRepo);
            parser.ParserExelNDUKuev(okrugNduRepo, deputatRepo);
            parser.ParserExelOBL(okrugOblRepo, deputatRepo, dilniziaRepo);
            parser.ParserExelCITY(cityRepo, okrugCityRepo, deputatRepo, merRepo, dilniziaRepo);


            parser.ParserExelCITYKuev(cityRepo, okrugCityRepo, deputatRepo, merRepo, dilniziaRepo);

            User user = new User();
            user.setUsername("obranetc");
            user.setPassword("2019");
            user.setActive(true);
            Set<Role> roles = new HashSet<>();
            roles.add(Role.ADMIN);
            roles.add(Role.USER);
            user.setRoles(roles);
            userRepo.save(user);
        }
        return "app";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) throws IOException {


        Iterable<Message> messages = messageRepo.findAll();

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }




    @PostMapping("/add")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Map<String, Object> model
    ) throws IOException, TelegramApiException {
        Message message = new Message(text, tag, user);

       /* for (User us:userRepo.findAll())
        {
            if (us.isAdmin())
            {
                bot.execute(new SendMessage().setChatId(us.getChat_id()).setText(text));
                System.out.println(us.getUsername());
            }
        }
*/

        messageRepo.save(message);

        Iterable<Message> messages = messageRepo.findAll();

        model.put("messages", messages);
        return "redirect:/main";
    }
}