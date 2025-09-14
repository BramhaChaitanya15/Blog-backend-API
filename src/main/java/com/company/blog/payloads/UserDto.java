package com.company.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import com.company.blog.utils.UniqueUsername;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	private Integer userId;
	@NotEmpty
	private String name;
	@NotEmpty
	@Email
	private String email;
	@NotEmpty
	@UniqueUsername
	private String username;
	@NotEmpty
	@Size(min = 8, max = 16, message = "password must be atleast of 8 characters and maximum of 16 characters")
	private String password;
	private String about;
	private String profileImage = "userdefault.png";
	private Set<RoleDto> roles = new HashSet<>();

	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

}
