package com.company.blog.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PostDto {

	private Integer postId;
	private String title;
	private String content;
	private String imageName = "default.png";
	private Date addedDate;
	private CategoryDto category;;
	private UserDto user;
	private Set<CommentDto> comments = new HashSet<>();

}
