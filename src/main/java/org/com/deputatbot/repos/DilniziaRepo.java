package org.com.deputatbot.repos;

import org.com.deputatbot.domain.Dilnizia;
import org.com.deputatbot.domain.OkrugCity;
import org.com.deputatbot.domain.OkrugNdu;
import org.com.deputatbot.domain.OkrugObl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DilniziaRepo extends JpaRepository<Dilnizia,Long> {
    List<Dilnizia> findAllByRegionContaining(String region);
    List<Dilnizia> findAllByNumber(Integer number);
    Dilnizia findByRegionContaining(String region);
    List<Dilnizia> findAllByOkrugNduNumber(Integer number);
    List<Dilnizia> findAllByOkrugOblNumber(Integer number);
    List<Dilnizia> findAllByOkrugCityNumber(Integer number);
    Dilnizia findByNumber (Integer number);

}
