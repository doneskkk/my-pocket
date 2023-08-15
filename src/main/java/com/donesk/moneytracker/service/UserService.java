package com.donesk.moneytracker.service;

import com.donesk.moneytracker.dto.DtoMapper;
import com.donesk.moneytracker.dto.UserDto;
import com.donesk.moneytracker.entity.User;
import com.donesk.moneytracker.exception.UserNotFoundException;
import com.donesk.moneytracker.repository.UserRepo;

public class UserService {

    private final DtoMapper dtoMapper;
    private final UserRepo userRepo;

    public UserService(DtoMapper dtoMapper, UserRepo userRepo) {
        this.dtoMapper = dtoMapper;
        this.userRepo = userRepo;
    }

    public User getUser(Long id){
        return userRepo.findById(id).orElseThrow( () -> new UserNotFoundException("User with that id "+ id +" wasn't found"));
    }

    public Long create(UserDto userDto){
        User userToSave = dtoMapper.convertToUser(userDto);
        userRepo.save(userToSave);
        return userToSave.getId();
    }

}