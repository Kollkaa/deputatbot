package org.com.deputatbot.service;

import org.com.deputatbot.domain.City;
import org.com.deputatbot.domain.Dilnizia;

import java.util.List;

public interface DilniziaService {
    Dilnizia addDilnizia(Dilnizia dilnizia);
    void delete(long id);
    Dilnizia getByNumber(Long name);
    Dilnizia editDilnizia(Dilnizia dilnizia);
    List<Dilnizia> getAll();

}
