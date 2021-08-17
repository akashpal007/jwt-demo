package com.springboot.jwt.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.jwt.demo.common.Roles;
import com.springboot.jwt.demo.entity.UserEntity;
import com.springboot.jwt.demo.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/save")
	public ResponseEntity<Long> saveUser(@RequestBody UserEntity request) {
		UserEntity response = userService.saveUser(request);
		return ResponseEntity.ok().body(response.getId());
	}

	@PostMapping("/add-role")
	public ResponseEntity<Object> addRoleToUser(@RequestParam String userName, @RequestParam Roles roleName) {
		userService.addRoleToUser(userName, roleName);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/find")
	public ResponseEntity<UserEntity> gatUser(@RequestParam String userName) {
		UserEntity user = userService.gatUser(userName);
		return ResponseEntity.ok().body(user);
	}

	@GetMapping("/find-all")
	public ResponseEntity<List<UserEntity>> gatUsers() {
		List<UserEntity> users = userService.gatUsers();
		return ResponseEntity.ok().body(users);
	}

}
