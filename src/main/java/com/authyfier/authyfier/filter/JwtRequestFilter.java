package com.authyfier.authyfier.filter;

import com.authyfier.authyfier.service.AppUserDetailsService;
import com.authyfier.authyfier.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

	private final AppUserDetailsService appUserDetailsService;
	private final JwtUtil jwtUtil;

	private static final List<String> PUBLIC_ENDPOINTS = List.of(
			"/login",
			"/register",
			"/send-reset-otp",
			"/reset-password",
			"/logout"
	);


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws java.io.IOException, ServletException {
		String path = request.getServletPath();
		if (PUBLIC_ENDPOINTS.contains(path)) {
			filterChain.doFilter(request, response); // Skip authentication for public endpoints
			return;
		}


		String jwt = null;
		String email = null;


		final String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			jwt = authHeader.substring(7); // Extract JWT token
		}


		if (jwt == null){
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("jwt".equals(cookie.getName())) {
						jwt = cookie.getValue(); // Extract JWT token from cookie
						break;
					}
				}
			}
		}

		if (jwt != null){
			email = jwtUtil.extractEmail(jwt);
			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
				var userDetails = appUserDetailsService.loadUserByUsername(email);
				if (jwtUtil.validateToken(jwt, userDetails)) {
					var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
				return; // Stop further processing if token is invalid
			}
		}

		filterChain.doFilter(request, response); // Continue the filter chain
	}


}
