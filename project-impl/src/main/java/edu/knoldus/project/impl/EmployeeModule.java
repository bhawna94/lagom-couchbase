package edu.knoldus.project.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import edu.knoldus.project.api.EmployeeService;
import edu.knoldus.project.impl.repository.EmployeeRepository;
import edu.knoldus.project.impl.repository.EmployeeRepositoryImpl;

public class EmployeeModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(EmployeeService.class, EmployeeServiceImpl.class);
        bind(EmployeeRepository.class).to(EmployeeRepositoryImpl.class);
    }
}