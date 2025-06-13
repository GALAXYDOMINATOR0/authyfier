package com.authyfier.authyfier.controller;


import com.authyfier.authyfier.io.ProfileRequest;
import com.authyfier.authyfier.io.ProfileResponse;
import com.authyfier.authyfier.service.EmailService;
import com.authyfier.authyfier.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;
	private final EmailService emailService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ProfileResponse register(@Valid @RequestBody ProfileRequest profileRequest) {
		ProfileResponse response = profileService.createProfile(profileRequest);
		emailService.sendWelcomeMail(response.getEmail(), response.getName());
		return response;
	}

	@GetMapping("/profile")
	public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email) {
		return profileService.getProfile(email);
	}

//	@GetMapping("/test")
//	public String test() {
//		return "Working";
//	}

}
