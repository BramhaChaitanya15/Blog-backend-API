package com.company.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.blog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
