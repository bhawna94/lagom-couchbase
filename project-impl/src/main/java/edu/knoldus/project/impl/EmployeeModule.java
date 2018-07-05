package edu.knoldus.project.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import edu.knoldus.project.api.EmployeeService;

public class EmployeeModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(EmployeeService.class, EmployeeServiceImpl.class);
    }
}