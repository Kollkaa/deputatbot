package org.com.deputatbot.service;


import org.com.deputatbot.domain.OkrugNdu;

import java.util.List;

public interface OkrugCityService {
    OkrugNdu addOkrug(OkrugNdu okrugNdu);
    void delete(long id);
    OkrugNdu getByNumber(Integer number);
    OkrugNdu editOkrug(OkrugNdu bank);
    List<OkrugNdu> getAll();

}
