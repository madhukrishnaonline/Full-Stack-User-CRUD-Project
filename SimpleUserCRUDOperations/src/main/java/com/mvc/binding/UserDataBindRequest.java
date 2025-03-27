package com.mvc.binding;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataBindRequest {

	private byte[] profile;
	
	private String profileName;

	private String contentType;

	@NotBlank(message = "User Name Required")
	private String userName;

	@NotBlank(message = "Password Required")
	private String password;

	@NotNull(message = "Email Required")
	@Pattern(regexp = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b", message = "Invalid Email")
	private String email;

	@NotBlank(message = "Full Name Required")
	private String fullName;
	
	@NotBlank(message = "Address Required")
	private String address;

	@NotBlank(message = "City Required")
	private String city;

	@NotBlank(message = "State Required")
	private String state;

	@NotNull(message = "Mobile Required")
	@Pattern(regexp = "^\\d{10}$", message = "Invalid Mobile")
	private String mobile;

	private Boolean isActive = true;

}// class