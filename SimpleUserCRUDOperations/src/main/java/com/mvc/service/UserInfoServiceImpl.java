package com.mvc.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mvc.binding.UserDataBindRequest;
import com.mvc.binding.UserDataBindResponse;
import com.mvc.binding.UserDataBindResponsePage;
import com.mvc.constants.AppConstants;
import com.mvc.email.service.EmailService;
import com.mvc.entity.UserInfoEntity;
import com.mvc.exceptions.UserNotFoundException;
import com.mvc.repository.UserInfoRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserInfoRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Override
	public String registerUser(@Valid UserDataBindRequest request, MultipartFile multipartFile) throws IOException {
		Optional<UserInfoEntity> entity = userRepository.findByUserName(request.getUserName());
		if (entity.isEmpty()) {
			request.setProfileName(multipartFile.getOriginalFilename());
			request.setContentType(multipartFile.getContentType());

			UserInfoEntity userInfo = new UserInfoEntity();
			BeanUtils.copyProperties(request, userInfo);

			Long mobile = Long.valueOf(request.getMobile());
			userInfo.setMobile(mobile);
			userInfo.setPassword(passwordEncoder.encode(request.getPassword()));

			String storePath = "M:\\SpringBootImages";
			File dir = new File(storePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, request.getProfileName());
			try (InputStream inputStream = multipartFile.getInputStream();
					FileOutputStream outputStream = new FileOutputStream(file)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

				userInfo.setProfilePath(storePath + File.separator + request.getProfileName());

				userRepository.save(userInfo);

				emailService.sendRegistrationMail(request.getEmail(), AppConstants.REG_SUCCESS,
						sendRegMailBody(userInfo.getUserName()));

				log.info("Registration Mail Sent Successfully");
				return AppConstants.REG_SUCCESS;
			}
		} // if
		else {
			return AppConstants.ALREADY_REGISTERED;
		} // else
	}// registerUser()

	/*@Override
	public String registerMultipleUsersData(List<UserDataBindRequest> request) {
		List<String> userNames = request.stream().map(entity -> entity.getUserName()).toList();
		
		request.forEach(data->{
			Optional<UserInfoEntity> userData = userRepository.findByUserName(data.getUserName());					
		});
		if (allById.isEmpty()) {
			List<UserInfoEntity> usersInfoEntity = new LinkedList<UserInfoEntity>();
			for (UserDataBindRequest data : request) {
				UserInfoEntity entity = new UserInfoEntity();
				BeanUtils.copyProperties(data, entity);
	
				Long mobile = Long.valueOf(data.getMobile());
				entity.setMobile(mobile);
	
				usersInfoEntity.add(entity);
			} // for
			List<UserInfoEntity> newEntity = userRepository.saveAll(usersInfoEntity);
			ExecutorService threadPool = Executors.newFixedThreadPool(5);
			ExecutorCompletionService<Object> service = new ExecutorCompletionService<Object>(threadPool);
			for (UserInfoEntity data : newEntity) {
				service.submit(new Callable<Object>() {
					@Override
					public Object call() throws Exception {
						try {
							UserDataBindResponse bindDataResponse = new UserDataBindResponse();
							BeanUtils.copyProperties(data, bindDataResponse);
							emailService.sendRegistrationMail(bindDataResponse.getEmail(), AppConstants.REG_SUCCESS,
									sendRegMailBody(bindDataResponse.getUserName()));
						} // try
						catch (Exception e) {
							e.printStackTrace();
						} // catch
						return null;
					}// call()
				});// submit(-)
			} // for
			log.info("Registration mails sent Successfully...");
			return AppConstants.REG_SUCCESS;
		} // if
		else {
			return "One of the User/Users Already Registered";
		} // else
	}// registerMultipleUsersData()
	*/

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfoEntity> userData = userRepository.findByUserName(username);
		if (userData.isPresent()) {
			UserInfoEntity user = userData.get();
			User user2 = new User(user.getUserName(), user.getPassword(), Collections.emptyList());
			emailService.sendLoginDetailsMail(user.getEmail(), "Account Login Alert", sendLoginDtlsMail(username));
			return user2;
		}
		return null;
	}

	@Override
	public UserDataBindResponse findByUserName(String userName) throws UserNotFoundException {
		Optional<UserInfoEntity> userDetails = userRepository.findByUserName(userName);
		if (userDetails.isPresent()) {
			UserInfoEntity userInfo = userDetails.get();
			UserDataBindResponse userData = new UserDataBindResponse();
			BeanUtils.copyProperties(userInfo, userData);
			return userData;
		} // if
		else {
			throw new UserNotFoundException(AppConstants.USER_NOT_FOUND);
		} // else
	}// findUserById()

	@Override
	public UserDataBindResponse findByUserMail(String mail) throws UserNotFoundException {
		Optional<UserInfoEntity> userDetails = userRepository.findByEmail(mail);
		if (userDetails.isPresent()) {
			UserInfoEntity userInfo = userDetails.get();
			UserDataBindResponse userData = new UserDataBindResponse();
			BeanUtils.copyProperties(userInfo, userData);
			return userData;
		} // if
		else {
			throw new UserNotFoundException(AppConstants.USER_NOT_FOUND);
		} // else
	}// findUserById()

	@Override
	public String findUserImageById(Integer userId) throws UserNotFoundException {
		Optional<UserInfoEntity> id = userRepository.findById(userId);
		if (id.isPresent()) {
			return id.get().getProfilePath();
		} // if
		throw new UserNotFoundException("user Not Found");
	}

	/*
	 * @Override public UserDataBindResponse loginUser(UserLoginBind data) throws
	 * UserNotFoundException { Optional<UserInfoEntity> userInfo =
	 * userRepository.findByUserNameAndPassword(data.getUserName(),
	 * data.getPassword()); if (userInfo.isPresent()) { UserInfoEntity
	 * userInfoEntity = userInfo.get(); UserDataBindResponse response = new
	 * UserDataBindResponse(); BeanUtils.copyProperties(userInfoEntity, response);
	 * emailService.sendLoginDetailsMail(userInfoEntity.getEmail(),
	 * "Account Login Alert", sendLoginDtlsMail(userInfoEntity.getUserName()));
	 * log.info("Login Alert Sent Successfully"); return response; } // if else {
	 * throw new UserNotFoundException(AppConstants.USER_NOT_FOUND); } // else }//
	 * loginUser()
	 */

	@Override
	public List<UserDataBindResponse> findAllUsers() {
		List<UserInfoEntity> userInfo = userRepository.findAll();
		if (!userInfo.isEmpty()) {
			List<UserDataBindResponse> userData = new LinkedList<UserDataBindResponse>();
			for (UserInfoEntity entity : userInfo) {
				UserDataBindResponse userDataResponse = new UserDataBindResponse();
				BeanUtils.copyProperties(entity, userDataResponse);
				userData.add(userDataResponse);
			} // for
			log.info("Users Data Fetched Successfully");
			return userData;
		} // if
		else {
			throw new RuntimeException("No Data to be Fetch");
		} // else
	}// findAllUsers()

	@Override
	public List<UserDataBindResponse> findAllUsersBySortAsc() {
		Sort sort = Sort.by(Direction.ASC, AppConstants.FIELD_USER_NAME);
		List<UserInfoEntity> userInfo = userRepository.findAll(sort);
		List<UserDataBindResponse> userData = new LinkedList<UserDataBindResponse>();
		for (UserInfoEntity entity : userInfo) {
			UserDataBindResponse userDataResponse = new UserDataBindResponse();
			BeanUtils.copyProperties(entity, userDataResponse);
			userData.add(userDataResponse);
		} // for
		return userData;
	}// findAllUsersBySortAsc()

	@Override
	public List<UserDataBindResponse> findAllUsersBySortDesc() {
		Sort sort = Sort.by(Direction.DESC, AppConstants.FIELD_USER_NAME);
		List<UserInfoEntity> userInfo = userRepository.findAll(sort);
		List<UserDataBindResponse> userData = new LinkedList<UserDataBindResponse>();
		for (UserInfoEntity entity : userInfo) {
			UserDataBindResponse userDataResponse = new UserDataBindResponse();
			BeanUtils.copyProperties(entity, userDataResponse);
			userData.add(userDataResponse);
		} // for
		return userData;
	}// findAllUsersBySortDesc()

	@Override
	public UserDataBindResponsePage findAllUsersByPage(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<UserInfoEntity> page = userRepository.findAll(pageable);
		List<UserInfoEntity> content = page.getContent();
		List<UserDataBindResponse> userData = new LinkedList<UserDataBindResponse>();
		UserDataBindResponsePage pageDetails = new UserDataBindResponsePage();

		for (UserInfoEntity entity : content) {
			UserDataBindResponse userDataResponse = new UserDataBindResponse();
			BeanUtils.copyProperties(entity, userDataResponse);
			userData.add(userDataResponse);
			pageDetails.setUserDataBindResponse(userData);
		} // for

		pageDetails.setTotalPages(page.getTotalPages());
		pageDetails.setCurrentPage(page.getPageable().getPageNumber() + 1);
		pageDetails.setIsFirst(page.isFirst());
		pageDetails.setIsLast(page.isLast());
		pageDetails.setHasPrevious(page.hasPrevious());
		pageDetails.setHasNext(page.hasNext());
		pageDetails.setNoOfRecordsInCurrentPage(page.getNumberOfElements());
		pageDetails.setTotalNoOfRecords(page.getTotalElements());

		return pageDetails;
	}// findAllUsersByPage(-,-)

	@Override
	public UserDataBindResponsePage findAllUsersByPageAndSort(Integer pageNo, Integer pageSize) {
		Sort sort = Sort.by(Direction.ASC, AppConstants.FIELD_USER_NAME);
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<UserInfoEntity> page = userRepository.findAll(pageable);
		List<UserInfoEntity> content = page.getContent();
		List<UserDataBindResponse> userData = new LinkedList<UserDataBindResponse>();
		UserDataBindResponsePage pageDetails = new UserDataBindResponsePage();

		for (UserInfoEntity entity : content) {
			UserDataBindResponse userDataResponse = new UserDataBindResponse();
			BeanUtils.copyProperties(entity, userDataResponse);
			userData.add(userDataResponse);
			pageDetails.setUserDataBindResponse(userData);
		} // for

		pageDetails.setTotalPages(page.getTotalPages());
		pageDetails.setCurrentPage(page.getPageable().getPageNumber() + 1);
		pageDetails.setIsFirst(page.isFirst());
		pageDetails.setIsLast(page.isLast());
		pageDetails.setHasPrevious(page.hasPrevious());
		pageDetails.setHasNext(page.hasNext());
		pageDetails.setNoOfRecordsInCurrentPage(page.getNumberOfElements());
		pageDetails.setTotalNoOfRecords(page.getTotalElements());

		return pageDetails;
	}// findAllUsersByPageAndSort

	@Override
	public String updateUserData(UserDataBindRequest request) {
		Optional<UserInfoEntity> optional = userRepository.findByUserName(request.getUserName());
		if (optional.isPresent()) {
			UserInfoEntity userInfoEntity = optional.get();
			String profilePath = userInfoEntity.getProfilePath();
			BeanUtils.copyProperties(request, userInfoEntity);

			Long mobile = Long.valueOf(request.getMobile());
			userInfoEntity.setProfilePath(profilePath);;
			userInfoEntity.setMobile(mobile);
			userRepository.save(userInfoEntity);

			return AppConstants.UPDATE_SUCCESS;
		} // if
		else {
			return AppConstants.USER_NOT_FOUND;
		} // else
	}// updateUserData()

	@Override
	public String updateUserName(String oldUserName, String newUserName) {
		Optional<UserInfoEntity> optional = userRepository.findByUserName(oldUserName);
		if (optional.isPresent()) {
			UserInfoEntity userInfoEntity = optional.get();
			userInfoEntity.setUserName(newUserName);
			userRepository.save(userInfoEntity);
			return AppConstants.UPDATE_SUCCESS;
		} else {
			return AppConstants.USER_NOT_FOUND;
		}
	}

	@Override
	public String deleteUserById(Integer userId) throws UserNotFoundException {
		Optional<UserInfoEntity> optional = userRepository.findById(userId);
		if (optional.isPresent()) {
			UserInfoEntity data = optional.get();
			userRepository.deleteById(userId);
			emailService.sendDeletionMail(data.getEmail(), "Your Account Has Been Deleted",
					sendDelMailBody(data.getUserName()));
			log.info("Deletion Mail Sent Successfully");
			return AppConstants.USER_DATA_DELETED;
		} // if
		else {
			throw new UserNotFoundException(AppConstants.USER_NOT_FOUND);
		} // else
	}// deleteUserById()

	@Override
	public String deleteAllUsers() {
		List<UserInfoEntity> list = userRepository.findAll();
		if (list.isEmpty()) {
			userRepository.deleteAll();
			for (UserInfoEntity entity : list) {
				UserDataBindResponse bindResponse = new UserDataBindResponse();
				BeanUtils.copyProperties(entity, bindResponse);
				emailService.sendDeletionMail(bindResponse.getEmail(), "Your Account Deactivated",
						sendDelMailBody(bindResponse.getUserName()));
			} // for
			return AppConstants.USER_DATA_DELETED;
		} // if
		else {
			throw new RuntimeException("No Data to be Delete");
		} // else
	}// deleteAllUsers()

	private String sendRegMailBody(String userName) {
		String mailBody = null;
		try (FileReader file = new FileReader("REGISTRATION_BODY.txt");
				BufferedReader reader = new BufferedReader(file);) {
			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				builder.append(line);
				line = reader.readLine();
			} // while
			mailBody = builder.toString();
			mailBody = mailBody.replace("{UserName}", userName);

		} // try
		catch (Exception e) {
			log.error(AppConstants.EXCEPTION + " " + e.getMessage());
		} // catch
		return mailBody;
	}// sendRegMailBody(-)

	private String sendLoginDtlsMail(String userName) {
		String mailBody = null;
		try (FileReader file = new FileReader("LOGIN_DETAILS_BODY.txt");
				BufferedReader reader = new BufferedReader(file);) {
			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				builder.append(line);
				line = reader.readLine();
			} // while
			mailBody = builder.toString();
			mailBody = mailBody.replace("{USERNAME}", userName);
			mailBody = mailBody.replace("{DATETIME}", LocalDateTime.now().toString());
			Format format = new SimpleDateFormat("EEEE");
			String day = format.format(new Date());
			mailBody = mailBody.replace("{DAY}", day);
			mailBody = mailBody.replace("{OSNAME}", System.getProperty("os.name"));
		} // try
		catch (Exception e) {
			log.error(AppConstants.EXCEPTION + " " + e.getMessage());
		} // catch
		return mailBody;
	}// sendRegMailBody(-)

	private String sendDelMailBody(String userName) {
		String mailBody = null;
		try (FileReader file = new FileReader("DELETION_DATA_BODY.txt");
				BufferedReader reader = new BufferedReader(file);) {
			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				builder.append(line);
				line = reader.readLine();
			} // while
			mailBody = builder.toString();
			mailBody = mailBody.replace("{UserName}", userName);
		} // try
		catch (Exception e) {
			log.error(AppConstants.EXCEPTION + " " + e.getMessage());
		} // catch
		return mailBody;
	}// sendMailBody(-)

}// class