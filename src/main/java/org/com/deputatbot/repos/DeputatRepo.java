package org.com.deputatbot.repos;

import org.com.deputatbot.domain.Deputat;

import org.com.deputatbot.domain.Partia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeputatRepo extends JpaRepository<Deputat, Long> {
    List<Deputat> findAllByPartia(Partia partia);
    List<Deputat> findAllByName(String name);

    Deputat findBySurname(String surname);
}
