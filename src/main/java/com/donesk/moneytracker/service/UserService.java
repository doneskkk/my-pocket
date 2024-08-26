package com.donesk.moneytracker.service;

import com.donesk.moneytracker.model.User;
import com.donesk.moneytracker.exception.UserNotFoundException;
import com.donesk.moneytracker.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private  UserRepo userRepo;
    private  PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Optional<User> findByUsername(String username){
        return userRepo.findByUsername(username);
    }

    public User getUser(Long id){
        return userRepo.findById(id).orElseThrow( () -> new UserNotFoundException("User with that id "+ id +" wasn't found"));
    }



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("The user '%s' wasn't found", username)
        ));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority("fds")).collect(Collectors.toList())

        );
    }

//    public User createUser(RegistrationUserDto registrationUserDto){
//        User user = new User();
////        if(registrationUserDto.getPassword() != registrationUserDto.getConfirmPassword()) {
////            throw new R
////        }
//        user.setUsername(registrationUserDto.getUsername());
//        user.setEmail(registrationUserDto.getEmail());
//        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
//        user.setRoles(List.of(roleService.getUserRole()));
//        return userRepo.save(user);
//    }

}