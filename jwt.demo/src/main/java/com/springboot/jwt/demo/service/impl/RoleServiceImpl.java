package com.springboot.jwt.demo.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.jwt.demo.entity.RoleEntity;
import com.springboot.jwt.demo.repository.RoleRepository;
import com.springboot.jwt.demo.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepo;

	@Override
	public RoleEntity saveRole(RoleEntity role) {
		log.info("SAVE NEW ROLE TO DB: " + role);
		return roleRepo.save(role);
	}

	@Override
	public List<RoleEntity> getRoles() {
		return roleRepo.findAll();
	}

}
