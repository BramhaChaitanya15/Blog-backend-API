package com.company.blog.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.blog.entities.User;
import com.company.blog.exceptions.BadLoginCredentialsException;
import com.company.blog.payloads.JwtAuthRequest;
import com.company.blog.payloads.JwtAuthResponse;
import com.company.blog.payloads.UserDto;
import com.company.blog.security.JwtTokenHelper;
import com.company.blog.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	AuthController(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) {
		this.authenticationManager(request.getUsername(), request.getPassword());

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());

		String token = this.jwtTokenHelper.generateToken(userDetails);

		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		response.setUser(this.modelMapper.map((User) userDetails, UserDto.class));

		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);

	}

	private void authenticationManager(String username, String password) {
		UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(username, password);
		try {
			this.authenticationManager.authenticate(upat);
		} catch (BadCredentialsException e) {
			throw new BadLoginCredentialsException("Invalid Password!!");
		}
	}

	// register new user
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
		UserDto registeredUser = this.userService.registerNewUser(userDto);
		return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
	}

}
