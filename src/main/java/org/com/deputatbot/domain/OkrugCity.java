package org.com.deputatbot.domain;

import javax.persistence.*;
@Entity
public class OkrugCity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer number;

    public Long getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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
