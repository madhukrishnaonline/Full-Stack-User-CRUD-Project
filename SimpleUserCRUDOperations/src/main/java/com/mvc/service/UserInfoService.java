package com.mvc.service;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import com.mvc.binding.UserDataBindRequest;
import com.mvc.binding.UserDataBindResponse;
import com.mvc.binding.UserDataBindResponsePage;
import com.mvc.exceptions.UserNotFoundException;

public interface UserInfoService extends UserDetailsService{
	String registerUser(UserDataBindRequest request,MultipartFile imageFile) throws IOException;
	
//	String registerMultipleUsersData(List<UserDataBindRequest> request);

	UserDataBindResponse findByUserName(String userName) throws UserNotFoundException;

	UserDataBindResponse findByUserMail(String mail) throws UserNotFoundException;
	
	String findUserImageById(Integer userId) throws UserNotFoundException;

//	UserDataBindResponse loginUser(UserLoginBind loginBind) throws UserNotFoundException;
	
	List<UserDataBindResponse> findAllUsers();

	List<UserDataBindResponse> findAllUsersBySortAsc();
	
	List<UserDataBindResponse> findAllUsersBySortDesc();
	
	UserDataBindResponsePage findAllUsersByPage(Integer pageNo,Integer pageSize);
	
//	List<UserDataBindResponse> findAllUsersInEveryPage(Integer pageSize);

	UserDataBindResponsePage findAllUsersByPageAndSort(Integer pageNo,Integer pageSize);
	
//	UserDataBindResponse updateUserData
	
	String updateUserData(UserDataBindRequest request);
	
	String updateUserName(String oldUserName,String newUserName);
	
	String deleteUserById(Integer userId) throws UserNotFoundException;
	
	String deleteAllUsers();
}//interface