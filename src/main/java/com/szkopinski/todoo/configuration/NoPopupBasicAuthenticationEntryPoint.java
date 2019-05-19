package com.szkopinski.todoo.configuration;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;

public class NoPopupBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      org.springframework.security.core.AuthenticationException e) throws IOException, ServletException {

    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
  }
}
