package com.tinubu.insurance.infrastructure.api.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

  @Value("${app.openapi.dev-url:http://localhost:8081/insurance-policies}")
  private String devUrl;

  @Value("${app.openapi.prod-url:https://api.example.tinubu.com/insurance-policies}")
  private String prodUrl;

  @Bean
  public OpenAPI customOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Development server");

    Server prodServer = new Server();
    prodServer.setUrl(prodUrl);
    prodServer.setDescription("Production server");

    Contact contact =
        new Contact()
            .name("Samir TABIB")
            .email("tbib.samir@gmail.com")
            .url("https://tinyurl.com/2s4x3zdw");

    License license = new License().name("MIT License").url("https://opensource.org/licenses/MIT");

    Info info =
        new Info()
            .title("Insurance Policies API")
            .version("1.0")
            .description("API documentation for the Insurance Policies application")
            .contact(contact)
            .license(license);

    return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
  }
}
