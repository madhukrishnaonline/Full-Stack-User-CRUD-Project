package com.mvc.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvc.binding.UserDataBindRequest;
import com.mvc.binding.UserDataBindResponse;
import com.mvc.binding.UserDataBindResponsePage;
import com.mvc.binding.UserLoginBind;
import com.mvc.constants.AppConstants;
import com.mvc.exceptions.UserNotFoundException;
import com.mvc.service.JwtService;
import com.mvc.service.UserInfoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*",methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PATCH,RequestMethod.PUT,RequestMethod.DELETE})
public class UserController {

	@Autowired
	private UserInfoService userService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/")
	public ResponseEntity<String> launchHome() {
		String home = """
				<center>
				     <h1>"Welcome"</h1>
				            <h2>"to"</h2>
				              <h3>"Users"</h3>
				                   <h4>"API"</h4>
				                             </center>
				""";
		return ResponseEntity.status(HttpStatus.OK).body(home);
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestPart String userJson, @RequestPart MultipartFile file) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			UserDataBindRequest userInfo = objectMapper.readValue(userJson, UserDataBindRequest.class);

			String user = userService.registerUser(userInfo, file);
			return new ResponseEntity<String>(user, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} // catch
	}// registerUser

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody UserLoginBind loginBind) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginBind.getUserName(), loginBind.getPassword());
		Authentication authenticate = authenticationManager.authenticate(authenticationToken);
		if (authenticate.isAuthenticated()) {
			String token = jwtService.generateToken(loginBind.getUserName());
			System.out.println(token);
			return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("token", token));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Credentials");
	}

	@GetMapping("token/{token}")
	public ResponseEntity<UserDataBindResponse> getUserDetailsByToken(@PathVariable String token) {
		String userName = jwtService.extractUserName(token.replace("Bearer ", ""));
		UserDataBindResponse userDetails = null;
		try {
			userDetails = userService.findByUserName(userName);
			return new ResponseEntity<UserDataBindResponse>(userDetails, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<UserDataBindResponse>(userDetails, HttpStatus.NOT_FOUND);
		}

	}

	/*@PostMapping("/registerAll")
	public ResponseEntity<String> registerMultipleUsersData(@RequestBody @Valid List<UserDataBindRequest> userInfos) {
		String response = userService.registerMultipleUsersData(userInfos);
		if (response.equalsIgnoreCase(AppConstants.REG_SUCCESS)) {
			return new ResponseEntity<String>(response, HttpStatus.CREATED);
		} // if
		else {
			return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
		} // else
	}*/

	@GetMapping("/find/username/{userName}")
	public ResponseEntity<UserDataBindResponse> findUserById(@PathVariable String userName)
			throws UserNotFoundException {
		UserDataBindResponse data = userService.findByUserName(userName);
		if (data != null) {
			return new ResponseEntity<UserDataBindResponse>(data, HttpStatus.FOUND);
		} // if
		else {
			return new ResponseEntity<UserDataBindResponse>(data, HttpStatus.NOT_FOUND);
		} // else
	}// findUserById

	@GetMapping("/find/mail/{mail}")
	public ResponseEntity<UserDataBindResponse> findUserByMail(@PathVariable String mail) throws UserNotFoundException {
		UserDataBindResponse data = userService.findByUserMail(mail);
		if (data != null) {
			return new ResponseEntity<UserDataBindResponse>(data, HttpStatus.FOUND);
		} // if
		else {
			return new ResponseEntity<UserDataBindResponse>(data, HttpStatus.NOT_FOUND);
		} // else
	}// findUserById

	/*
	 * @PostMapping("/login") public ResponseEntity<UserDataBindResponse>
	 * loginUser(@RequestBody @Valid UserLoginBind loginBind) throws
	 * UserNotFoundException { UserDataBindResponse loginUser =
	 * userService.loginUser(loginBind); if (loginUser != null) { return new
	 * ResponseEntity<UserDataBindResponse>(loginUser, HttpStatus.OK); } // if else
	 * { return new ResponseEntity<UserDataBindResponse>(loginUser,
	 * HttpStatus.NOT_FOUND); } // else }// loginUser
	 */

	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer userId) throws UserNotFoundException {
		String response = userService.deleteUserById(userId);
		if (response.equals(AppConstants.USER_DATA_DELETED)) {
			return new ResponseEntity<String>(response, HttpStatus.GONE);
		} // if
		else {
			return new ResponseEntity<String>(response, HttpStatus.NOT_FOUND);
		} // else
	}// deleteUser

	@GetMapping("/findAllUsers")
	public ResponseEntity<List<UserDataBindResponse>> findAllUsers() {
		List<UserDataBindResponse> data = userService.findAllUsers();
		if (data != null) {
			return new ResponseEntity<List<UserDataBindResponse>>(data, HttpStatus.OK);
		} // if
		else {
			return new ResponseEntity<List<UserDataBindResponse>>(data, HttpStatus.NO_CONTENT);
		} // else
	}

	@GetMapping("image/{id}")
	public ResponseEntity<Resource> getFileById(@PathVariable Integer id) {
		try {
			String imagePath = userService.findUserImageById(id);
			String fileName = imagePath.substring(20);
//			System.out.println("File Name :: " + fileName);

			String uploadDir = imagePath.substring(0, 20);
//			System.out.println("Upload Dir :: " + uploadDir);

			Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() || resource.isReadable()) {
				return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG) // Change based on file type
						.body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping(value = "/findAllUsers/sort/asc")
	public ResponseEntity<List<UserDataBindResponse>> findAllUsersBySortAsc() {
		List<UserDataBindResponse> data = userService.findAllUsersBySortAsc();
		if (data != null) {
			return new ResponseEntity<List<UserDataBindResponse>>(data, HttpStatus.OK);
		} // if
		else {
			return new ResponseEntity<List<UserDataBindResponse>>(data, HttpStatus.NO_CONTENT);
		} // else
	}

	@GetMapping(value = "/findAllUsers/sort/desc")
	public ResponseEntity<List<UserDataBindResponse>> findAllUsersBySortDescsc() {
		List<UserDataBindResponse> data = userService.findAllUsersBySortDesc();
		if (data != null) {
			return new ResponseEntity<List<UserDataBindResponse>>(data, HttpStatus.OK);
		} // if
		else {
			return new ResponseEntity<List<UserDataBindResponse>>(data, HttpStatus.NO_CONTENT);
		} // else
	}// findAllUsersBySortDescsc()

	@GetMapping("/findAllUsers/bypage/{pageNo}/{pageSize}")
	public ResponseEntity<UserDataBindResponsePage> findAllUsersByPage(
			@PageableDefault(page = 0, size = 5) Pageable pageable, @PathVariable Integer pageNo,
			@PathVariable Integer pageSize) {
		UserDataBindResponsePage data = userService.findAllUsersByPage(pageNo, pageSize);
		if (data != null) {
			return new ResponseEntity<UserDataBindResponsePage>(data, HttpStatus.OK);
		} // if
		else {
			return new ResponseEntity<UserDataBindResponsePage>(data, HttpStatus.NO_CONTENT);
		} // else
	}// findAllUsersByPage()

	@GetMapping("/findAllUsers/bypage/{pageNo}/{pageSize}/asc")
	public ResponseEntity<UserDataBindResponsePage> findAllUsersByPageAndSort(@PathVariable Integer pageNo,
			@PathVariable Integer pageSize) {
		UserDataBindResponsePage data = userService.findAllUsersByPageAndSort(pageNo, pageSize);
		if (data != null) {
			return new ResponseEntity<UserDataBindResponsePage>(data, HttpStatus.OK);
		} // if
		else {
			return new ResponseEntity<UserDataBindResponsePage>(data, HttpStatus.NO_CONTENT);
		} // else
	}// findAllUsersByPageAndSort()

	@PutMapping("/update")
	public ResponseEntity<String> updateUserData(@RequestBody @Valid UserDataBindRequest request) {
		String response = userService.updateUserData(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}// updateUserData()

	@PatchMapping("/update/{oldUserName}/{newUserName}")
	public ResponseEntity<String> updateUserName(@PathVariable String oldUserName, @PathVariable String newUserName) {
		String response = userService.updateUserName(oldUserName, newUserName);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/delete/all")
	public ResponseEntity<String> deleteAllUsers() {
		String response = userService.deleteAllUsers();
		return new ResponseEntity<String>(response, HttpStatus.GONE);
	}// deleteAllUsers()
}// class