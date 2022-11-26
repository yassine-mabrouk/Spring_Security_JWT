package com.enset.fbc.security;

import com.enset.fbc.entities.RoleEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        if (req.getServletPath().equals("refreshToken")) {
            chain.doFilter(req, res);
        }  else {
            String header = req.getHeader(SecurityConstants.HEADER_STRING);
            if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                chain.doFilter(req, res);
                return;
            }
            UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
            // autenticat user
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // pass au suivant
            chain.doFilter(req, res);
        }
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        if (token != null) {

            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");


       Claims claims = Jwts.parser()
                    .setSigningKey( SecurityConstants.TOKEN_SECRET )
                    .parseClaimsJws( token )
                    .getBody();
            ArrayList<String> roles = (ArrayList<String>) claims.get("roles");
            Collection<GrantedAuthority> grantedAuthority = new ArrayList<>();
            if (roles !=null ) {
                for (String str : roles) {
                    grantedAuthority.add(new SimpleGrantedAuthority(str));
                }
            }

            if (claims.getSubject() != null) {
                return new UsernamePasswordAuthenticationToken(claims.getSubject(), null,grantedAuthority );
            }
            return null;
        }
        return null;
    }

}
