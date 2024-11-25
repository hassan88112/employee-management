package com.stc.EmployeeManagement.ServiceTest;

import com.stc.EmployeeManagement.service.Impl.EmployeeServiceImpl;
import com.stc.EmployeeManagement.models.Employee;
import com.stc.EmployeeManagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeServiceImpl service;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setFirstName("Hassan");
        employee.setLastName("Shalash");
        employee.setEmail("hassanshalash0@gmail.com");
        employee.setDepartment("IT");
        employee.setSalary(8000.0);
    }

    @Test
    void createEmployee_shouldSaveEmployee() {
        when(repository.save(any(Employee.class))).thenReturn(employee);

        Employee savedEmployee = service.createEmployee(employee);

        assertNotNull(savedEmployee);
        assertEquals("hassan", savedEmployee.getFirstName());
        verify(repository, times(1)).save(employee);
    }

    @Test
    void getEmployeeById_shouldReturnEmployee() {
        when(repository.findById(employee.getId())).thenReturn(Optional.of(employee));

        Employee foundEmployee = service.getEmployeeById(employee.getId());

        assertNotNull(foundEmployee);
        assertEquals(employee.getId(), foundEmployee.getId());
        verify(repository, times(1)).findById(employee.getId());
    }

    @Test
    void getEmployeeById_shouldThrowExceptionWhenNotFound() {
        UUID employeeId = UUID.randomUUID();
        when(repository.findById(employeeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> service.getEmployeeById(employeeId));

        assertEquals("Employee with ID " + employeeId + " not found", exception.getMessage());
    }

    @Test
    void deleteEmployee_shouldDeleteEmployee() {
        UUID employeeId = employee.getId();
        when(repository.findById(employeeId)).thenReturn(Optional.of(employee));
        doNothing().when(repository).delete(employee);

        service.deleteEmployee(employeeId);

        verify(repository, times(1)).delete(employee);
    }
}
