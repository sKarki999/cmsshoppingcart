package com.cmsshoppingcart.springboot.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmsshoppingcart.springboot.DAO.ProductRepository;
import com.cmsshoppingcart.springboot.entity.Cart;
import com.cmsshoppingcart.springboot.entity.Product;

@Controller
@RequestMapping("/cart")
@SuppressWarnings("unchecked")
public class CartController {

	@Autowired
	private ProductRepository prodRepo;

	/*
	 * HttpSession to keep the cart data in the session from request to request
	 */
	@GetMapping("/add/{id}")
	public String add(@PathVariable("id") int theId, HttpSession theSession, Model theModel,
			@RequestParam(value = "cartPage", required = false) String cartPage) {

		Product theProduct = prodRepo.getOne(theId);

		/*
		 * Check the session whether it exists or not if not setted, then add the
		 * product and set the session.
		 */
		if (theSession.getAttribute("cart") == null) {

			HashMap<Integer, Cart> cart = new HashMap<>();
			cart.put(theId, new Cart(theId, theProduct.getName(), theProduct.getPrice(), 1, theProduct.getImage()));
			theSession.setAttribute("cart", cart);

		} else {

			/*
			 * get the session first, if the same product is added again, dont add rather
			 * increase the quantity. if not, simply add the product to the cart and set the
			 * session.
			 */
			HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) theSession.getAttribute("cart");

			if (cart.containsKey(theId)) {

				int qty = cart.get(theId).getQuantity();
				cart.put(theId,
						new Cart(theId, theProduct.getName(), theProduct.getPrice(), ++qty, theProduct.getImage()));

			} else {

				cart.put(theId, new Cart(theId, theProduct.getName(), theProduct.getPrice(), 1, theProduct.getImage()));
				theSession.setAttribute("cart", cart);
			}

		}

		HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) theSession.getAttribute("cart");

		int size = 0;
		double total = 0;

		for (Cart value : cart.values()) {
			size += value.getQuantity();
			total += value.getQuantity() * Double.parseDouble(value.getPrice());
		}

		theModel.addAttribute("size", size);
		theModel.addAttribute("total", total);

		if (cartPage != null) {

			return "redirect:/cart/view";

		}
		return "cart_view";

	}

	/*
	 * subract the product if there was single product, remove session also get view
	 * cart after subtraction, dynamically
	 */
	@GetMapping("/subtract/{id}")
	public String subtract(@PathVariable("id") int theId, HttpSession theSession, Model theModel,
			HttpServletRequest theHttpServletRequest) {

		Product theProduct = prodRepo.getOne(theId);

		HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) theSession.getAttribute("cart");
		int qty = cart.get(theId).getQuantity();
		if (qty == 1) {
			cart.remove(theId);
			if (cart.size() == 0) {
				theSession.removeAttribute("cart");
			}
		} else {

			cart.put(theId, new Cart(theId, theProduct.getName(), theProduct.getPrice(), --qty, theProduct.getImage()));
		}

		String refererLink = theHttpServletRequest.getHeader("referer");

		return "redirect:" + refererLink;
	}

	@GetMapping("/remove/{id}")
	public String remove(@PathVariable("id") int theId, HttpSession theSession, Model theModel,
			HttpServletRequest theHttpServletRequest) {

		HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) theSession.getAttribute("cart");
		cart.remove(theId);
		if (cart.size() == 0) {
			theSession.removeAttribute("cart");
		}

		String refererLink = theHttpServletRequest.getHeader("referer");

		return "redirect:" + refererLink;
	}

	
	@GetMapping("/clear")
	public String clear(HttpSession theSession, HttpServletRequest theHttpServletRequest) {

		
		theSession.removeAttribute("cart");
			
		String refererLink = theHttpServletRequest.getHeader("referer");

		return "redirect:" + refererLink;
	}
	
	
	
	@GetMapping("/view")
	public String view(HttpSession theSession, Model theModel) {

		if (theSession.getAttribute("cart") == null) {
			return "redirect:/";
		}

		HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) theSession.getAttribute("cart");

		theModel.addAttribute("cart", cart);
		theModel.addAttribute("notcartviewpage", true);

		return "cart";
	}

}
