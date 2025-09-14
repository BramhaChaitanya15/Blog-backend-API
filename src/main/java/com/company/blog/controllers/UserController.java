package com.company.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.blog.payloads.ApiResponse;
import com.company.blog.payloads.UpdateUserCredentialsDto;
import com.company.blog.payloads.UpdateUserDto;
import com.company.blog.payloads.UserDto;
import com.company.blog.services.FileService;
import com.company.blog.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private FileService fileService;
	@Autowired
	private ModelMapper modelMapper;

	@Value("${project.image}")
	private String path;

	// POST-create user
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto createUserDto = this.userService.createUser(userDto);
		return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
	}

	// PUT-update user
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserDto userDto, @PathVariable("userId") Integer uId) {
		UserDto updatedUser = this.userService.updateUser(userDto, uId);
		return ResponseEntity.ok(updatedUser);
	}

	@PutMapping("/credential/{userId}")
	public ResponseEntity<UserDto> updateUserCredentials(@RequestBody UpdateUserCredentialsDto dto,
			@PathVariable Integer userId) {
		UserDto update = this.userService.updateUserCredentials(dto, userId);
		return ResponseEntity.ok(update);
	}

	// DELETE-delete user
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid) {
		this.userService.deleteUser(uid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("user deleted successfully!", true), HttpStatus.OK);
	}

	// GET-get all user
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		return ResponseEntity.ok(this.userService.getAllUsers());
	}

	// GET-get single user
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getSingleUsers(@PathVariable("userId") Integer uid) {
		return ResponseEntity.ok(this.userService.getUserById(uid));
	}

	// POST-post image upload
	@PostMapping("/image/upload/{userId}")
	public ResponseEntity<UserDto> uploadPostImage(@RequestParam MultipartFile image, @PathVariable Integer userId)
			throws IOException {

		UserDto userDto1 = this.userService.getUserById(userId);

		UpdateUserDto updateUserDto = this.modelMapper.map(userDto1, UpdateUserDto.class);

//		String oldImageName = userDto.getProfileImage();

//		if (!oldImageName.equalsIgnoreCase("default.png")) {
//			this.fileService.deleteFile(path, oldImageName);
//		}

		String fileName = this.fileService.uploadimage(path, image);
		updateUserDto.setProfileImage(fileName);
		UserDto updateUser = this.userService.updateUser(updateUserDto, userId);

		return ResponseEntity.ok(updateUser);

	}

	// GET-serve files
	@GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = this.fileService.getResource(path, imageName);
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
