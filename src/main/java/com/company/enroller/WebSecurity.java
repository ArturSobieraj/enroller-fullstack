package com.company.enroller;

import com.company.enroller.security.JWTAuthenticationFilter;
import com.company.enroller.security.JWTAuthorizationFilter;
import com.company.enroller.security.ParticipantProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final ParticipantProvider participantProvider;

    @Value("${security.secret}")
    private String secret;

    @Value("${security.issuer}")
    private String issuer;

    @Value("${security.token_expiration_in_seconds}")
    private int tokenExpiration;

    @Autowired
    public WebSecurity(ParticipantProvider participantProvider,
                       @Qualifier("BCryptEncoder") PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.participantProvider = participantProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/participants").permitAll()
                .antMatchers("/api/tokens").permitAll()
                .antMatchers("/api/**").authenticated()
                .and()
                .addFilterBefore(new JWTAuthenticationFilter(authenticationManager(), secret, issuer, tokenExpiration),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), secret))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(participantProvider).passwordEncoder(passwordEncoder);

   }
}