package com.company.blog.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateUserCredentialsDto {
	
	private String username;
	private String password;
	
}
