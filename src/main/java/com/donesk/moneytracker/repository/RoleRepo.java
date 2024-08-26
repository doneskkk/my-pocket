package com.donesk.moneytracker.repository;

import com.donesk.moneytracker.model.ERole;
import com.donesk.moneytracker.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
