package com.example.Arunlearn.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Arunlearn.service.JwtService;
import com.example.Arunlearn.service.UserDetailsImp;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtservice;

	@Autowired
	private UserDetailsImp userDetails;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		// TODO Auto-generated method stub

		final String authHeader = request.getHeader("Authorization");

		String token = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {

			token = authHeader.substring(7);

		} else {
			filterChain.doFilter(request, response);
			return;
		}

		String username = jwtservice.extractUsername(token);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails user = userDetails.loadUserByUsername(username);
			
			if(jwtservice.isValid(token, user)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null , user.getAuthorities());
				
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}

		}
		filterChain.doFilter(request, response);

	}

}
