package com.company.blog.services;

import java.util.List;

import com.company.blog.payloads.CategoryDto;

public interface CategoryService {

	// create
	CategoryDto createCategory(CategoryDto categoryDto);

	// update
	CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);

	// fetch by id
	CategoryDto getCategoryById(Integer categoryId);

	// fetch all
	List<CategoryDto> getAllCategories();

	// delete
	void deleteCategory(Integer categoryId);

}
