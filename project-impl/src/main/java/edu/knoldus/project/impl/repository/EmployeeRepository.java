package edu.knoldus.project.impl.repository;

import edu.knoldus.project.api.Employee;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface EmployeeRepository {
    
     CompletableFuture<Employee> getEmployeeById(String employeeId);
     
     CompletableFuture<Employee> getEmployeeByName(String name);
     
     CompletableFuture<String> deleteEmployeeById(String employeeId);
     
     CompletableFuture<String> insertEmployee(Employee employee);
     
     
}
