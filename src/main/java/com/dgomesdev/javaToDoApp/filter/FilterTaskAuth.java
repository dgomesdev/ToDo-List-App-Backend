package com.dgomesdev.javaToDoApp.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.dgomesdev.javaToDoApp.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();
        if (servletPath.startsWith("/tasks/")) {
            var authorization = request.getHeader("Authorization");

            var encodedAuth = authorization.substring("Basic".length()).trim();
            byte[] decodedAuth = Base64.getDecoder().decode(encodedAuth);
            String authString = new String(decodedAuth);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            System.out.println("Authorized!");

            var user = userRepository.findByUsername(username);
            if (user == null) response.sendError(401, "User not authorized");
            else {
                var verifiedPassword = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (verifiedPassword.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else response.sendError(401, "User's password not authorized");
            }
        } else filterChain.doFilter(request, response);
    }
}