package org.com.deputatbot.bot;

import com.boot.lions.domain.User;
import com.boot.lions.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class Main
{
    @Autowired
    public UserRepo userRepo;

    public Main(){}

    public List<User> main()
    {
        for (User us:userRepo.findAll())
        {
            System.out.println(us.getUsername());
        }
        return userRepo.findAll();
    }

}
