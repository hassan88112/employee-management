package com.stc.EmployeeManagement.service;

import com.stc.EmployeeManagement.models.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    Employee createEmployee(Employee employee);

    Employee getEmployeeById(UUID id);

    List<Employee> getAllEmployees();

    Employee updateEmployee(UUID id, Employee employee);

    void deleteEmployee(UUID id);
}
