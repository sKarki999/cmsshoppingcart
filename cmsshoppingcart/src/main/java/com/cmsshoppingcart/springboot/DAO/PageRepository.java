package com.cmsshoppingcart.springboot.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cmsshoppingcart.springboot.entity.Page;

public interface PageRepository extends JpaRepository<Page, Integer>{
	
	Page findBySlug(String slug);

	Page findBySlugAndIdNot(String slug, Integer id);

	List<Page> findAllByOrderBySortingAsc();
	
}
