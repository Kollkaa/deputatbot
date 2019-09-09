package org.com.deputatbot.service.imp;

import org.com.deputatbot.domain.City;
import org.com.deputatbot.repos.CityRepo;
import org.com.deputatbot.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepo cityRepo;
    @Override
    public City addCity(City city) {
        City savedCity= cityRepo.saveAndFlush(city);

        return savedCity;
    }

    @Override
    public void delete(long id) {
        cityRepo.deleteById(id);
    }

    @Override
    public City getByName(String name) {
        return cityRepo.findByName(name);    }

    @Override
    public City editCity(City city) {
        return cityRepo.saveAndFlush(city);
    }

    @Override
    public List<City> getAll() {
        return cityRepo.findAll();
    }
}
