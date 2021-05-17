package dev.tho.easyfile.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tho.easyfile.model.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static dev.tho.easyfile.config.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {

        UUID username = UUID.randomUUID();
        String password = "";
        try {
            Bucket bucket = new ObjectMapper().readValue(req.getInputStream(), Bucket.class);
            username = bucket.getId();
            password = bucket.getPassword();
        } catch (IOException e) {
            logger.error("User supplied credentials have invalid format");
        }

        return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>())
            );

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader("Access-Control-Allow-Headers",HEADER_STRING);
        res.addHeader("Access-Control-Expose-Headers", HEADER_STRING);
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
