package com.example.bfh_sql_solver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SolverController {

    // ✅ Root endpoint
    @GetMapping("/")
    public String home() {
        return "✅ BFH SQL Solver is running!";
    }

    // ✅ Endpoint to fetch the final SQL query
    @GetMapping("/final-sql")
    public String getFinalSql() {
        return """
            SELECT
              e.EMP_ID,
              e.FIRST_NAME,
              e.LAST_NAME,
              d.DEPARTMENT_NAME,
              SUM(CASE WHEN e2.DOB > e.DOB THEN 1 ELSE 0 END) AS YOUNGER_EMPLOYEES_COUNT
            FROM EMPLOYEE e
            JOIN DEPARTMENT d
              ON d.DEPARTMENT_ID = e.DEPARTMENT
            LEFT JOIN EMPLOYEE e2
              ON e2.DEPARTMENT = e.DEPARTMENT
             AND e2.EMP_ID <> e.EMP_ID
            GROUP BY
              e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME
            ORDER BY e.EMP_ID DESC;
            """;
    }
}
