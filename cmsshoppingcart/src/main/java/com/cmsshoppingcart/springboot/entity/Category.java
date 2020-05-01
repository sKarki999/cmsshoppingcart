package com.cmsshoppingcart.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="categories")
public class Category {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	@Size(min=2, message="Name must be atleast 2 character")
	private String name;
	
	@Column(name="slug")
	private String slug;
	
	@Column(name="sort")
	private int sorting;

	public Category() {
		
	}
	
	public Category(Integer id, String name, String slug, int sorting) {
		this.id = id;
		this.name = name;
		this.slug = slug;
		this.sorting = sorting;
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

	public int getSorting() {
		return sorting;
	}

	public void setSorting(int sorting) {
		this.sorting = sorting;
	}
	

}



