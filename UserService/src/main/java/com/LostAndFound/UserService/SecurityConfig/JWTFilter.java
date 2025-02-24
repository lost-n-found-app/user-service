package com.LostAndFound.UserService.SecurityConfig;


import com.LostAndFound.UserService.repository.UserRepository;
import com.LostAndFound.UserService.service.JwtService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {
        UsernamePasswordAuthenticationToken authToken;
        boolean userExists = true;
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String number;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        number = jwtService.extractNumber(jwt);

        if (number != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.validateToken(jwt, number)) {

                userExists = userRepository.existsByPhoneNumber(number);
                if (userExists) {
                    authToken = new UsernamePasswordAuthenticationToken(
                            number, null, null);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Phone number not found in the database");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
