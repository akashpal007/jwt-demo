package com.springboot.jwt.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springboot.jwt.demo.entity.RoleEntity;
import com.springboot.jwt.demo.entity.UserEntity;
import com.springboot.jwt.demo.service.RoleService;
import com.springboot.jwt.demo.service.UserService;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/* Run once to set up initial data to work with :) */
//	@Bean
//	CommandLineRunner run(UserService userService, RoleService roleService) {
//		return args -> {
//			userService.saveUser(new UserEntity(null, "Akash", "akash", "12345", null));
//			userService.saveUser(new UserEntity(null, "Nil", "nil", "12345", null));
//			userService.saveUser(new UserEntity(null, "Sam", "sam", "12345", null));
//
//			roleService.saveRole(new RoleEntity(null, "ROLE_USER"));
//			roleService.saveRole(new RoleEntity(null, "ROLE_MANAGER"));
//			roleService.saveRole(new RoleEntity(null, "ROLE_ADMIN"));
//			roleService.saveRole(new RoleEntity(null, "ROLE_SUPER_ADMIN"));
//
//			userService.addRoleToUser("akash", "ROLE_USER");
//			userService.addRoleToUser("akash", "ROLE_ADMIN");
//			userService.addRoleToUser("nil", "ROLE_MANAGER");
//			userService.addRoleToUser("sam", "ROLE_SUPER_ADMIN");
//		};
//	}
}
