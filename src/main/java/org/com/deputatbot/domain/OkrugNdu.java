package org.com.deputatbot.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class OkrugNdu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer number;

    public Long getId() {
        return id;
    }

    @OneToOne
   @JoinColumn(name = "okrug_id")
    private Deputat deputat;

   private String region;

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
