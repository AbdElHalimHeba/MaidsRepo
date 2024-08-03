package cc.maids.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cc.maids.security.AuthorizationFilter;

@Configuration
public class SecurityConfig {

	@Autowired
	private AuthorizationFilter filter;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		return http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				.requestMatchers("/api/borrow/**", "/api/return/**").permitAll()
				.anyRequest().authenticated()
		)
		.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
		.sessionManagement(sessionManagement -> sessionManagement
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		).build();				
	}
	
}