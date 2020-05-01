package com.cmsshoppingcart.springboot.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.cmsshoppingcart.springboot.DAO.CategoryRepository;
import com.cmsshoppingcart.springboot.DAO.PageRepository;
import com.cmsshoppingcart.springboot.entity.Cart;
import com.cmsshoppingcart.springboot.entity.Category;
import com.cmsshoppingcart.springboot.entity.Page;

@ControllerAdvice
@SuppressWarnings("unchecked")
public class CommonController {

	@Autowired
	private PageRepository pageRepo;
	
	@Autowired
	private CategoryRepository catRepo;
	
	@ModelAttribute
	public void sharedData(Model theModel, HttpSession theSession, Principal principal) {
		
		/*
		 * Principle: gives access to login users
		 * used to represent any entity
		 */
		if( principal != null) {
			theModel.addAttribute("principal", principal.getName());
		}
		
		
		List<Page> pages = pageRepo.findAllByOrderBySortingAsc();
		List<Category> categories = catRepo.findAll();
		
		boolean cartActive = false;
		
		if(theSession.getAttribute("cart") != null) {
			
			HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)theSession.getAttribute("cart");
			
			/*
			 * Cart Overview: display size and total
			 * loop through the cart and add to these variables
			 */
			
			
			int size = 0;
			double total = 0;
			
			for(Cart value: cart.values()) {
				size += value.getQuantity();
				total += value.getQuantity() * Double.parseDouble(value.getPrice());
			}
			
			cartActive = true;
			
			theModel.addAttribute("csize", size);
			theModel.addAttribute("ctotal", total);
			theModel.addAttribute("cActive", cartActive);
			
			
		}
		
		
		theModel.addAttribute("cpages", pages);
		theModel.addAttribute("ccategories", categories);
		
	}

}
