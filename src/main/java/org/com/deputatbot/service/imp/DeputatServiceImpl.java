package org.com.deputatbot.service.imp;

import org.com.deputatbot.domain.Deputat;
import org.com.deputatbot.repos.DeputatRepo;
import org.com.deputatbot.service.DeputatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeputatServiceImpl implements DeputatService {
    @Autowired
    private DeputatRepo deputatRepo;
    @Override
    public Deputat addDeputat(Deputat deputat) {
        Deputat savedDeputat=deputatRepo.saveAndFlush(deputat);
        return savedDeputat;
    }

    @Override
    public void delete(long id) {
            deputatRepo.deleteById(id);
    }

    @Override
    public Deputat getBySurname(String surname) {
        return deputatRepo.findBySurname(surname);
    }

    @Override
    public Deputat editDeputat(Deputat deputat) {
        return deputatRepo.saveAndFlush(deputat);
    }

    @Override
    public List<Deputat> getAll() {
        return deputatRepo.findAll();
    }
}
