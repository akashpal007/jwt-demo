package com.springboot.jwt.demo.service;

import java.util.List;

import com.springboot.jwt.demo.entity.UserEntity;

public interface UserService {
	UserEntity saveUser(UserEntity user);

	void addRoleToUser(String userName, String roleName);

	UserEntity gatUser(String userName);

	List<UserEntity> gatUsers();
}
