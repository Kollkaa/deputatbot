package org.com.deputatbot.repos;


import org.com.deputatbot.domain.Deputat;
import org.com.deputatbot.domain.OkrugCity;
import org.com.deputatbot.domain.OkrugNdu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OkrugNduRepo extends JpaRepository<OkrugNdu, Long> {
    OkrugNdu findByNumber(Integer number);
    OkrugNdu findByDeputat(Deputat deputat);
    List<OkrugNdu> findByOrderByRegion();
}
