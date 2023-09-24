package com.donesk.moneytracker.exception;

import com.donesk.moneytracker.entity.User;
import com.donesk.moneytracker.repository.UserRepo;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private final UserRepo userRepo;

    public UserValidator(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        Optional<User> foundUser = userRepo.findByEmail(user.getEmail());
        if (foundUser.isPresent()) {
            errors.rejectValue("email", "", "This email is already exists");
        }
    }
}
