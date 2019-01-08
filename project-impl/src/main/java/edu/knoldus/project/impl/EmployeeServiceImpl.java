package edu.knoldus.project.impl;

import akka.NotUsed;
import com.couchbase.client.core.CouchbaseException;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import edu.knoldus.project.api.Employee;
import edu.knoldus.project.api.EmployeeResponse;
import edu.knoldus.project.api.EmployeeService;
import edu.knoldus.project.impl.repository.EmployeeRepository;

import javax.inject.Inject;
import java.util.NoSuchElementException;


public class EmployeeServiceImpl implements EmployeeService {
    
    private EmployeeRepository employeeRepository;
    @Inject
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        
        this.employeeRepository = employeeRepository;
        
    }
    
    
    @Override
    public ServiceCall<Employee, EmployeeResponse> insert() {
        return request -> employeeRepository.insertEmployee(request)
                .exceptionally(throwable -> {
                    Throwable cause = throwable.getCause();
                    if (cause instanceof CouchbaseException)
                        throw new CouchbaseException(cause.getMessage());
                    throw new RuntimeException(cause);
                });
    }
    

    @Override
    public ServiceCall<NotUsed, Employee> getEmployee(String documentId) {
        return request ->
                employeeRepository.getEmployeeById(documentId)
                        .exceptionally(throwable -> {
                            Throwable cause = throwable.getCause();
                            if (cause instanceof NoSuchElementException)
                                throw new NotFound("Such document with " +documentId + "does not exist");
                            throw new RuntimeException(cause);
                        });
    }
    
    @Override
    public ServiceCall<NotUsed, Employee> getEmployeeByName(String name, String token) {
        return request ->
                employeeRepository.getEmployeeByName(name, token)
                        .exceptionally(throwable -> {
                            Throwable cause = throwable.getCause();
                            if (cause instanceof NoSuchElementException)
                                throw new NotFound("Such document with " + name + "does not exist");
                            throw new RuntimeException(cause);
                        });
    }
    
    @Override
    public ServiceCall<NotUsed, String> deleteEmployee(String documentId) {
        return request -> employeeRepository.deleteEmployeeById(documentId)
                .exceptionally(throwable -> {
                    Throwable cause = throwable.getCause();
                    if (cause instanceof DocumentDoesNotExistException)
                        throw new NotFound("Such document with " + documentId + "does not exist");
                    throw new RuntimeException(cause);
                });
    }


}
