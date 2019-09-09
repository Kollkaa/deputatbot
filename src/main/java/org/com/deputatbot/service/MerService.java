package org.com.deputatbot.service;

import org.com.deputatbot.domain.City;
import org.com.deputatbot.domain.Mer;

import java.util.List;

public interface MerService {
    Mer addMer(Mer mer);
    void delete(long id);
    Mer getByName(String name);
    Mer editMer(Mer bank);
    List<Mer> getAll();

}
