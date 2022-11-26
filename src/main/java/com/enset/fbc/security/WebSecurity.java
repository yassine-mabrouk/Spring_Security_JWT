package com.enset.fbc.security;

import com.enset.fbc.service.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFilter;

@EnableWebSecurity
public class WebSecurity  extends WebSecurityConfigurerAdapter {
    private  final UserService userDetailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserService userDetailService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailService = userDetailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()//// for communication betweenn other app
                .csrf().disable()  // desable csrf because we work with stateless
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
                .permitAll()
                .antMatchers(HttpMethod.GET , "/refreshToken/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/users/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and().addFilter(this.getAuthenticationFilter())
                //  .addFilter(new AuthenticationFilter(authenticationManager()));
                .addFilter(new AuthorizationFilter(authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    // method to change login url which is /login by default
    protected AuthentifiactionFilrer getAuthenticationFilter() throws Exception {
        final AuthentifiactionFilrer filter = new AuthentifiactionFilrer(authenticationManager());
        filter.setFilterProcessesUrl("/users/login");
        return filter;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
          auth.userDetailsService(userDetailService).passwordEncoder(bCryptPasswordEncoder);
    }
}
