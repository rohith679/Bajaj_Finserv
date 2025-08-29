package com.example.bfh_sql_solver.runner;

import com.example.bfh_sql_solver.dto.GenerateWebhookRequest;
import com.example.bfh_sql_solver.dto.GenerateWebhookResponse;
import com.example.bfh_sql_solver.service.BfhClient;
import com.example.bfh_sql_solver.service.SqlSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class StartupRunner implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

  private final BfhClient client;
  private final SqlSolver solver;

  @Value("${app.candidate.name}")
  private String name;

  @Value("${app.candidate.regNo}")
  private String regNo;

  @Value("${app.candidate.email}")
  private String email;

  public StartupRunner(BfhClient client, SqlSolver solver) {
    this.client = client;
    this.solver = solver;
  }

  @Override
  public void run(String... args) throws Exception {
    logger.info("=== BFH SQL Solver Starting ===");
    logger.info("Candidate: {} | RegNo: {} | Email: {}", name, regNo, email);

    // 1) Generate webhook
    GenerateWebhookResponse resp = client.generateWebhook(new GenerateWebhookRequest(name, regNo, email));
    String webhook = resp.getWebhook();
    String accessToken = resp.getAccessToken();

    logger.info("Received webhook: {}", webhook);
    logger.info("Received accessToken: {}", accessToken != null ? accessToken.substring(0, Math.min(12, accessToken.length())) + "..." : "null");

    // 2) Solve SQL
    String finalSql = solver.pickSqlByRegNo(regNo);
    logger.info("Prepared final SQL ({} chars)", finalSql.length());

    // 3) Save SQL to file
    Path outDir = Path.of("results");
    Files.createDirectories(outDir);
    Path outFile = outDir.resolve("final-query.sql");
    Files.writeString(outFile, finalSql + System.lineSeparator(), StandardCharsets.UTF_8);
    logger.info("Saved final SQL to {}", outFile.toAbsolutePath());

    // 4) Submit to webhook
    client.submitFinalQuery(webhook, accessToken, finalSql);

    logger.info("=== Done ===");
  }
}
