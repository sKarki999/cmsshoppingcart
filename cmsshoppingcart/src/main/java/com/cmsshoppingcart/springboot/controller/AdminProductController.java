package com.cmsshoppingcart.springboot.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cmsshoppingcart.springboot.DAO.CategoryRepository;
import com.cmsshoppingcart.springboot.DAO.ProductRepository;
import com.cmsshoppingcart.springboot.entity.Category;
import com.cmsshoppingcart.springboot.entity.Product;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

	// inject dependencies
	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private CategoryRepository catRepo;


//	// get all the products without pagination support
//	@GetMapping
//	public String index(Model theModel) {
//
//		List<Product> products = productRepo.findAll();
//		// to display category name instead of category id;
//		List<Category> categories = catRepo.findAll();
//		
//		HashMap<Integer, String> cats = new HashMap<>();
//		for (Category cat : categories) {
//			cats.put(cat.getId(), cat.getName());
//		}
//
//		theModel.addAttribute("products", products);
//		theModel.addAttribute("categories", categories);
//		theModel.addAttribute("cats", cats);		
//		
//		return "admin/products/index";
//	}


	// get all products with pagination support
	@GetMapping
	public String index(Model theModel, @RequestParam(value="page", required=false) Integer p) {
		
		// per page count
		int perPage = 5;
		// if p has some value then value is retained if not, p is 0 meaning its the first page
		int page = (p != null) ? p : 0; 
		
		// for pagination support
		Pageable pageable = PageRequest.of(page, perPage); 
		Page<Product> products = productRepo.findAll(pageable);
		List<Category> categories = catRepo.findAll();

		HashMap<Integer, String> cats = new HashMap<>();
		for (Category cat : categories) {
			cats.put(cat.getId(), cat.getName());
		}

		theModel.addAttribute("products", products);
		theModel.addAttribute("categories", categories);
		theModel.addAttribute("cats", cats);		
		
		// count of all the records of rows
		long count = productRepo.count();
		// page count
		double pageCount = Math.ceil((double)count / (double)perPage);
		
		// add to model attributes
		theModel.addAttribute("pageCount", (int)pageCount);
		theModel.addAttribute("perPage", perPage);
		theModel.addAttribute("count", count);
		theModel.addAttribute("page", page);
		
		
		return "admin/products/index";
	
	
	}
	
	
	
	// add a product
	@GetMapping("/add")
	public String add(Model theModel, Product theProduct) {

		List<Category> categories = catRepo.findAll();
		theModel.addAttribute("products", theProduct);
		theModel.addAttribute("categories", categories);
		return "admin/products/add";
	}

	@PostMapping("/save")
	public String add(@Valid @ModelAttribute("products") Product theProduct, BindingResult tBR, RedirectAttributes tRA,
			MultipartFile file, Model theModel) throws IOException {

		if (tBR.hasErrors()) {

			List<Category> categories = catRepo.findAll();
			theModel.addAttribute("categories", categories);
			return "admin/products/add";
		}

		// to check whether product already exists
		String slug = theProduct.getName().toLowerCase().replace(" ", "-");
		Product prodExists = productRepo.findBySlug(slug);

		// to upload an image
		// firstly check if file exists
		boolean fileOk = false;
		byte[] bytes = file.getBytes();
		String filename = file.getOriginalFilename();
		Path path = Paths.get("src/main/resources/static/media/" + filename);

		if (filename.endsWith(".jpg") || filename.endsWith(".png")) {
			fileOk = true;
		}

		if (!fileOk) {

			tRA.addFlashAttribute("message", "Image must be png or jpg");
			tRA.addFlashAttribute("alertClass", "alert alert-danger");
			tRA.addFlashAttribute("products", theProduct);

		} else if (prodExists != null) {

			tRA.addFlashAttribute("message", "Product already exists");
			tRA.addFlashAttribute("alertClass", "alert alert-danger");
			tRA.addFlashAttribute("products", theProduct);

		} else {

			theProduct.setSlug(slug);
			theProduct.setImage(filename);
			productRepo.save(theProduct);

			// to upload file
			Files.write(path, bytes);

			tRA.addFlashAttribute("message", "Product added succesfully");
			tRA.addFlashAttribute("alertClass", "alert alert-success");

		}

		return "redirect:/admin/products/add";
	}

	// edit the page
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") int theId, Model theModel) {

		List<Category> categories = catRepo.findAll();
		theModel.addAttribute("categories", categories);

		Product theProduct = productRepo.getOne(theId);
		theModel.addAttribute("products", theProduct);

		return "admin/products/edit";
	}

	@PostMapping("/edit")
	public String edit(@Valid @ModelAttribute("products") Product theProduct, BindingResult tBR, RedirectAttributes tRA,
			MultipartFile file, Model theModel) throws IOException {

		Product currentProduct = productRepo.getOne(theProduct.getId());
		List<Category> categories = catRepo.findAll();

		if (tBR.hasErrors()) {

			theModel.addAttribute("categories", categories);
			theModel.addAttribute("productName", theProduct.getName());
			return "admin/products/edit";

		}

		// to upload an image
		// firstly check if file exists
		boolean fileOk = false;
		byte[] bytes = file.getBytes();
		String filename = file.getOriginalFilename();
		Path path = Paths.get("src/main/resources/static/media/" + filename);

		if (!file.isEmpty()) {

			if (filename.endsWith(".jpg") || filename.endsWith(".png")) {
				fileOk = true;
			}

		} else {
			fileOk = true;
		}

		tRA.addFlashAttribute("message", "Product edited succesfully");
		tRA.addFlashAttribute("alertClass", "alert alert-success");

		// to check whether product already exists
		String slug = theProduct.getName().toLowerCase().replace(" ", "-");
		Product prodExists = productRepo.findBySlugAndIdNot(slug, theProduct.getId());

		if (!fileOk) {

			tRA.addFlashAttribute("message", "Image must be png or jpg");
			tRA.addFlashAttribute("alertClass", "alert alert-danger");
			tRA.addFlashAttribute("products", theProduct);

		} else if (prodExists != null) {

			tRA.addFlashAttribute("message", "Product already exists");
			tRA.addFlashAttribute("alertClass", "alert alert-danger");
			tRA.addFlashAttribute("products", theProduct);

		} else {

			theProduct.setSlug(slug);

			if (!file.isEmpty()) {

				Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
				Files.delete(path2);
				theProduct.setImage(filename);
				Files.write(path, bytes);

			} else {

				theProduct.setImage(currentProduct.getImage());

			}

			productRepo.save(theProduct);

		}

		return "redirect:/admin/products/edit/" + theProduct.getId();
	}

	// delete the products
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") int theId, RedirectAttributes tRA, Product theProduct) throws IOException {

		Product currentProduct = productRepo.getOne(theProduct.getId());
		Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
		Files.delete(path2);

		productRepo.deleteById(theId);
		tRA.addFlashAttribute("message", "Product deleted succesfully");
		tRA.addFlashAttribute("alertClass", "alert alert-success");

		return "redirect:/admin/products";
	}

}
