package com.cmsshoppingcart.springboot.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmsshoppingcart.springboot.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	Category findBySlug(String slug);
	
	List<Category> findAllByOrderBySortingAsc();
	
	
	
}
