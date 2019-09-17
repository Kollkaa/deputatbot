package org.com.deputatbot.repos;


import org.com.deputatbot.domain.City;
import org.com.deputatbot.domain.Mer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MerRepo extends JpaRepository<Mer, Long> {

    List<Mer>findAllByName(String name);

}
