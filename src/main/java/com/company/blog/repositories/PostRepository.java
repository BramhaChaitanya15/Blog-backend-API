package com.company.blog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.company.blog.entities.Category;
import com.company.blog.entities.Post;
import com.company.blog.entities.User;

public interface PostRepository extends JpaRepository<Post, Integer> {
	
	Page<Post> findByUser(User user, Pageable pageable);
	Page<Post> findByCategory(Category category, Pageable pageable);
	
	Page<Post> findByTitleContaining(String title, Pageable pageable);

}
