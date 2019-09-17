package org.com.deputatbot.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;

@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "mer_id")
    private Mer mer;

    public Long getId() {
        return id;
    }

    public Mer getMer() {
        return mer;
    }

    public void setMer(Mer mer) {
        this.mer = mer;
    }

    @Enumerated(EnumType.STRING)
    private TypeCity typeCity;

    private String name;

    public String getTypeCity() {
        return typeCity.GetTitle();
    }

    public void setTypeCity(TypeCity typeCity) {
        this.typeCity = typeCity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
