package com.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleUserCrudOperationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleUserCrudOperationsApplication.class, args);

	}//main
	/*
		@Bean
		FilterRegistrationBean<CorsFilter> corsFilter() {
			FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
			registrationBean.setFilter(new CorsFilter());
			registrationBean.addUrlPatterns("/*");
			return registrationBean;
		}*/
	/*@Bean
	CommandLineRunner runner(ApplicationContext ctx) {
		return args->{
			int count = ctx.getBeanDefinitionCount();
			System.out.println("Beans Count ::"+count);
			String[] beans = ctx.getBeanDefinitionNames();
			Arrays.sort(beans);
			for(String bean:beans) {
				System.out.println(bean);
			}//for
		};
	}*/
}//class