package com.company.blog.services;

import com.company.blog.payloads.PostDto;
import com.company.blog.payloads.PostResponse;

public interface PostService {

	// create
	PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);

	// update
	PostDto updatePost(PostDto postDto, Integer postId);

	// fetch by id
	PostDto getPostById(Integer postId);

	// fetch all
	PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	// delete
	void deletePost(Integer postId);
	
	//get all post by category
	PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy);
	
	//get all post by user
	PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy);
	
	//search for post
	PostResponse searchPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy);	
}
