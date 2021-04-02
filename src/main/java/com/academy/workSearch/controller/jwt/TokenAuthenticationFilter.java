package com.academy.workSearch.controller.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public TokenAuthenticationFilter() {
        super("/api/v1/");
        setAuthenticationSuccessHandler((request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getRequestDispatcher(request.getServletPath() + request.getPathInfo()).forward(request, response);
        });
        setAuthenticationFailureHandler((request, response, authenticationException) -> response.getOutputStream().print(authenticationException.getMessage()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (token == null) {
            TokenAuthentication authentication = new TokenAuthentication(null);
            authentication.setAuthenticated(false);
            return authentication;
        }
        TokenAuthentication tokenAuthentication = new TokenAuthentication(token);
        return getAuthenticationManager().authenticate(tokenAuthentication);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }
}
