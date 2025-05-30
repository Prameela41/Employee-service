package com.tekikons.service;

import com.tekikons.entity.EmployeeEntity;
import com.tekikons.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    private EmployeeRepository repository;

    @Override
    public List<EmployeeEntity> getAllEmployees() {
         List<EmployeeEntity> employees=(List<EmployeeEntity>) repository.findAll();
        return employees;
    }

    @Override
    public EmployeeEntity getEmployeeById(Integer Id) {
       Optional<EmployeeEntity> employeeOptional= repository.findById(Id);
        return employeeOptional.get();
    }

    @Override
    public EmployeeEntity saveEmployee(EmployeeEntity employee) {
        EmployeeEntity entity = repository.save(employee);
        return entity;

    }
}
