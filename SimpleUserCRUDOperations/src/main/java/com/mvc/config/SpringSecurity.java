package com.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mvc.filter.SecurityFilter;
import com.mvc.service.UserInfoService;

import lombok.SneakyThrows;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

	@Autowired
	private SecurityFilter filter;

	@Autowired
	private UserInfoService userInfoService;

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userInfoService);

		return provider;
	}

	@Bean
	@SneakyThrows
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
		return configuration.getAuthenticationManager();
	}

	@Bean
	@SuppressWarnings({"removal"})
	SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		return security.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable()).authorizeHttpRequests()
				.requestMatchers("/user/", "/user/register", "/user/registerAll","/user/login").permitAll().and()
				.authorizeHttpRequests()
				.requestMatchers("/user/findAllUsers","/user/update","/user/find/mail/{mail}","/user/update/{oldUserName}/{newUserName}", "/user/findAllUsers/sort/desc",
						"/user/findAllUsers/bypage/{pageNo}/{pageSize}",
						"/user/findAllUsers/bypage/{pageNo}/{pageSize}/asc", "/user/find/username/{userName}",
						"/user/delete/{userName}","/user/image/{id}","/user/token/{token}")
				.authenticated().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build();
	}
}