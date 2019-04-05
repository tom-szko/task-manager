package com.szkopinski.todoo.configuration;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  @Bean
  public Docket taskApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("tasks-api")
        .useDefaultResponseMessages(false)
        .apiInfo(apiInfo())
        .select()
        .paths(regex("/api.*"))
        .build();
  }

  @Bean
  public UiConfiguration uiConfig() {
    return new UiConfiguration(null);
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Todoo task manager API")
        .description("API for Todoo. A simple task manager app")
        .version("1.0")
        .build();
  }
}
