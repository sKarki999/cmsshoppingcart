package com.cmsshoppingcart.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmsshoppingcart.springboot.DAO.PageRepository;
import com.cmsshoppingcart.springboot.entity.Page;

@Controller
@RequestMapping("/")
public class PageController {
	
	@Autowired
	private PageRepository pageRepo;
	
	@GetMapping
	public String home(Model theModel) {
		
		Page page = pageRepo.findBySlug("home");
		theModel.addAttribute("page", page);
		return "page";
	}
	
	
	@GetMapping("/login")
	private String login() {
		
		return "login";
	}
	
	
	@GetMapping("/{slug}")
	public String page(@PathVariable("slug") String slug, Model theModel) {
		
		Page page = pageRepo.findBySlug(slug);
		
		if (page == null) {
			return "redirect:/";
		}
		
		theModel.addAttribute("page", page);
		return "page";
	}
	
	
}









