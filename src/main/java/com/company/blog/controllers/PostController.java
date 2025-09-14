package com.company.blog.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.blog.config.AppConstants;
import com.company.blog.payloads.ApiResponse;
import com.company.blog.payloads.PostDto;
import com.company.blog.payloads.PostResponse;
import com.company.blog.services.FileService;
import com.company.blog.services.PostService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	// POST-create post
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) throws IOException {
		PostDto createdPost = this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createdPost, HttpStatus.CREATED);
	}

	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
		PostDto updatedPost = this.postService.updatePost(postDto, postId);
		return ResponseEntity.ok(updatedPost);
	}

	// GET-get post by id
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
		return ResponseEntity.ok(this.postService.getPostById(postId));
	}

	// GET-get all posts
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getPosts(
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		return ResponseEntity.ok(this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir));
	}

	// GET-get posts by user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PostResponse> getPostsByUser(@PathVariable Integer userId,
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy) {
		return ResponseEntity.ok(this.postService.getPostsByUser(userId, pageNumber, pageSize, sortBy));
	}

	// GET-get posts by category
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<PostResponse> getPostsByCategory(@PathVariable Integer categoryId,
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy) {
		return ResponseEntity.ok(this.postService.getPostsByCategory(categoryId, pageNumber, pageSize, sortBy));
	}

	// DELETE- delete post
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted successfully!", true), HttpStatus.OK);
	}

	// GET-search posts
	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<PostResponse> searchPostByTitle(@PathVariable String keywords,
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy) {
		return ResponseEntity.ok(this.postService.searchPosts(keywords, pageNumber, pageSize, sortBy));
	}

	// POST-post image upload
	@PostMapping("/posts/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam MultipartFile image, @PathVariable Integer postId)
			throws IOException {

		PostDto postDto = this.postService.getPostById(postId);

//		String oldImageName = postDto.getImageName();
//		if (oldImageName != null && !oldImageName.equalsIgnoreCase("default.png")) {
//		    try {
//		        // Rename the old file to a temp name (e.g., append .bak)
//		        this.fileService.renameFile(path, oldImageName, oldImageName + ".bak");
//		        System.out.println("Old image renamed to .bak for later deletion.");
//		    } catch (IOException e) {
//		        System.err.println("Failed to rename old file: " + e.getMessage());
//		    }
//		}

		String fileName = this.fileService.uploadimage(path, image);
		postDto.setImageName(fileName);
		PostDto updatePost = this.postService.updatePost(postDto, postId);

		return ResponseEntity.ok(updatePost);

	}

	// GET-serve files
	@GetMapping(value = "/posts/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = this.fileService.getResource(path, imageName);
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
