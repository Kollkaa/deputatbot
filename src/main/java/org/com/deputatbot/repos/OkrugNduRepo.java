package org.com.deputatbot.repos;


import org.com.deputatbot.domain.OkrugNdu;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OkrugNduRepo extends JpaRepository<OkrugNdu, Long> {
    OkrugNdu findByNumber(Integer number);
}
