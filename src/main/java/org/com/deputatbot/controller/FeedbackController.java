package org.com.deputatbot.controller;

import org.checkerframework.common.reflection.qual.GetMethod;
import org.com.deputatbot.domain.TypeFeedback;
import org.com.deputatbot.repos.FeedbackRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackRepo feedbackRepo;
    @GetMapping
    public String allFeedback(Model model)
    {
        model.addAttribute("feedbacks",feedbackRepo.findAll());
        return "feedback";
    }

    @GetMapping("/notFound")
    public String NotFoundFeedback(Model model) {
        model.addAttribute("feedbacks",feedbackRepo.findAllByTypeFeedback(TypeFeedback.notFound));
        return "feedback";
    }
    @GetMapping("/notCorect")
    public String NotCorectFeedback(Model model) {
        model.addAttribute("feedbacks",feedbackRepo.findAllByTypeFeedback(TypeFeedback.notFound));
        return "feedback";
    }
    @GetMapping("/likeBot")
    public String LikeBotFeedback(Model model) {
        model.addAttribute("feedbacks",feedbackRepo.findAllByTypeFeedback(TypeFeedback.notFound));
        return "feedback";
    }
    @GetMapping("/anything")
    public String AbythingFeedback(Model model) {
        model.addAttribute("feedbacks",feedbackRepo.findAllByTypeFeedback(TypeFeedback.notFound));
        return "feedback";
    }


}
