package com.springboot.jwt.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.jwt.demo.entity.RoleEntity;
import com.springboot.jwt.demo.service.RoleService;

@RestController
@RequestMapping("/api/role")
public class RoleController {
	@Autowired
	private RoleService roleService;

	@PostMapping("/save")
	public ResponseEntity<Long> saveRole(RoleEntity role) {
		RoleEntity response = roleService.saveRole(role);
		return ResponseEntity.ok().body(response.getId());
	}

	@GetMapping("/find-all")
	public ResponseEntity<List<RoleEntity>> getRoles() {
		List<RoleEntity> roles = roleService.getRoles();
		return ResponseEntity.ok().body(roles);
	}

}
