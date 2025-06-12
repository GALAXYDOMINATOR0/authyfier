package com.authyfier.authyfier.controller;


import com.authyfier.authyfier.io.ProfileRequest;
import com.authyfier.authyfier.io.ProfileResponse;
import com.authyfier.authyfier.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ProfileResponse register(@Valid @RequestBody ProfileRequest profileRequest) {
		ProfileResponse response = profileService.createProfile(profileRequest);
		return response;
	}

}
