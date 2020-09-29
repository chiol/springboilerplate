package kr.ibct.springboilerplate.config;

import kr.ibct.springboilerplate.account.AccountSecurity;
import kr.ibct.springboilerplate.account.AccountService;
import kr.ibct.springboilerplate.jwt.JwtAuthenticationEntryPoint;
import kr.ibct.springboilerplate.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
//        securedEnabled = true,
//        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    AccountService accountService;
    @Autowired
    JwtAuthenticationEntryPoint entryPoint;

    @Bean
    public AccountSecurity accountSecurity() {
        return new AccountSecurity();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .cors().disable()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(entryPoint)
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/users/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/users/**").permitAll()
                .antMatchers(HttpMethod.PATCH, "/api/v1/users/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/v1/users/{userId}").access("hasRole('ADMIN') or @accountSecurity.check(authentication, #userId)")
                .antMatchers("/auth").authenticated()
                .antMatchers("/auth/admin").hasRole("ADMIN")
                .anyRequest().permitAll();
        http.headers().frameOptions().sameOrigin(); // h2-console 때문에
    }
}
