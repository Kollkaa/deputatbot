package org.com.deputatbot.domain;

import javax.persistence.*;
@Entity
public class OkrugObl {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer number;

    public Long getId() {
        return id;
    }

    private String region;



    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }




    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer    getNumber() {
        return number;
    }

}
