package com.softplayer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;
import com.softplayer.jwt.JWTAuthenticationFilter;
import com.softplayer.util.CustomAccessDeniedHandler;
import com.softplayer.util.CustomAuthenticationEntryPoint;

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
			    .antMatchers("/login**").permitAll()
			    .antMatchers("/logout").permitAll()
			    .anyRequest().authenticated()
			.and()
				.logout()
				.logoutUrl("/logout")
			    .invalidateHttpSession(true)
			    .clearAuthentication(true)
			    .deleteCookies("JSESSIONID")
				.permitAll()
			.and()
				.exceptionHandling()
				.accessDeniedHandler(new CustomAccessDeniedHandler())
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
			.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
//				.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
//		                UsernamePasswordAuthenticationFilter.class)
				
				.addFilterBefore(new JWTAuthenticationFilter(),
		                UsernamePasswordAuthenticationFilter.class);
        } catch(Exception e) {
			log.error(e.getMessage(), e);
		}
    }
	
	@Override
	public void configure(WebSecurity web) {
	    try {
			web.ignoring().antMatchers("/v2/api-docs",
					                    "/configuration/ui",
					                    "/swagger-resources/**",
					                    "/configuration/security",
					                    "/swagger-ui.html",
					                    "/swagger2.json",
					                    "/webjars/**");
	    } catch(Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		try {
			auth.inMemoryAuthentication()
				.withUser("softplayer").password(passwordEncoder().encode("softplayer")).roles("USER")
				.and()
				.withUser("hardplayer").password(passwordEncoder().encode("hardplayer")).roles("ADMIN");
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
    }
	
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
    	return super.authenticationManager();
    }
	
    @Bean
	protected BCryptPasswordEncoder passwordEncoder(){ 
        return new BCryptPasswordEncoder(); 
    }
	
	@Bean
	protected CorsConfigurationSource corsConfigurationSource() {
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