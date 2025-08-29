package com.example.bfh_sql_solver.mock;

import com.example.bfh_sql_solver.dto.FinalQueryPayload;
import com.example.bfh_sql_solver.dto.GenerateWebhookRequest;
import com.example.bfh_sql_solver.dto.GenerateWebhookResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hiring")
public class LocalMockController {

  @PostMapping("/generateWebhook/JAVA")
  public GenerateWebhookResponse generate(@RequestBody GenerateWebhookRequest req) {
    GenerateWebhookResponse r = new GenerateWebhookResponse();
    r.setWebhook("http://localhost:8080/hiring/receiveFinalQuery");
    r.setAccessToken("mocked-jwt-token-123");
    return r;
  }

  @PostMapping("/receiveFinalQuery")
  public ResponseEntity<String> receiveFinalQuery(@RequestBody FinalQueryPayload payload,
                                                  @RequestHeader(value = "Authorization", required = false) String auth) {
    System.out.println("=== Local mock received submission ===");
    System.out.println("Authorization: " + auth);
    System.out.println("FinalQuery: " + (payload != null ? payload.getFinalQuery() : null));
    return ResponseEntity.ok("ok");
  }
}
