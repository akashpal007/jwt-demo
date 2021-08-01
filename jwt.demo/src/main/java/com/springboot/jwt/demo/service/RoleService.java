package com.springboot.jwt.demo.service;

import java.util.List;

import com.springboot.jwt.demo.entity.RoleEntity;

public interface RoleService {
	RoleEntity saveRole(RoleEntity role);
	List<RoleEntity> getRoles();
}
