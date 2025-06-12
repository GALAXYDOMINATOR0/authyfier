package com.authyfier.authyfier.config;

import com.authyfier.authyfier.filter.JwtRequestFilter;
import com.authyfier.authyfier.service.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final AppUserDetailsService appUserDetailsService;
	private final JwtRequestFilter jwtRequestFilter;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/login", "/register","/send-reset-otp","/reset-password","/logout")
						.permitAll().anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.logout(AbstractHttpConfigurer::disable)
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint));
				return httpSecurity.build();

	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsFilter corsFilter(){
		return new CorsFilter(corsConfigurationSource());
	}

	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin(String.valueOf(List.of("http://localhost:3000")));
		configuration.addAllowedMethod(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS").toString());
		configuration.addAllowedHeader(List.of("Authorization", "Content-Type").toString());
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


	@Bean
	public AuthenticationManager authenticationManager(){
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(appUserDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(daoAuthenticationProvider);
	}

}
