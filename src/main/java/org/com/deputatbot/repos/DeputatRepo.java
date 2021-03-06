package org.com.deputatbot.repos;

import org.com.deputatbot.domain.Deputat;

import org.com.deputatbot.domain.OkrugCity;
import org.com.deputatbot.domain.OkrugObl;
import org.com.deputatbot.domain.Partia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeputatRepo extends JpaRepository<Deputat, Long> {
    List<Deputat> findAllByPartia(Partia partia);
    List<Deputat> findAllByName(String name);
    List<Deputat> findAllByOkrugCity(OkrugCity okrugCity);
    List<Deputat> findAllByOkrugObl(OkrugObl okrugObl);
    List<Deputat> findAllBySurnameContaining(String surname);
    Deputat findBySurname(String surname);

}
