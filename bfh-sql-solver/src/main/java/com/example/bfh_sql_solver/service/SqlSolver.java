package com.example.bfh_sql_solver.service;

import org.springframework.stereotype.Component;

@Component
public class SqlSolver {

  // SQL for Question 2
  private static final String Q2_SQL = """
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

  public String solveQuestion2() {
    return Q2_SQL.trim();
  }

  public String pickSqlByRegNo(String regNo) {
    if (regNo == null) throw new IllegalArgumentException("regNo required");
    String digits = regNo.replaceAll("\\D", "");
    if (digits.length() >= 2) {
      int last2 = Integer.parseInt(digits.substring(digits.length() - 2));
      if (last2 % 2 == 0) {
        return solveQuestion2();
      } else {
        throw new IllegalStateException("RegNo indicates Question 1 (odd). Only Q2 is implemented.");
      }
    }
    throw new IllegalArgumentException("regNo must contain at least two digits.");
  }
}
