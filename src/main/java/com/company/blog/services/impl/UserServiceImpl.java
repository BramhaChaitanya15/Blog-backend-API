package com.company.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.blog.config.AppConstants;
import com.company.blog.entities.Role;
import com.company.blog.entities.User;
import com.company.blog.exceptions.DuplicateUsernameException;
import com.company.blog.exceptions.ResourceNotFoundException;
import com.company.blog.payloads.UpdateUserCredentialsDto;
import com.company.blog.payloads.UpdateUserDto;
import com.company.blog.payloads.UserDto;
import com.company.blog.repositories.RoleRepository;
import com.company.blog.repositories.UserRepository;
import com.company.blog.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto registerNewUser(UserDto userDto) {

		User user = this.dtoToUser(userDto);
		// encode password
		user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

		// role
		Role role = this.roleRepository.findById(AppConstants.NORMAL_USER).get();
		user.getRoles().add(role);
		
		if (this.userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateUsernameException("Username already exists.");
        }

		User newUser = this.userRepository.save(user);

		return this.userToDto(newUser);
	}

	@Override
	public UserDto createUser(UserDto userDto) {

		User user = this.dtoToUser(userDto);
		user.setPassword(this.bCryptPasswordEncoder.encode(userDto.getPassword()));
		User savedUser = this.userRepository.save(user);

		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UpdateUserDto userDto, Integer userId) {

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));

		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setAbout(userDto.getAbout());
		user.setProfileImage(userDto.getProfileImage());

		User updatedUser = this.userRepository.save(user);

		return this.modelMapper.map(updatedUser, UserDto.class);
	}

	@Override
	public UserDto getUserById(Integer userId) {

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));

		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {

		List<User> users = this.userRepository.findAll();
		List<UserDto> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());

		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		// Remove all role associations
		user.getRoles().clear();
		userRepository.save(user);

		this.userRepository.delete(user);

	}
	@Override
	public UserDto updateUserCredentials(UpdateUserCredentialsDto userDto, Integer userId) {
		
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		user.setUsername(userDto.getUsername());
		user.setPassword(this.bCryptPasswordEncoder.encode(userDto.getPassword()));
		
		User updatedUser = this.userRepository.save(user);
		
		return this.userToDto(updatedUser);
	}

	// convert dto to entity
	private User dtoToUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		return user;
	}

	// converting entity to dto
	public UserDto userToDto(User user) {
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		return userDto;
	}

}
