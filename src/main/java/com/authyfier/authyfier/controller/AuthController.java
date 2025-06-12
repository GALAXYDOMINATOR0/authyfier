package com.authyfier.authyfier.controller;

import com.authyfier.authyfier.io.AuthRequest;
import com.authyfier.authyfier.io.AuthResponse;
import com.authyfier.authyfier.service.AppUserDetailsService;
import com.authyfier.authyfier.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {


	private final AuthenticationManager authenticationManager;
	private final AppUserDetailsService appUserDetailsService;

	private final JwtUtil jwtUtil;


	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
		try {
			authenticate(authRequest.getEmail(), authRequest.getPassword());
			final UserDetails userDetails = appUserDetailsService.loadUserByUsername(authRequest.getEmail());
			final String jwtToken = jwtUtil.generateToken(userDetails);
			ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
					.path("/")
					.httpOnly(true)
					.maxAge(Duration.ofDays(1))
					.sameSite("Strict")
					.build();
			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
					.body(new AuthResponse(authRequest.getEmail(), jwtToken));
		} catch (BadCredentialsException ex) {

			Map<String, Object> error = new HashMap<>();
			error.put("error", true);
			error.put("message", "Invalid email or password");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		} catch (DisabledException ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", true);
			error.put("message", "User is disabled");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		} catch (Exception ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", true);
			error.put("message", "An unexpected error occurred");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}

	private void authenticate(String email, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
	}

}
