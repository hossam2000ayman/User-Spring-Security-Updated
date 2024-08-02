package com.example.springsecurityjwt.config;

import com.example.springsecurityjwt.entity.User;
import com.example.springsecurityjwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter class for JWT authentication in Spring Security.
 *
 * This filter intercepts each HTTP request to check for a JWT token in the Authorization header.
 * If a valid token is found, the user's authentication details are set in the security context.
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Extracts the JWT token from the Authorization header.</li>
 *   <li>Validates the token and extracts the username from it.</li>
 *   <li>Loads user details and sets the authentication in the security context if the token is valid.</li>
 *   <li>Ensures the filter runs once per request by extending OncePerRequestFilter.</li>
 * </ul>
 */

@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    /**
     * Filters each HTTP request to check for a valid JWT token.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet exception occurs
     * @throws IOException if an IO exception occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get Authorization Header value
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String username;

        // If the authorization header is null or does not start with "Bearer ", pass the request to the next filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        //get the json web token through HttpHeaders Authorization after "Bearer "
        jwt = authHeader.substring(7);
        //extract username by the input token
        username = jwtService.extractUsername(jwt);
        //if username is existed but it is not authenticated yet (this will make for each request)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load the user details by username since it's existed
            User user = (User) userDetailsService.loadUserByUsername(username);
            //check if the token is valid to use to be authenticated
            if (jwtService.isTokenValid(jwt, user)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        //whatever if the user is authenticated or not it will pass to next filter and based on that show response or error
        filterChain.doFilter(request, response);
    }
}
