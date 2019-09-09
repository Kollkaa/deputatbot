package org.com.deputatbot.service;

import org.com.deputatbot.domain.City;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CityService {

    City addCity(City city);
    void delete(long id);
    City getByName(String name);
    City editCity(City city);
    List<City> getAll();

}