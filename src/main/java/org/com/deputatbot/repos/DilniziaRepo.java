package org.com.deputatbot.repos;

import org.com.deputatbot.domain.Dilnizia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DilniziaRepo extends JpaRepository<Dilnizia,Long> {
    List<Dilnizia> findAllByRegionContaining(String name);
    List<Dilnizia> findAllByNumber(Long name);
}
