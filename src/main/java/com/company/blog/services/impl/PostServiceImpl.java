package com.company.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.company.blog.entities.Category;
import com.company.blog.entities.Post;
import com.company.blog.entities.User;
import com.company.blog.exceptions.ResourceNotFoundException;
import com.company.blog.payloads.PostDto;
import com.company.blog.payloads.PostResponse;
import com.company.blog.repositories.CategoryRepository;
import com.company.blog.repositories.PostRepository;
import com.company.blog.repositories.UserRepository;
import com.company.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		Post post = this.dtoToPost(postDto);
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		Post newPost = this.postRepository.save(post);
		return this.postToDto(newPost);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		Category category = this.categoryRepository.findById(postDto.getCategory().getCategoryId()).get();
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		post.setCategory(category);
		Post savedPost = this.postRepository.save(post);
		return this.postToDto(savedPost);
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		return this.postToDto(post);
	}

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable page = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> pagePost = this.postRepository.findAll(page);
		List<Post> posts = pagePost.getContent();
		List<PostDto> postDtos = posts.stream().map(post -> this.postToDto(post)).collect(Collectors.toList());
		PostResponse postResponse = new PostResponse(postDtos, pagePost.getNumber(), pagePost.getSize(),
				pagePost.getTotalElements(), pagePost.getTotalPages(), pagePost.isLast());
		return postResponse;
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		this.postRepository.delete(post);

	}

	@Override
	public PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy) {
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
		Page<Post> pagePost = this.postRepository.findByCategory(category, pageable);
		List<PostDto> postDtos = pagePost.stream().map(post -> this.postToDto(post)).collect(Collectors.toList());
		PostResponse postResponse = new PostResponse(postDtos, pagePost.getNumber(), pagePost.getSize(),
				pagePost.getTotalElements(), pagePost.getTotalPages(), pagePost.isLast());
		return postResponse;
	}

	@Override
	public PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
		Page<Post> pagePost = this.postRepository.findByUser(user, pageable);
		List<PostDto> postDtos = pagePost.stream().map(post -> this.postToDto(post)).collect(Collectors.toList());
		PostResponse postResponse = new PostResponse(postDtos, pagePost.getNumber(), pagePost.getSize(),
				pagePost.getTotalElements(), pagePost.getTotalPages(), pagePost.isLast());
		return postResponse;
	}

	@Override
	public PostResponse searchPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
		Page<Post> pagePost = this.postRepository.findByTitleContaining(keyword, pageable);
		List<PostDto> postDtos = pagePost.stream().map(post -> this.postToDto(post)).collect(Collectors.toList());
		PostResponse postResponse = new PostResponse(postDtos, pagePost.getNumber(), pagePost.getSize(),
				pagePost.getTotalElements(), pagePost.getTotalPages(), pagePost.isLast());
		return postResponse;
	}

	// convert dto to entity
	private Post dtoToPost(PostDto postDto) {
		Post post = this.modelMapper.map(postDto, Post.class);
		return post;
	}

	// converting entity to dto
	public PostDto postToDto(Post post) {
		PostDto postDto = this.modelMapper.map(post, PostDto.class);
		return postDto;
	}

}
