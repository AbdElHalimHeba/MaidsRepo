package cc.maids.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		String role = request.getHeader("ROLE");
		
	    if (role != null && role.equals("ADMIN")) {
			SecurityContextHolder.getContext()
				.setAuthentication(new UsernamePasswordAuthenticationToken("ADMIN", null, null));
		}
		
		chain.doFilter(request, response);
		
	}
		
}
