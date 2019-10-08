package org.com.deputatbot.domain;

import javax.persistence.*;

@Entity
public class Deputat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private String partion;
    private TypeOk typeOk;


    @Enumerated(EnumType.STRING)
    private Partia partia;

    @ManyToOne
    @JoinColumn(name = "okrugCity_id")
    private OkrugCity okrugCity;

    @ManyToOne
    @JoinColumn(name = "okrugObl_id")
    private OkrugObl okrugObl;


    public Deputat(){}

    public Deputat(String name, String surname, String partion) {
        this.name = name;
        this.surname = surname;
        this.partion = partion;
    }

    public org.com.deputatbot.domain.OkrugObl getOkrugObl() {
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

    public TypeOk getTypeOk() {
        return typeOk;
    }

    public void setTypeOk(TypeOk typeOk) {
        this.typeOk = typeOk;
    }

    public String getPartia() {
        return partia.GetPartiaName();
    }

    public void setPartia(Partia partia) {
        this.partia = partia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPartion() {
        return partion;
    }

    public void setPartion(String partion) {
        this.partion = partion;
    }
}
