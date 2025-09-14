package com.company.blog.services;

import com.company.blog.payloads.CommentDto;

public interface CommentService {

	//create comment
	CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId);

	//edit comment
	CommentDto updateComment(CommentDto commentDto, Integer id);

	//delete comment
	void deleteComment(Integer id);

}
