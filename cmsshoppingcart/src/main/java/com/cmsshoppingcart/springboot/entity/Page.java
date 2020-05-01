package com.cmsshoppingcart.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="pages")
public class Page {
	
	// creating fields and annotating them
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private int id;
	
	@Column(name="title")
	@Size(min = 2, message="title must be atleast 2 characters long")
	private String title;
	
	@Column(name="slug")
	private String slug;
	
	@Column(name="content")
	@Size(min = 5, message="Content must be atleast 5 characters long")
	private String content;
	
	@Column(name="sort")
	private int sorting;
	

	// no-arg constructor
	Page() {
		
	}
	
	// parameterised constructor
	Page(int id, String title, String slug, String content, int sorting) {
		this.id = id;
		this.title = title;
		this.slug = slug;
		this.content = content;
		this.sorting = sorting;
	}

	
	// getters and setters
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSorting() {
		return sorting;
	}

	public void setSorting(Integer sorting) {
		this.sorting = sorting;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
