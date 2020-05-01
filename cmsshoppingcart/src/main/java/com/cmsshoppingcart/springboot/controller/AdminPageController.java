package com.cmsshoppingcart.springboot.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cmsshoppingcart.springboot.DAO.PageRepository;
import com.cmsshoppingcart.springboot.entity.Page;

@Controller
@RequestMapping("/admin/pages")
public class AdminPageController {

	// // Inject dependencies
	// // constructor dependencies
	// private PageRepository pageRepo;
	// public AdminPageController(PageRepository pageRepo) {
	// this.pageRepo = pageRepo;
	// }

	// Inject dependencies
	// using @Autowired annotation
	@Autowired
	private PageRepository pageRepo;

	// find all the pages
	// GetMapping
	@GetMapping
	public String index(Model theModel) {

		// create a list with the type of Page.
		List<Page> pages = pageRepo.findAllByOrderBySortingAsc();

		// add the returned pages in model attribute and return to the view page
		theModel.addAttribute("pages", pages);
		return "admin/pages/index";

	}

	// To add a new Page
	@GetMapping("/add")
	public String add(Model theModel, Page thePage) {

		theModel.addAttribute("pages", thePage);
		return "admin/pages/add";
		
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("pages") Page thePage, BindingResult tBR, RedirectAttributes tRA) {

		// check for errors
		if (tBR.hasErrors()) {

			return "admin/pages/add";

		}

		// set the slug
		// if slug isnot given, take the title, convert to lowercase and replace spaces
		// by -
		// if slug is given, take the slug, convert to lowercase and replace spaces by -
		String slug = thePage.getSlug() == "" ? thePage.getTitle().toLowerCase().replace(" ", "-")
				: thePage.getSlug().toLowerCase().replace(" ", "-");

		// slug should be unique
		// check whether the slug provided is already present in database
		Page slugExists = pageRepo.findBySlug(slug);

		if (slugExists != null) {
			// show the error messages
			tRA.addFlashAttribute("message", "Slug exists choose another");
			tRA.addFlashAttribute("alertClass", "alert alert-danger");
			tRA.addFlashAttribute("pages", thePage);
		} else {

			// show success message
			// save the slug and then
			// save the object in database
			tRA.addFlashAttribute("message", "Page added succesfully");
			tRA.addFlashAttribute("alertClass", "alert alert-success");
			tRA.addFlashAttribute("pages", thePage);

			thePage.setSlug(slug);
			thePage.setSorting(200);
			pageRepo.save(thePage);
		}

		return "redirect:/admin/pages/add";
	}

	// editing the page
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") int theId, Model theModel) {

		Page thePage = pageRepo.getOne(theId);
		theModel.addAttribute("pages", thePage);

		return "admin/pages/edit";
	}

	@PostMapping("/edited")
	public String edit(@Valid @ModelAttribute("pages") Page thePage, BindingResult tBR,
			RedirectAttributes tRA) {

		if (tBR.hasErrors()) {
			return "admin/pages/edit";
		}

		// set the slug and check whether it exists in another titles
		String slug = thePage.getSlug() == "" ? thePage.getTitle().toLowerCase().replace(" ", "-")
				: thePage.getSlug().toLowerCase().replace(" ", "-");

		Page slugExists = pageRepo.findBySlugAndIdNot(slug, thePage.getId());

		if (slugExists != null) {
			// show the error messages
			tRA.addFlashAttribute("message", "Slug exists choose another");
			tRA.addFlashAttribute("alertClass", "alert alert-danger");
			tRA.addFlashAttribute("pages", thePage);
		} else {

			// show success message
			// save the slug and then
			// save the object in database
			tRA.addFlashAttribute("message", "Page edited succesfully");
			tRA.addFlashAttribute("alertClass", "alert alert-success");
			tRA.addFlashAttribute("pages", thePage);
			
			
			thePage.setSlug(slug);
			pageRepo.save(thePage);
		}

		return "redirect:/admin/pages/edit/" + thePage.getId();
	}
	
	// deleting the page 
	// based on id
	// prompt the user once to confirm deletion
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") int theId) {
		
		pageRepo.deleteById(theId);
		
		return "redirect:/admin/pages";
		
	}
	
	
	// reordering the page
	@PostMapping("/reorder")
	public @ResponseBody String reorder(@RequestParam("id[]") int[] id) {
		
		Page page;
		int count = 1;
		
		for(int pageId: id) {
			
			page = pageRepo.getOne(pageId);
			page.setSorting(count);
			pageRepo.save(page);
			count++;
			
		}
		
		return "ok";
	}

}








