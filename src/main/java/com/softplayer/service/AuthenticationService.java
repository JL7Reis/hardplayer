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

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthenticationService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ReturnObject ro;
	
	private static final long EXPIRATION_TIME = 60 * 60 * 1000;
	private static final String SECRET = "pj0ug8sXjx826202OFRrwC2PqaM6EWQrpHgMzyhmq60GkDqZQzaH7rSyoirlNOf4eXL0LxPZQ2K1iROwobXCAA";
	private static final String TOKEN_PREFIX = "Bearer";
	private static final String HEADER_CUSTOM_HEADER = "Access-Control-Expose-Headers";
	
	public ReturnObject setLogin(LoginPayload LoginPayload) {
		
		try {
			authSpringSecurity(LoginPayload);
			ro.setReturnObjectOk(generateJwtToken(LoginPayload.getUsername()));
		} catch (Exception e) {
			log.error(e.getMessage());
			ro.setReturnObjectError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
		return ro;
	}
	
	public static void addAuthentication(HttpServletResponse response, String username) {
		response.addHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + " " + generateJwtToken(username));
		response.addHeader(HEADER_CUSTOM_HEADER, HttpHeaders.AUTHORIZATION);
	}
	
	public static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if (token != null) {
			String user = getUserNameFromJwtToken(token);
			
			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
			}
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static String generateJwtToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
	}
	
	public static String getUserNameFromJwtToken(String token) {
		log.debug(">> " + token);
		return Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody()
				.getSubject();
	}
	
    public static boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, ""));
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
        	log.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
        	log.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
        	log.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
        	log.error("JWT claims string is empty -> Message: {}", e);
        }
        return false;
    }
	
	private void authSpringSecurity(LoginPayload LoginPayload) {
		UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(LoginPayload.getUsername(), LoginPayload.getPassword());
		Authentication auth = authenticationManager.authenticate(authReq);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}