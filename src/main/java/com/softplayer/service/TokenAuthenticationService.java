package com.softplayer.service;

import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softplayer.domain.LoginPayload;
import com.softplayer.domain.ReturnObject;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenAuthenticationService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ReturnObject ro;
	
	private static final long EXPIRATION_TIME = 60 * 60 * 1000;
	static final String SECRET = "pj0ug8sXjx826202OFRrwC2PqaM6EWQrpHgMzyhmq60GkDqZQzaH7rSyoirlNOf4eXL0LxPZQ2K1iROwobXCAA";
	static final String TOKEN_PREFIX = "Bearer";
	
	public void addAuthentication(HttpServletResponse response, String username) {
		response.addHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + " " + generateJwt(username));
	}
	
	public ReturnObject setLogin(LoginPayload LoginPayload) {
		
		try {
			loginSpringSecurity(LoginPayload);
			
			ro.setResult(generateJwt(LoginPayload.getUsername()));
			ro.setStatus(HttpStatus.OK.value());
		} catch (Exception e) {
			ro.setMessage(e.getMessage());
			ro.setStatus(HttpStatus.BAD_REQUEST.value());
			log.error(e.getMessage());
		}
		return ro;
	}
	
	public static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if (token != null) {
			String user = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody()
					.getSubject();
			
			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
			}
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	private String generateJwt(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
	}
	
	private void loginSpringSecurity(LoginPayload LoginPayload) {
		UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(LoginPayload.getUsername(), LoginPayload.getPassword());
		Authentication auth = authenticationManager.authenticate(authReq);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}