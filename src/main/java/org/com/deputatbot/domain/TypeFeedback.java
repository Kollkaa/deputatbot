package org.com.deputatbot.domain;

public enum TypeFeedback {



notFound("не знайшов що шукав"),
    notCorect("не правильно видає інформацію"),
        likeBot("подобається бот"),
            anythingFeedback("інша відповідь");

    private String feedback;
    public String GetTypeFeedback()
    {
        return this.feedback;
    }
    TypeFeedback(String feedback) {
        this.feedback = feedback;
    }
}
