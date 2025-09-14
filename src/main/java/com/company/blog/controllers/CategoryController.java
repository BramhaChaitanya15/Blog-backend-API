package com.company.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.blog.payloads.ApiResponse;
import com.company.blog.payloads.CategoryDto;
import com.company.blog.services.CategoryService;

import jakarta.validation.Valid;

//Role - ADMIN
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	// POST-create
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		CategoryDto createCategoryDto = this.categoryService.createCategory(categoryDto);
		return new ResponseEntity<>(createCategoryDto, HttpStatus.CREATED);
	}

	// PUT-update
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable("categoryId") Integer cid) {
		CategoryDto updateCategoryDto = this.categoryService.updateCategory(categoryDto, cid);
		return ResponseEntity.ok(updateCategoryDto);
	}

	// DELETE-delete
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") Integer cid) {
		this.categoryService.deleteCategory(cid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("category deleted successfully!", true), HttpStatus.OK);
	}

	// GET-fetch
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable("categoryId") Integer cid) {
		return ResponseEntity.ok(this.categoryService.getCategoryById(cid));
	}

	// GET-fetch all
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllCategories() {
		return ResponseEntity.ok(this.categoryService.getAllCategories());
	}

}
