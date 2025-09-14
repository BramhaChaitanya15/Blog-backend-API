package com.company.blog.services;

import java.util.List;

import com.company.blog.payloads.UpdateUserCredentialsDto;
import com.company.blog.payloads.UpdateUserDto;
import com.company.blog.payloads.UserDto;

public interface UserService {
	
	//register
	UserDto registerNewUser(UserDto userDto);

	// create
	UserDto createUser(UserDto userDto);

	// update
	UserDto updateUser(UpdateUserDto userDto, Integer userId);
	
	// update passsword and username
	UserDto updateUserCredentials(UpdateUserCredentialsDto userDto, Integer userId);

	// fetch by id
	UserDto getUserById(Integer userId);

	// fetch all
	List<UserDto> getAllUsers();

	// delete
	void deleteUser(Integer userId);

}
