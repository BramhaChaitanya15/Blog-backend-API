package com.company.blog.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentDto {
	
	private Integer Id;
	private String content;
	private UserDto user;

}
