package com.company.blog.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.blog.entities.Comment;
import com.company.blog.entities.Post;
import com.company.blog.entities.User;
import com.company.blog.exceptions.ResourceNotFoundException;
import com.company.blog.payloads.CommentDto;
import com.company.blog.repositories.CommentRepository;
import com.company.blog.repositories.PostRepository;
import com.company.blog.repositories.UserRepository;
import com.company.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		Comment comment = this.dtoToComment(commentDto);
		comment.setUser(user);
		comment.setPost(post);
		Comment savedComment = this.commentRepository.save(comment);
		return this.commentToDto(savedComment);
	}

	@Override
	public CommentDto updateComment(CommentDto commentDto, Integer id) {
		Comment comment = this.commentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "Comment Id", id));
		comment.setContent(commentDto.getContent());
		Comment savedComment = this.commentRepository.save(comment);
		return this.commentToDto(savedComment);
	}

	@Override
	public void deleteComment(Integer id) {
		Comment comment = this.commentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "Comment Id", id));
		this.commentRepository.delete(comment);
	}

	// convert dto to entity
	private Comment dtoToComment(CommentDto commentDto) {
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		return comment;
	}

	// converting entity to dto
	public CommentDto commentToDto(Comment comment) {
		CommentDto commentDto = this.modelMapper.map(comment, CommentDto.class);
		return commentDto;
	}

}
