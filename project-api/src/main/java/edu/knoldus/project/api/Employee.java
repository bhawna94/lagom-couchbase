package edu.knoldus.project.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor
@Value
@Builder
public class Employee {
    
    String empId;
    
    String empName;
}
