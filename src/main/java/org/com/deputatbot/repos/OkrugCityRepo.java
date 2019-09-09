package org.com.deputatbot.repos;

import org.com.deputatbot.domain.OkrugCity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OkrugCityRepo extends JpaRepository<OkrugCity,Long> {
    OkrugCity findByNumber(Integer number);
    List<OkrugCity> findAllByCity_Name(String name);
    OkrugCity findByRegionContaining(String dilnizia);
}
