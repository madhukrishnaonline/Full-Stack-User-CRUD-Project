package com.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mvc.controller.UserController;

@WebMvcTest(value = UserController.class)
class UserInfoEntityServiceImplTest {

	@MockBean
	private UserInfoService service;

	@Autowired
	private MockMvc mockMvc;

	/*@Test//(expected=NullPointerException.class)
	void registerUserWithData() throws Exception {
		UserDataBindRequest info = new UserDataBindRequest();
		info.setUserName("madhukrishna");
		info.setPassword("xxxxxxxxxx");
		info.setFullName("Madhu Krishna");
		info.setEmail("madhu@gmail.com");
		info.setAddress("Kotapeta");
		info.setCity("Betamcherla");
		info.setState("Andhra Pradesh");
	
		ObjectMapper mapper = new ObjectMapper();
		String jsonInfo = mapper.writeValueAsString(info);
		when(service.registerUser(info)).thenReturn("Registration Successfull");
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/register")
				.contentType("application/json").content(jsonInfo);
	
		MvcResult mockResult = mockMvc.perform(mockHttpServletRequestBuilder).andReturn();
	
		MockHttpServletResponse response = mockResult.getResponse();
		int status = response.getStatus();
	
		assertEquals(201, status);
	}*/

	/*@Test
	void registerUserWithoutData() throws Exception {
		UserDataBindRequest info = new UserDataBindRequest();
	
		when(service.registerUser(info)).thenReturn("Registration Failed");
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/register");
	
		MvcResult mockResult = mockMvc.perform(mockHttpServletRequestBuilder).andReturn();
	
		MockHttpServletResponse response = mockResult.getResponse();
		int status = response.getStatus();
	
		assertEquals(400, status);
	}*/

	/*@Test
	void findUserByIdWithId() throws Exception {
		UserDataBindResponse info = new UserDataBindResponse();
		
		when(service.findBy(1099)).thenReturn(info);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/find/madhukrishna");
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		int status = result.getResponse().getStatus();
		
		assertEquals(302, status);
	}//findUserById
	*/	
	/*@Test
	void findUserByIdWithoutId() throws Exception {
		UserDataBindResponse info = new UserDataBindResponse();
		
		when(service.findUserById(null)).thenReturn(info);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/find/");
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		int status = result.getResponse().getStatus();
		
		assertEquals(404, status);
	}//findUserById
	*/
}//class