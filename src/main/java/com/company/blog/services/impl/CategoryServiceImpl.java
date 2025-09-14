package com.company.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.blog.entities.Category;
import com.company.blog.exceptions.ResourceNotFoundException;
import com.company.blog.payloads.CategoryDto;
import com.company.blog.repositories.CategoryRepository;
import com.company.blog.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {

		Category category = this.dtoToCategory(categoryDto);
		Category savedCatagory = this.categoryRepository.save(category);

		return this.categoryToDto(savedCatagory);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {

		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

		category.setCategoryTitle(categoryDto.getCategoryTitle());
		category.setCategoryDescription(categoryDto.getCategoryDescription());

		Category updatedCategory = this.categoryRepository.save(category);

		return this.categoryToDto(updatedCategory);
	}

	@Override
	public CategoryDto getCategoryById(Integer categoryId) {

		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

		return this.categoryToDto(category);
	}

	@Override
	public List<CategoryDto> getAllCategories() {

		List<Category> categories = this.categoryRepository.findAll();
		List<CategoryDto> categoryDtos = categories.stream().map(category -> this.categoryToDto(category))
				.collect(Collectors.toList());

		return categoryDtos;
	}

	@Override
	public void deleteCategory(Integer categoryId) {

		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

		this.categoryRepository.delete(category);

	}

	// convert dto to entity
	private Category dtoToCategory(CategoryDto categoryDto) {
		Category catagory = this.modelMapper.map(categoryDto, Category.class);
		return catagory;
	}

	// converting entity to dto
	public CategoryDto categoryToDto(Category catrgory) {
		CategoryDto categoryDto = this.modelMapper.map(catrgory, CategoryDto.class);
		return categoryDto;
	}

}
