package com.mvc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users_Entity")
//@SQLDelete(sql = "update Users_Info_Entity set STATUS='INACTIVE' where USER_NAME=?")
//@SQLRestriction(value = "STATUS <> 'INACTIVE'")
//@PropertySource(value = {"application.properties"})
public class UserInfoEntity {
	@Id
	@SequenceGenerator(initialValue = 1000, name = "generator", sequenceName = "USER_API_GENERATOR", allocationSize = 1)
	@GeneratedValue(generator = "generator", strategy = GenerationType.SEQUENCE)
	@Column(name = "USER_ID")
	private Integer userId;

	private String profilePath;

	@Column(name = "CONTENT_TYPE")
	private String contentType;

	@Column(name = "USER_NAME", length = 25)
	private String userName;

	@Column(length = 70)
	private String password;

	@Column(length = 35, nullable = false)
	private String email;

	@Column(name = "FULL_NAME", length = 30, nullable = false)
	private String fullName;

	@Column(length = 30, nullable = false)
	private String address;

	@Column(length = 30, nullable = false)
	private String city;

	@Column(length = 30, nullable = false)
	private String state;

	@Column(nullable = false)
	private Long mobile;

	@Column(length = 10)
	private Boolean isActive = true;
}//CLASS