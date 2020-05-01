package com.cmsshoppingcart.springboot.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmsshoppingcart.springboot.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByUsername(String username);
	
}
