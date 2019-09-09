package org.com.deputatbot.domain;

public enum TypeOk {
    OBLAST("депутат обласної ради"),
    NDY("народний депутат України"),
    CITY("міський депутат");


     private String title;

     TypeOk(String title){this.title=title;}
     public String GetTypeOkrug(){return title;}


}
