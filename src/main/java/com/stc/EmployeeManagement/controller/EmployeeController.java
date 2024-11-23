package com.stc.EmployeeManagement.controller;

import com.stc.EmployeeManagement.Service.EmployeeService;
import com.stc.EmployeeManagement.dto.EmployeeDto;
import com.stc.EmployeeManagement.models.Employee;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;


    @PostMapping
    public Employee createEmployee(@Valid @RequestBody EmployeeDto dto) {
        logger.info("Received request to create employee with email: {}", dto.getEmail());
        Employee employee = modelMapper.map(dto, Employee.class);
        Employee createdEmployee = employeeService.createEmployee(employee);
        logger.info("Successfully created employee with ID: {}", createdEmployee.getId());
        return createdEmployee;
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable UUID id) {
        logger.info("Received request to get employee with ID: {}", id);
        Employee employee = employeeService.getEmployeeById(id);
        logger.info("Successfully retrieved employee with ID: {}", id);
        return employee;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        logger.info("Received request to get all employees");
        List<Employee> employees = employeeService.getAllEmployees();
        logger.info("Successfully retrieved {} employees", employees.size());
        return employees;
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable UUID id, @Valid @RequestBody EmployeeDto dto) {
        logger.info("Received request to update employee with ID: {}", id);
        Employee employee = modelMapper.map(dto, Employee.class);
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        logger.info("Successfully updated employee with ID: {}", id);
        return updatedEmployee;
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable UUID id) {
        logger.info("Received request to delete employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        logger.info("Successfully deleted employee with ID: {}", id);
    }
}
