package org.com.deputatbot.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;

@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
