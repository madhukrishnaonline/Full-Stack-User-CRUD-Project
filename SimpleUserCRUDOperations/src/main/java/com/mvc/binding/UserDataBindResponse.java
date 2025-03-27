package com.mvc.binding;

import lombok.Data;

@Data
public class UserDataBindResponse {
	
	private Integer userId;
	
	private String profilePath;
	
	private String contentType;

	private String userName;

	private String password;

	private String email;

	private String fullName;
	
	private String address;

	private String city;

	private String state;

	private Long mobile;

	private Boolean isActive;

}// class