package com.stc.EmployeeManagement.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stc.EmployeeManagement.Service.EmployeeService;
import com.stc.EmployeeManagement.controller.EmployeeController;
import com.stc.EmployeeManagement.dto.EmployeeDto;
import com.stc.EmployeeManagement.exception.EmployeeNotFoundException;
import com.stc.EmployeeManagement.models.Employee;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createEmployee_shouldReturnCreatedEmployee() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Hassan");
        employeeDto.setLastName("Shalash");
        employeeDto.setEmail("hassanshalash0@gmail.com");
        employeeDto.setDepartment("IT");
        employeeDto.setSalary(8000.0);

        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        employee.setDepartment(employeeDto.getDepartment());
        employee.setSalary(employeeDto.getSalary());

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("hassan"))
                .andExpect(jsonPath("$.lastName").value("shalash"))
                .andExpect(jsonPath("$.email").value("hassanshalash0@gmail.com"))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(jsonPath("$.salary").value(8000.0));

        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }

    @Test
    void getEmployeeById_shouldReturnEmployee() throws Exception {
        UUID id = UUID.randomUUID();

        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName("hassan");
        employee.setLastName("shalash");
        employee.setEmail("hassanshalash0@gmail.com");
        employee.setDepartment("IT");
        employee.setSalary(8000.0);

        when(employeeService.getEmployeeById(id)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("hassan"));

        verify(employeeService, times(1)).getEmployeeById(id);
    }

    @Test
    void deleteEmployee_shouldReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(employeeService).deleteEmployee(id);

        mockMvc.perform(delete("/api/employees/{id}", id))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(id);
    }

    @Test
    void getEmployeeById_notFound_shouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(employeeService.getEmployeeById(id)).thenThrow(new EmployeeNotFoundException("Employee with ID " + id + " not found"));

        mockMvc.perform(get("/api/employees/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee with ID " + id + " not found"));

        verify(employeeService, times(1)).getEmployeeById(id);
    }
}
