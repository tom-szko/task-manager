package com.szkopinski.todoo.configuration;

import com.szkopinski.todoo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class GlobalAuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void init(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService());
  }

  @Bean
  UserDetailsService userDetailsService() {
    return userName -> {
      com.szkopinski.todoo.model.User user = userRepository.findByUserName(userName);
      if (user != null) {
        return new User(user.getUserName(), user.getPassword(), true, true, true, true, AuthorityUtils.createAuthorityList(
            "USER"));
      } else {
        throw new UsernameNotFoundException("Could not find the user '" + userName + "'");
      }
    };
  }
}
