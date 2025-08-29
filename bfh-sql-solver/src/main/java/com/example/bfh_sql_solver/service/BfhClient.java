package com.example.bfh_sql_solver.service;

import com.example.bfh_sql_solver.dto.FinalQueryPayload;
import com.example.bfh_sql_solver.dto.GenerateWebhookRequest;
import com.example.bfh_sql_solver.dto.GenerateWebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BfhClient {

  private static final Logger logger = LoggerFactory.getLogger(BfhClient.class);
  private final RestTemplate restTemplate;

  @Value("${app.api.base}")
  private String baseUrl;

  @Value("${app.api.generate}")
  private String generatePath;

  public BfhClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public GenerateWebhookResponse generateWebhook(GenerateWebhookRequest body) {
    String url = baseUrl + generatePath;
    logger.info("Calling generateWebhook at: {}", url);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<GenerateWebhookRequest> req = new HttpEntity<>(body, headers);

    ResponseEntity<GenerateWebhookResponse> resp =
        restTemplate.exchange(url, HttpMethod.POST, req, GenerateWebhookResponse.class);

    if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
      logger.info("generateWebhook succeeded");
      return resp.getBody();
    }
    throw new IllegalStateException("Failed to generate webhook: " + resp.getStatusCode());
  }

  public void submitFinalQuery(String webhookUrl, String jwtAccessToken, String finalSql) {
    logger.info("Submitting final SQL to webhook: {}", webhookUrl);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", jwtAccessToken);

    FinalQueryPayload payload = new FinalQueryPayload(finalSql);
    HttpEntity<FinalQueryPayload> entity = new HttpEntity<>(payload, headers);

    ResponseEntity<String> resp =
        restTemplate.exchange(webhookUrl, HttpMethod.POST, entity, String.class);

    if (resp.getStatusCode().is2xxSuccessful()) {
      logger.info("Final SQL submitted successfully.");
    } else {
      logger.error("Submission failed: {} - {}", resp.getStatusCode(), resp.getBody());
      throw new IllegalStateException("Submission failed: " + resp.getStatusCode());
    }
  }
}
