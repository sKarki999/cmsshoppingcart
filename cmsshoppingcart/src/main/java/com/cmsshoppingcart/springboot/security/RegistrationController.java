package com.cmsshoppingcart.springboot.security;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmsshoppingcart.springboot.DAO.UserRepository;
import com.cmsshoppingcart.springboot.entity.User;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passEncoder;
	
	
	@GetMapping
	public String register(User theUser, Model theModel) {
		
		theModel.addAttribute("user", theUser);
		return "register";
	
	}
	
	
	@PostMapping("/save")
	public String register(@Valid @ModelAttribute("user") User theUser,
							BindingResult tBR, Model theModel) {
		
		
		if(tBR.hasErrors()) {
			return "register";
		}
			
		if (!theUser.getPassword().equals(theUser.getConfirmPassword())) {
			
			theModel.addAttribute("passwordMatchProblem", "Password donot match");
			return "register";
			
		}
		
		theUser.setPassword(passEncoder.encode(theUser.getPassword()));
		
		userRepo.save(theUser);
		
		return "redirect:/login";
		
	}
	
	
}











