package org.com.deputatbot.service;

import org.com.deputatbot.domain.City;
import org.com.deputatbot.domain.Deputat;

import java.util.List;

public interface DeputatService {
    Deputat addDeputat(Deputat deputat);
    void delete(long id);
    Deputat getBySurname(String surname);
    Deputat editDeputat(Deputat deputat);
    List<Deputat> getAll();

}
