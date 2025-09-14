package com.company.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.blog.payloads.ApiResponse;
import com.company.blog.payloads.CommentDto;
import com.company.blog.services.CommentService;

@RestController
@RequestMapping("/api/v1")
public class CommentController {

	@Autowired
	private CommentService commentService;

	// POST-create comment
	@PostMapping("/post/{postId}/user/{userId}/comment")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, @PathVariable Integer postId,
			@PathVariable Integer userId) {
		CommentDto createdComment = this.commentService.createComment(commentDto, postId, userId);
		return new ResponseEntity<CommentDto>(createdComment, HttpStatus.CREATED);
	}

	// PUT-edit comment
	@PutMapping("/comment/{id}")
	public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto, @PathVariable Integer id) {
		CommentDto updatedComment = this.commentService.updateComment(commentDto, id);
		return new ResponseEntity<CommentDto>(updatedComment, HttpStatus.OK);
	}

	// DELETE-delete comment
	@DeleteMapping("/comment/{id}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer id) {
		this.commentService.deleteComment(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("comment deleted successfully!", true), HttpStatus.OK);
	}

}
