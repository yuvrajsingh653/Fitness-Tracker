package com.project.fitness.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtUtils.getJwtFromHeader(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                Claims claims = jwtUtils.getAllClaims(jwt);
                String userId = claims.getSubject();
                List<?> roles = claims.get("roles", List.class);

                List<SimpleGrantedAuthority> authorities = roles == null
                        ? List.of()
                        : roles.stream()
                                .map(JwtAuthenticationFilter::toAuthority)
                                .toList();

                var authentication = new UsernamePasswordAuthenticationToken(
                        userId, null, authorities);
                authentication.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (RuntimeException ignored) {
            // Invalid or malformed tokens are treated as unauthenticated requests.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private static SimpleGrantedAuthority toAuthority(Object role) {
        String name;
        if (role instanceof String value) {
            name = value;
        } else if (role instanceof java.util.Map<?, ?> value && value.get("authority") != null) {
            name = value.get("authority").toString();
        } else {
            name = role.toString();
        }

        return new SimpleGrantedAuthority(name.startsWith("ROLE_") ? name : "ROLE_" + name);
    }
}
