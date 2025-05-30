package com.tekikons.service;

import com.tekikons.entity.EmployeeEntity;

import java.util.List;

public interface EmployeeService {

    List<EmployeeEntity> getAllEmployees();

    EmployeeEntity getEmployeeById(Integer Id);

    EmployeeEntity saveEmployee(EmployeeEntity employee);

}
