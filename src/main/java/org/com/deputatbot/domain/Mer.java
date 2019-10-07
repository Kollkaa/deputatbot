package org.com.deputatbot.domain;

import javax.persistence.*;

@Entity
public class Mer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private String partion;

    public Long getId() {
        return id;
    }

    public Mer() {
    }

    public Mer(String name, String surname, String partion) {
        this.name = name;
        this.surname = surname;
        this.partion = partion;
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
