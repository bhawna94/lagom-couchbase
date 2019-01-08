package edu.knoldus.project.impl.repository;

import edu.knoldus.project.api.Employee;
import edu.knoldus.project.api.EmployeeResponse;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface EmployeeRepository {
    
     CompletableFuture<Employee> getEmployeeById(String employeeId);
     
     CompletableFuture<Employee> getEmployeeByName(String name, String token);
     
     CompletableFuture<String> deleteEmployeeById(String employeeId);
     
     CompletableFuture<EmployeeResponse> insertEmployee(Employee employee);
     
     
}
