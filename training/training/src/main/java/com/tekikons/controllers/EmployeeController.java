package com.tekikons.controllers;

import com.tekikons.entity.EmployeeEntity;
import com.tekikons.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/all")
    public List<EmployeeEntity> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{Id}")
    public EmployeeEntity getEmployeeById(@PathVariable Integer Id){
        return employeeService.getEmployeeById(Id);
    }

    @PostMapping("/save")
    public EmployeeEntity saveEmployee(@RequestBody EmployeeEntity employee){
        return employeeService.saveEmployee(employee);

    }
}
