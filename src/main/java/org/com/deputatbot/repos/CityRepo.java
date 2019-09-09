package org.com.deputatbot.repos;

import org.com.deputatbot.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityRepo extends JpaRepository<City, Long> {
    List<City>findAllByName(String name);

    City findByName(String name);
}
