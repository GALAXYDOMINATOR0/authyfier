package com.authyfier.authyfier.service.Impl;

import com.authyfier.authyfier.entity.UserEntity;
import com.authyfier.authyfier.io.ProfileRequest;
import com.authyfier.authyfier.io.ProfileResponse;
import com.authyfier.authyfier.repository.UserRepositry;
import com.authyfier.authyfier.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {


	@Autowired
	private UserRepositry userRepositry;
	private final PasswordEncoder passwordEncoder;


	@Override
	public ProfileResponse createProfile(ProfileRequest profileRequest) {
		if (userRepositry.existsByEmail(profileRequest.getEmail())) {
			throw new RuntimeException("Email already registered");
		}

		UserEntity newProfile = contertToUserEntity(profileRequest);
		newProfile = userRepositry.save(newProfile);

		return convertToProfileRespone(newProfile);
	}



	private UserEntity contertToUserEntity(ProfileRequest profileRequest){
	    return UserEntity.builder()
				.email(profileRequest.getEmail())
				.userId(UUID.randomUUID().toString())
				.name(profileRequest.getName())
				.password(passwordEncoder.encode(profileRequest.getPassword()))
				.isVerified(false)
				.resetOtpExpiryAt(0L)
				.VerifyOTP(null)
				.VerifyOtpExpiry(0L)
				.resetOTP(null)
				.build();
	}


	private ProfileResponse convertToProfileRespone(UserEntity user){
		return ProfileResponse.builder()
				.name(user.getName())
				.email(user.getEmail())
				.userId(user.getUserId())
				.isVerified(user.getIsVerified())
				.build();
	}




}
