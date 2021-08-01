package com.springboot.jwt.demo.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.jwt.demo.entity.RoleEntity;
import com.springboot.jwt.demo.entity.UserEntity;
import com.springboot.jwt.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthenticationController {
	@Autowired
	UserService userService;

	private String secret = "8p2qLo97kPGBwmMS";

	@PostMapping("/refresh-token")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Refresh_Bearer ")) {
			try {
				String refreshToken = authorizationHeader.substring("Refresh_Bearer ".length());

				Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				/* Verify token */
				DecodedJWT decodedJWT = verifier.verify(refreshToken);
				String username = decodedJWT.getSubject();
				/* Fetch user */
				UserEntity user = userService.gatUser(username);
				/* Generate access token */
				String accessToken = JWT.create().withSubject(user.getUserName())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles",
								user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toList()))
						.sign(algorithm);
				
				Map<String, String> token = new HashMap<>();
				token.put("access_token", accessToken);
				token.put("refresh_token", refreshToken);

				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), token);

			} catch (Exception exception) {
				log.error("Error in Refresh token: {}", exception.getMessage());

				response.setHeader("error", exception.getMessage());

				/* Sending token in the body */
				Map<String, String> error = new HashMap<>();
				error.put("error_message", exception.getMessage());

				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		} else {
			throw new RuntimeException("Refresh token is missing");
		}
	}

}
