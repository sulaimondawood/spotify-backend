package com.dawood.spotify.configs;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dawood.spotify.services.CustomUserDetailsService;
import com.dawood.spotify.utils.ErrorResponseWriter;
import com.dawood.spotify.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;

  private final CustomUserDetailsService customUserDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader != null && authHeader.startsWith("Bearer ")) {

      try {

        String jwt = authHeader.substring(7);

        String email = jwtUtils.getUsernameFromToken(jwt);

        UserDetails user = customUserDetailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null,
            user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      } catch (TokenExpiredException e) {
        log.error(e.getMessage());
        ErrorResponseWriter.write(response, "Authorization token expired", HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }

      catch (JWTVerificationException e) {
        ErrorResponseWriter.write(response, "Invalid Authorization token", HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }

      catch (Exception e) {
        log.error("Something went wrong", e);
        ErrorResponseWriter.write(response, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
        return;
      }

    }

    filterChain.doFilter(request, response);
  }

}
