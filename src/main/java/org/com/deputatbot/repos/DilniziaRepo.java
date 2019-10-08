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
    List<Dilnizia> findAllByOkrugNdu(OkrugNdu okrugNdu);
    List<Dilnizia> findAllByOkrugObl(OkrugObl okrugObl);
    List<Dilnizia> findAllByOkrugCity(OkrugCity okrugCity);
    Dilnizia findByNumber (Integer number);

}
