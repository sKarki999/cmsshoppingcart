package com.cmsshoppingcart.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmsshoppingcart.springboot.DAO.CategoryRepository;
import com.cmsshoppingcart.springboot.DAO.ProductRepository;
import com.cmsshoppingcart.springboot.entity.Category;
import com.cmsshoppingcart.springboot.entity.Product;

@Controller
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryRepository catRepo;

	@Autowired
	private ProductRepository prodRepo;

	@GetMapping("/{slug}")
	public String category(@PathVariable("slug") String slug, Model theModel,
			@RequestParam(value = "page", required = false) Integer p) {

		// per page count
		int perPage = 6;
		// if p has some value then value is retained if not, p is 0 meaning its the
		// first page
		int page = (p != null) ? p : 0;
		Pageable pageable = PageRequest.of(page, perPage);
		long count = 0;

		if (slug.equals("all")) {

			Page<Product> products = prodRepo.findAll(pageable);
			count = prodRepo.count();

			theModel.addAttribute("products", products);

		} else {

			Category category = catRepo.findBySlug(slug);

			if (category == null) {
				return "redirect:/";
			}

			int categoryId = category.getId();
			String categoryName = category.getName();

			List<Product> products = prodRepo.findAllByCategoryId(Integer.toString(categoryId), pageable);

			count = prodRepo.countByCategoryId(Integer.toString(categoryId));

			theModel.addAttribute("products", products);
			theModel.addAttribute("categoryName", categoryName);
		}

		// page count
		double pageCount = Math.ceil((double) count / (double) perPage);
		
		// if query paramter page is greater than the actual pages then redirect to last page
		if ( page >= pageCount || page < 0) {
			return "redirect:/category/" + slug + "?page=" + ((int)pageCount-1);
		}

		// add to model attributes
		theModel.addAttribute("pageCount", (int) pageCount);
		theModel.addAttribute("perPage", perPage);
		theModel.addAttribute("count", count);
		theModel.addAttribute("page", page);

		return "products";
	}

}
