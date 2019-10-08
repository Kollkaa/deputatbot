package org.com.deputatbot.domain;

import javax.persistence.*;
@Entity
public class Dilnizia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer number;

    @Column(length=3000)
    private String region;

    @ManyToOne
    @JoinColumn(name = "okrugndu_id")
    private OkrugNdu okrugNdu;

    @ManyToOne
    @JoinColumn(name = "okrugobl_id")
    private OkrugObl okrugObl;

    @ManyToOne
    @JoinColumn(name = "okrugcity_id")
    private OkrugCity okrugCity;
    public Dilnizia(){}
    public Dilnizia(Integer number, String region)
    {
        this.number=number;
        this.region=region;
    }

    public Long getId() {
        return id;
    }

    public OkrugObl getOkrugObl() {
        return okrugObl;
    }

    public void setOkrugObl(OkrugObl okrugObl) {
        this.okrugObl = okrugObl;
    }

    public OkrugCity getOkrugCity() {
        return okrugCity;
    }

    public void setOkrugCity(OkrugCity okrugCity) {
        this.okrugCity = okrugCity;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public OkrugNdu getOkrugNdu() {
        return okrugNdu;
    }

    public void setOkrugNdu(OkrugNdu okrugNdu) {
        this.okrugNdu = okrugNdu;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }


}
