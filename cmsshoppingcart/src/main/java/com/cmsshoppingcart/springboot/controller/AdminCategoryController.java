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

import com.cmsshoppingcart.springboot.DAO.CategoryRepository;
import com.cmsshoppingcart.springboot.entity.Category;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

	@Autowired
	private CategoryRepository catRepo;

	// get all the categories
	@GetMapping
	public String index(Model theModel) {

		List<Category> categories = catRepo.findAllByOrderBySortingAsc();
		theModel.addAttribute("categories", categories);

		return "admin/categories/index";

	}

	// to add new category
	@GetMapping("/add")
	public String add(Model theModel, Category theCategory) {
		theModel.addAttribute("categories", theCategory);
		return "admin/categories/add";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("categories") Category theCategory, BindingResult tBR,
			RedirectAttributes tRA) {

		if (tBR.hasErrors()) {
			return "admin/categories/add";
		}

		// check if category name already exists
		String slug = theCategory.getName().toLowerCase().replace(" ", "-");

		Category slugExists = catRepo.findBySlug(slug);

		if (slugExists != null) {

			tRA.addFlashAttribute("message", "Category already Exists add another");
			tRA.addFlashAttribute("alertClass", "alert alert-danger");
			tRA.addFlashAttribute("categories", theCategory);

		} else {

			tRA.addFlashAttribute("message", "Category added Successfully");
			tRA.addFlashAttribute("alertClass", "alert alert-success");
			tRA.addFlashAttribute("categories", theCategory);

			theCategory.setSlug(slug);
			catRepo.save(theCategory);
		}

		return "redirect:/admin/categories/add";
	}

	// edit the page

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") int theId, Model theModel) {

		Category theCategory = catRepo.getOne(theId);
		theModel.addAttribute("categories", theCategory);
		return "admin/categories/edit";
	}

	@PostMapping("/edited")
	public String edited(@Valid @ModelAttribute("categories") Category theCategory, BindingResult tBR,
			RedirectAttributes tRA) {

		if (tBR.hasErrors()) {
			return "admin/categories/edit";
		}

		String slug = theCategory.getName().toLowerCase().replace(" ", "-");
		Category slugExists = catRepo.findBySlug(slug);

		if (slugExists != null) {
			tRA.addFlashAttribute("message", "category exists, choose another");
			tRA.addFlashAttribute("alertClass", "alert alert-danger");
			tRA.addFlashAttribute("categories", theCategory);
		} else {

			tRA.addFlashAttribute("message", "Category edited successfully");
			tRA.addFlashAttribute("alertClass", "alert alert-success");
			tRA.addFlashAttribute("categories", theCategory);
			
			theCategory.setSlug(slug);
			catRepo.save(theCategory);
		}

		return "redirect:/admin/categories/edit/" + theCategory.getId();
	}
	
	// delete the category based on id
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") int theId) {
		
		catRepo.deleteById(theId);
		return "redirect:/admin/categories";
	}
	
	
	// Reordering
	@PostMapping("/reorder")
	public @ResponseBody String reorder(@RequestParam("id[]") int[] id ) {
		
		Category category;
		int count = 1;
		
		for(int catId: id) {
			
			category = catRepo.getOne(catId);
			category.setSorting(count);
			catRepo.save(category);
			count++;
			
		}
		
		return "ok";
	}
	
	
	
}








