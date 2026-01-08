package com.tekikons.service;

import com.tekikons.entity.EmployeeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class EmployeeServiceRegressionTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void testGetAllEmployeesNotNull() {
        List<EmployeeEntity> employees = employeeService.getAllEmployees();
        Assertions.assertNotNull(employees);
    }

    @Test
    public void testSaveAndFetchEmployee() {
        EmployeeEntity emp = new EmployeeEntity();
        emp.setName("Regression Test");
        EmployeeEntity saved = employeeService.saveEmployee(emp);
        Assertions.assertNotNull(saved.getId());
        EmployeeEntity fetched = employeeService.getEmployeeById(saved.getId());
        Assertions.assertEquals("Regression Test", fetched.getName());
    }
}