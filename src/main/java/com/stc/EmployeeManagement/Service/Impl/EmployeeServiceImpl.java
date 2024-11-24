package com.stc.EmployeeManagement.Service.Impl;

import com.stc.EmployeeManagement.Service.EmailNotificationService;
import com.stc.EmployeeManagement.Service.EmployeeService;
import com.stc.EmployeeManagement.exception.EmployeeNotFoundException;
import com.stc.EmployeeManagement.models.Employee;
import com.stc.EmployeeManagement.models.Mail;
import com.stc.EmployeeManagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmailNotificationService emailNotificationService;

    @Override
    public Employee createEmployee(Employee employee) {
        log.info("Creating new employee: {}", employee);
        Employee savedEmployee = repository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());

        try {
            String[] to = {"NTGAccountManagement@ntgclarity.com", "sfathi@ntgclarity.com"};
            String[] cc = {"hassanshalash0@gmail.com"};

            sendMailNotification(
                    prepareEmployeeDetailsEmail(employee),
                    "Employee Management Service",
                    to,
                    cc,
                    "hassanshalash0@gmail.com"
            );
            log.info("Notification email sent successfully for employee ID: {}", savedEmployee.getId());
        } catch (Exception e) {
            log.error("Failed to send notification email for employee ID: {}", savedEmployee.getId(), e);
        }

        return savedEmployee;
    }

    @Override
    public Employee getEmployeeById(UUID id) {
        log.info("Fetching employee by ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee with ID: {} not found", id);
                    return new EmployeeNotFoundException("Employee with ID " + id + " not found");
                });
    }

    @Override
    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        return repository.findAll();
    }

    @Override
    public Employee updateEmployee(UUID id, Employee employee) {
        log.info("Updating employee with ID: {}", id);
        Employee existingEmployee = getEmployeeById(id);

        log.debug("Updating employee details: {}", employee);
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setSalary(employee.getSalary());

        Employee updatedEmployee = repository.save(existingEmployee);
        log.info("Employee updated successfully with ID: {}", updatedEmployee.getId());

        return updatedEmployee;
    }

    @Override
    public void deleteEmployee(UUID id) {
        log.info("Deleting employee with ID: {}", id);
        Employee employee = getEmployeeById(id);
        repository.delete(employee);
        log.info("Employee deleted successfully with ID: {}", id);
    }

    private void sendMailNotification(String message, String subject, String[] to, String[] cc, String from) {
        try {
            Mail mail = new Mail();
            mail.setMailFrom("hassanshalash0@gmail.com");
            mail.setMailTo(to);
            mail.setMailCc(cc);
            mail.setMailSubject(subject);
            mail.setMailContent(message);
            mail.setContentType("text/html");

            emailNotificationService.sendNotification(mail, from);
        } catch (Exception e) {
            log.error("Error while sending email notification", e);
        }
    }

    private String prepareEmployeeDetailsEmail(Employee employee) {
        log.debug("Preparing email content for employee: {}", employee);

        String emailContent = "<html>" +
                "<body>" +
                "<br>" +
                "<b><br>This Mail contains Employee details.</b><br><br>" +
                "<table width='100%' border='1' align='center'>" +
                "  <tr align='center'><th>Field</th><th>Value</th></tr>" +
                "  <tr><td>First Name</td><td>" + employee.getFirstName() + "</td></tr>" +
                "  <tr><td>Last Name</td><td>" + employee.getLastName() + "</td></tr>" +
                "  <tr><td>Email</td><td>" + employee.getEmail() + "</td></tr>" +
                "  <tr><td>Department</td><td>" + employee.getDepartment() + "</td></tr>" +
                "  <tr><td>Salary</td><td>" + employee.getSalary() + "</td></tr>" +
                "</table>" +
                "<br>" +
                "<b>" +
                "Thanks,<br>" +
                "Hassan Shalash<br>" +
                "</b>" +
                "</body>" +
                "</html>";

        return emailContent;
    }

}
