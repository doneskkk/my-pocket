package com.donesk.moneytracker.service;

import com.donesk.moneytracker.repository.RoleRepo;
import com.donesk.moneytracker.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepo roleRepo;

    public Role getUserRole(){
        return roleRepo.findByName("ROLE_USER").get();
    }
}
