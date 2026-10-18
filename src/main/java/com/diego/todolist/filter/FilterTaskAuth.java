package com.diego.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.diego.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class FilterTaskAuth extends OncePerRequestFilter {

    private final IUserRepository userRepository;

    public FilterTaskAuth(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private void checkAuth(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            response.sendError(401);

        } else {

            String base64Credentials = authorizationHeader.substring("Basic".length()).trim();

            byte[] decodedCredentials = Base64.getDecoder().decode(base64Credentials);

            String[] credentialParts = new String(decodedCredentials).split(":");

            String authEmail = credentialParts[0];
            String authPassword = credentialParts[1];


            // Validates the user's email
            var user = userRepository.findByEmail(authEmail);
            if (user == null) {
                response.sendError(401);

            // Password decode and validation
            } else {
                var passwdVerify = BCrypt.verifyer()
                .verify(authPassword.toCharArray(), user.getPassword());
                
                if (passwdVerify.verified) {
                    request.setAttribute("userId", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks/")) {
            this.checkAuth(request, response, filterChain);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
