package org.com.deputatbot.domain;

import javax.persistence.*;
@Entity
public class OkrugObl {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer number;

    public Integer getId() {
        return id;
    }

    private String region;

    @OneToOne
    @JoinColumn(name = "okrug_id")
    private Deputat deputat;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Deputat getDeputat() {
        return deputat;
    }

    public void setDeputat(Deputat deputat) {
        this.deputat=deputat;
    }


    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer    getNumber() {
        return number;
    }

}
