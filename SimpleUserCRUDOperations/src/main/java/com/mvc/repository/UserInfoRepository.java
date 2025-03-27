package com.mvc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mvc.entity.UserInfoEntity;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer> {
//	Optional<UserInfoEntity> findByUserNameAndPassword(String userName, String password);

	Optional<UserInfoEntity> findByUserName(String userName);

	Optional<UserInfoEntity> findByEmail(String mail);

	//	List<UserInfoEntity> findAllByUserName(List<String> userNames);
}// interface