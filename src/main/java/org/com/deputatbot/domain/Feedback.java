package org.com.deputatbot.domain;

import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private String feedback;

    @Enumerated(EnumType.STRING)
    private  TypeFeedback typeFeedback;

    private LocalDate date;

    public String getDate() {
        return date.toString();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TypeFeedback getTypeFeedback() {
        return typeFeedback;
    }

    public void setTypeFeedback(TypeFeedback typeFeedback) {
        this.typeFeedback = typeFeedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
