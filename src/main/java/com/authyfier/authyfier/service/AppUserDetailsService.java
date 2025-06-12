package com.authyfier.authyfier.service;

import com.authyfier.authyfier.entity.UserEntity;
import com.authyfier.authyfier.repository.UserRepositry;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {


	private UserRepositry userRepositry;


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity existingUser =  userRepositry.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	return new User(existingUser.getEmail(), existingUser.getPassword(), new ArrayList<>());
	}



}
