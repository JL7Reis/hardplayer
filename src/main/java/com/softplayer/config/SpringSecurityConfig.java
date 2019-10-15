package com.softplayer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;
import com.softplayer.jwt.JWTAuthenticationFilter;
import com.softplayer.jwt.JWTLoginFilter;
import com.softplayer.util.CustomAccessDeniedHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
    protected void configure(HttpSecurity http) {
        try {
			http.csrf().disable()
				.cors()
				.and()
			    .authorizeRequests()
			    .antMatchers(HttpMethod.POST, "/token/generate-token").permitAll()
			    .anyRequest().authenticated()
		    .and()
			    .httpBasic()
			.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/logout")
			    .invalidateHttpSession(true)
			    .clearAuthentication(true)
			    .deleteCookies()
				.permitAll()
			.and()
				.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
			.and()
				.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
		                UsernamePasswordAuthenticationFilter.class)
				
				.addFilterBefore(new JWTAuthenticationFilter(),
		                UsernamePasswordAuthenticationFilter.class);
        } catch(Exception e) {
			log.error(e.getMessage(), e);
		}
    }
	
	@Override
	public void configure(WebSecurity web) {
	    try {
			web.ignoring().antMatchers(HttpMethod.GET, "/source");
	    } catch(Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		try {
			auth.inMemoryAuthentication()
				.withUser("softplayer").password("{noop}softplayer").roles("USER")
				.and()
				.withUser("hardplayer").password("{noop}hardplayer").roles("ADMIN");
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
    }
	
	@Bean
    protected AuthenticationManager authenticationManager() throws Exception {
    	return super.authenticationManager();
    }
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}