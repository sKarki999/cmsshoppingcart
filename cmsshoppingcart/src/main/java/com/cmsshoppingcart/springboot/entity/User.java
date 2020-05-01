package com.cmsshoppingcart.springboot.entity;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="users")
public class User implements UserDetails {
	
	
	/*
	 * to-do
	 * define all the fields
	 * annotate them
	 * security stuffs
	 */
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Size(min=2, message="Name must have atleast 2 characters")
	private String username;
	
	@Size(min=4, message="must be 4 characters long")
	private String password;
	
	@Column(name="email")
	@Email(message="please provide valid email")
	private String email;
	
	@Column(name="phone_number")
	@Size(min=6, message="Must be atleast 6 characters long")
	private String phoneNumber;
	
	@Transient
	private String confirmPassword;
	
	
	// need to have for each role we define.
	private static final long serialVersionUID = 1l;
	
	/*
	 * this class implement userdetails for security stuffs
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// define the role
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
