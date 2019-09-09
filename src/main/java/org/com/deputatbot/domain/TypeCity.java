package org.com.deputatbot.domain;

public enum  TypeCity {
    city("місто"),
    country("сільська громада"),
    city_country("селищна громада"),
    city_all("міська громада");

    private String title;

    TypeCity(String title)
    {
        this.title=title;
    }

    public String GetTitle()
    {return title;}

}
