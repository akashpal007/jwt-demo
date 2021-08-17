package com.springboot.jwt.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.jwt.demo.common.Roles;
import com.springboot.jwt.demo.entity.RoleEntity;
import com.springboot.jwt.demo.entity.UserEntity;
import com.springboot.jwt.demo.repository.RoleRepository;
import com.springboot.jwt.demo.repository.UserRepository;
import com.springboot.jwt.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/* This is from spring security UserDetailsService interface */
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserEntity userResponse = userRepo.findByUserName(userName).orElse(null);
		if (userResponse != null) {
			log.error("User found in the DB, userName: {}", userName);

			/* Setting spring defined authority from user define authority */
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();
			userResponse.getRoles().forEach(role -> {
				authorities.add(new SimpleGrantedAuthority(role.getName().toString()));
			});

			return new User(userResponse.getUserName(), userResponse.getPassword(), authorities);
		} else {
			log.error("User not found in the DB, userName: {}", userName);
			throw new UsernameNotFoundException("User not found in the DB.");
		}
	}

	@Override
	public UserEntity saveUser(UserEntity user) {
		log.info("SAVE NEW USER TO DB: " + user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

	@Override
	public void addRoleToUser(String userName, Roles roleName) {
		UserEntity userResponse = userRepo.findByUserName(userName).orElse(null);
		RoleEntity roleResponse = roleRepo.findByName(roleName).orElse(null);

		if (userResponse != null && roleResponse != null) {
			userResponse.getRoles().add(roleResponse);
			log.info("Add Role {} To User {}", roleResponse.getName(), userResponse.getName());
		} else {
			log.info("User Name or Role Name is not present in DB");
		}

	}

	@Override
	public UserEntity gatUser(String userName) {
		log.info("Finding user {}", userName);
		UserEntity userResponse = userRepo.findByUserName(userName).orElse(null);
		if (userResponse != null) {
			return userResponse;
		} else {
			log.info("User Name is not present in DB");
			return null;
		}
	}

	@Override
	public List<UserEntity> gatUsers() {
		return userRepo.findAll();
	}

}
