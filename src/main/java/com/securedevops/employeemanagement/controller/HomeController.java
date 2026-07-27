package com.securedevops.employeemanagement.controller;

import com.securedevops.employeemanagement.model.Employee;
import com.securedevops.employeemanagement.service.EmployeeService;
import com.securedevops.employeemanagement.service.FileStorageService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("employeeCount",
                employeeService.getAllEmployees().size());

        model.addAttribute("departmentCount",
                employeeService.getDepartmentCount());

        return "index";
    }

    @GetMapping("/employees")
    public String employees(Model model) {

        model.addAttribute("employees",
                employeeService.getAllEmployees());

        model.addAttribute("departmentCount",
                employeeService.getDepartmentCount());

        return "employees";
    }

    @GetMapping("/add-employee")
    public String addEmployee(Model model) {

        model.addAttribute("employee", new Employee());

        return "add-employee";
    }

    @GetMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable Integer id) {

        employeeService.deleteEmployee(id);

        return "redirect:/employees";
    }

    @GetMapping("/edit-employee/{employeeId}")
    public String editEmployee(@PathVariable Integer employeeId,
                               Model model) {

        model.addAttribute("employee",
                employeeService.getEmployeeById(employeeId));

        return "add-employee";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/explore-ems")
    public String exploreEMS() {

        return "explore-ems";
    }

    @GetMapping("/departments")
    public String departments(Model model) {

        model.addAttribute("departments",
                employeeService.getDepartmentSummary());

        return "departments";
    }

    @GetMapping("/departments/{department}")
    public String employeesByDepartment(@PathVariable String department,
                                        Model model) {

        model.addAttribute("employees",
                employeeService.getEmployeesByDepartment(department));

        model.addAttribute("departmentName", department);

        model.addAttribute("departmentCount",
                employeeService.getDepartmentCount());

        return "employees";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute Employee employee,
                               @RequestParam("resumeFile") MultipartFile resumeFile)
            throws IOException {

        if (!resumeFile.isEmpty()) {

            String fileName = fileStorageService.saveFile(resumeFile);

            employee.setResumeFileName(fileName);
        }

        employeeService.saveEmployee(employee);

        return "redirect:/employees";
    }
}