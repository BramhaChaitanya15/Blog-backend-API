package com.company.blog.utils;

import com.company.blog.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {

		if (username == null || username.isEmpty()) {
            return true;
        }
        return !userRepository.existsByUsername(username);
	}

}
