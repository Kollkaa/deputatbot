package org.com.deputatbot.repos;


import org.com.deputatbot.domain.Deputat;
import org.com.deputatbot.domain.OkrugCity;
import org.com.deputatbot.domain.OkrugObl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OkrugOblRepo extends JpaRepository<OkrugObl,Integer> {
   OkrugObl findByNumber(Integer number);
   List<OkrugObl> findAllByNumber(Integer number);
   OkrugObl findByRegionContaining(String dilnizia);

}
