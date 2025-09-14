package com.company.blog.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateUserDto {
	
	private Integer userId;
	private String name;
	private String email;
	private String about;
	private String profileImage = "userdefault.png";

}
