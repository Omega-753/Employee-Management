package com.securedevops.employeemanagement.controller;

import com.securedevops.employeemanagement.model.Employee;
import com.securedevops.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
public class ReportController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/reports")
    public String reports(Model model) {

        List<Employee> employees = employeeRepository.findAll();

        int totalEmployees = employees.size();

        long employeesWithResume = employees.stream()
                .filter(employee ->
                        employee.getResumeFileName() != null &&
                                !employee.getResumeFileName().isBlank())
                .count();

        Map<String, Long> departmentStatistics =
                employees.stream()
                        .collect(Collectors.groupingBy(
                                Employee::getDepartment,
                                TreeMap::new,
                                Collectors.counting()
                        ));

        int departmentCount = departmentStatistics.size();

        double averageEmployeesPerDepartment =
                departmentCount == 0 ? 0 :
                        (double) totalEmployees / departmentCount;

        long maxEmployees = departmentStatistics.values()
                .stream()
                .max(Long::compare)
                .orElse(1L);

        List<DepartmentReport> reportList = new ArrayList<>();

        departmentStatistics.forEach((department, count) -> {

            int percentage = (int) ((count * 100.0) / maxEmployees);

            reportList.add(
                    new DepartmentReport(
                            department,
                            count,
                            percentage
                    )
            );

        });

        reportList.sort(
                Comparator.comparing(DepartmentReport::getEmployeeCount)
                        .reversed()
        );

        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("departmentCount", departmentCount);
        model.addAttribute("employeesWithResume", employeesWithResume);
        model.addAttribute("averageEmployeesPerDepartment",
                String.format("%.2f", averageEmployeesPerDepartment));
        model.addAttribute("departmentStatistics", reportList);

        return "reports";
    }

    @GetMapping("/reports/export")
    public ResponseEntity<byte[]> exportCsv() {

        List<Employee> employees = employeeRepository.findAll();

        StringBuilder csv = new StringBuilder();

        csv.append("Employee ID,First Name,Last Name,Department,Email,Resume\n");

        for (Employee employee : employees) {

            csv.append(employee.getEmployeeId()).append(",");
            csv.append(employee.getFirstName()).append(",");
            csv.append(employee.getLastName()).append(",");
            csv.append(employee.getDepartment()).append(",");
            csv.append(employee.getEmail()).append(",");

            if (employee.getResumeFileName() != null &&
                    !employee.getResumeFileName().isBlank()) {

                csv.append("Yes");

            } else {

                csv.append("No");

            }

            csv.append("\n");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=employee-report.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static class DepartmentReport {

        private final String departmentName;
        private final Long employeeCount;
        private final int percentage;

        public DepartmentReport(String departmentName,
                                Long employeeCount,
                                int percentage) {

            this.departmentName = departmentName;
            this.employeeCount = employeeCount;
            this.percentage = percentage;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public Long getEmployeeCount() {
            return employeeCount;
        }

        public int getPercentage() {
            return percentage;
        }
    }
}