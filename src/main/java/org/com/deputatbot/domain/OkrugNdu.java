package org.com.deputatbot.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class OkrugNdu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer number;
    @OneToOne
    @JoinColumn(name = "okrug_id")
    private Deputat deputat;

    @Column( length = 1000000 )
    private String region;


    public Integer getId() {
        return id;
    }


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



    public Integer getNumber() {
        return number;
    }


}
