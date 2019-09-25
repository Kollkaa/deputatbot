package org.com.deputatbot.repos;

import org.com.deputatbot.domain.City;
import org.com.deputatbot.domain.Deputat;
import org.com.deputatbot.domain.OkrugCity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OkrugCityRepo extends JpaRepository<OkrugCity,Integer> {
    OkrugCity findByNumber(Integer number);
    List<OkrugCity> findAllByNumber(Integer number);
    List<OkrugCity> findAllByCity_Name(String name);
    OkrugCity findByRegionContaining(String dilnizia);
    OkrugCity findByDeputat(Deputat deputat);

}
