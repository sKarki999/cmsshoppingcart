package com.cmsshoppingcart.springboot.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "products")
public class Product {

	// define all the fields as per table, and annotate them

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	@Size(min = 2, message = "Name must be atleast 2 characters long")
	private String name;

	@Column(name = "slug")
	private String slug;

	@Column(name = "description")
	@Size(min = 5, message = "description must be atleast 5 characters long")
	private String description;

	@Column(name = "image")
	private String image;

	@Column(name = "price")
	@Pattern(regexp = "^[0-9]+([.][0-9]{1,2})?", message = "expected format: 0.99 or 1")
	private String price;

	@Column(name = "category_id")
	@Pattern(regexp = "^[1-9][0-9]*", message = "please choose a category")
	private String categoryId;

	@Column(name = "createdAt", updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updatedAt")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	// define constructors
	Product() {

	}

	public Product(Integer id, String name, String slug, String description, String image, String price,
			String categoryId, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.name = name;
		this.slug = slug;
		this.description = description;
		this.image = image;
		this.price = price;
		this.categoryId = categoryId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
		
}







