package com.example.bfh_sql_solver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

  @Value("${app.http.connectTimeoutMs:10000}")
  private int connectTimeout;

  @Value("${app.http.readTimeoutMs:20000}")
  private int readTimeout;

  @Bean
  public RestTemplate restTemplate() {
    SimpleClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
    f.setConnectTimeout(connectTimeout);
    f.setReadTimeout(readTimeout);
    return new RestTemplate(f);
  }
}
