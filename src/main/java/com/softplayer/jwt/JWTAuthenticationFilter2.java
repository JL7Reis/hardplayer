package com.softplayer.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.net.HttpHeaders;
import com.softplayer.service.AuthenticationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthenticationFilter2 extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {

		try {
			String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
			if (token != null && AuthenticationService.validateJwtToken(token)) {
				Authentication authentication = AuthenticationService.getAuthentication(httpServletRequest);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				AuthenticationService.addAuthentication(httpServletResponse, AuthenticationService.getUserNameFromJwtToken(token));
			}
		} catch (Exception e) {
			log.error("Can NOT set user authentication ", e);
		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}
}