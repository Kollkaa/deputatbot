package org.com.deputatbot.domain;

import javax.persistence.Entity;


public enum TypeOk {
    OBLAST("депутат обласної ради"),
    NDY("народний депутат України"),
    CITY("міський депутат");


     private String title;

     TypeOk(String title){this.title=title;}
     public String GetTypeOkrug(){return title;}


}
