package com.cmsshoppingcart.springboot.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmsshoppingcart.springboot.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer>{

	
	Admin findByUsername(String username);
	
}
