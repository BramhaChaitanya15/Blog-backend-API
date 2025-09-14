package com.company.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.company.blog.entities.User;
import com.company.blog.exceptions.BadLoginCredentialsException;
import com.company.blog.repositories.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// load user from database
		User user = this.userRepository.findByUsername(username)
				.orElseThrow(() -> new BadLoginCredentialsException("User", "username", username));
		return user;
	}

}
