package com.springboot.jwt.demo.controller;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.springboot.jwt.demo.dto.request.AuthenticationAccessTokenRequest;
import com.springboot.jwt.demo.dto.request.AuthenticationRefreshTokenRequest;
import com.springboot.jwt.demo.dto.response.AuthenticationResponse;
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

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	private String secret = "8p2qLo97kPGBwmMS";

	@PostMapping({ "/refresh-token", "/login" })
	public AuthenticationResponse refreshToken(@RequestBody AuthenticationRefreshTokenRequest request)
			throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
		} catch (Exception e) {
			log.error("Incorrect username or password: {}", e.getMessage());
			return new AuthenticationResponse(request.getUserName(), null, null, e.getLocalizedMessage());
		}

		User user = (User) userDetailsService.loadUserByUsername(request.getUserName());

		Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
		String accessToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				.withClaim("roles",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);

		String refreshToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000)).sign(algorithm);

		return new AuthenticationResponse(request.getUserName(), accessToken, refreshToken,
				"Access token and refresh token generated successfully.");
	}

	@PostMapping({ "/access-token" })
	public AuthenticationResponse accessToken(@RequestBody AuthenticationAccessTokenRequest request) throws Exception {
		/*currently there is no user name password validation in the code.*/
		if (request.getRefreshToken() != null) {
			try {
				Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				/* Verify token */
				DecodedJWT decodedJWT = verifier.verify(request.getRefreshToken());
				String username = decodedJWT.getSubject();
				/* Fetch user */
				UserEntity user = userService.gatUser(username);
				/* Generate access token */
				String accessToken = JWT.create().withSubject(user.getUserName())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withClaim("roles",
								user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toList()))
						.sign(algorithm);

				return new AuthenticationResponse(request.getUserName(), accessToken, request.getRefreshToken(),
						"Access token generated successfully.");
			} catch (Exception e) {
				log.error("Error in Access token: {}", e.getMessage());
				return new AuthenticationResponse(request.getUserName(), null, null, e.getLocalizedMessage());
			}
		} else {
			log.error("Refresh token is missing.");
			return new AuthenticationResponse(request.getUserName(), null, null, "Refresh token is missing.");
		}
	}

}
