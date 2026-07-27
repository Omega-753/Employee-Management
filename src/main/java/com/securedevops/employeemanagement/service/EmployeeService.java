package com.securedevops.employeemanagement.service;

import com.securedevops.employeemanagement.model.DepartmentSummary;
import com.securedevops.employeemanagement.model.Employee;
import com.securedevops.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public long getDepartmentCount() {
        return employeeRepository.findAll()
                .stream()
                .map(Employee::getDepartment)
                .filter(department -> department != null && !department.trim().isEmpty())
                .distinct()
                .count();
    }

    public List<DepartmentSummary> getDepartmentSummary() {

        Map<String, Long> departmentMap = employeeRepository.findAll()
                .stream()
                .filter(employee ->
                        employee.getDepartment() != null &&
                        !employee.getDepartment().trim().isEmpty())
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()));

        return departmentMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new DepartmentSummary(
                        entry.getKey(),
                        entry.getValue()))
                .toList();
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartmentIgnoreCase(department);
    }

    public void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }

    public Employee getEmployeeById(Integer employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }
}