//package com.donesk.moneytracker.exception;
//
//import com.donesk.moneytracker.model.User;
//import com.donesk.moneytracker.repository.UserRepo;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//
//import java.util.Optional;
//
//@Component
//public class UserValidator implements Validator {
//
//    private final UserRepo userRepo;
//
//    public UserValidator(UserRepo userRepo) {
//        this.userRepo = userRepo;
//    }
//
//    @Override
//    public boolean supports(Class<?> clazz) {
//        return User.class.equals(clazz);
//    }
//
//    @Override
//    public void validate(Object target, Errors errors) {
//        User user = (User) target;
//        Optional<User> foundUserByEmail = userRepo.findByEmail(user.getEmail());
//        Optional<User> foundUserByUsername = userRepo.findByUsername(user.getUsername());
//        if (foundUserByEmail.isPresent()) {
//            errors.rejectValue("email", "", "This email is already exists");
//        }
//
//        if (foundUserByUsername.isPresent()) {
//            errors.rejectValue("username", "", "This username is already exists");
//        }
//    }
//}
