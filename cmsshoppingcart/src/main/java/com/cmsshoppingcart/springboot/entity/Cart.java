package com.cmsshoppingcart.springboot.entity;


public class Cart {
	
	private Integer id;
	private String name;
	private String price;
	private Integer quantity;
	private String image;
	
	public Cart(Integer id, String name, String price, Integer quantity, String image) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.image = image;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Cart [id=" + id + ", name=" + name + ", price=" + price + ", quantity=" + quantity + ", image=" + image
				+ "]";
	}
		
	
}
