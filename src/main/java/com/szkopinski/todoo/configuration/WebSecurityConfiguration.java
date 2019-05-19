package com.szkopinski.todoo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic()
        .authenticationEntryPoint(new NoPopupBasicAuthenticationEntryPoint())
        .and()
        .authorizeRequests()
        .antMatchers("/*.js", "/*.css", "/images/*.png").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login.html")
        .permitAll()
        .and()
        .logout()
        .permitAll()
        .and()
        .csrf().disable();
  }
}
