package com.szkopinski.todoo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;

@Configuration
public class GlobalAuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private com.szkopinski.todoo.service.AccountService accountService;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService.getUser());
    }
}
