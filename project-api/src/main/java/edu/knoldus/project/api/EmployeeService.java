package edu.knoldus.project.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
;
import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.transport.Method.DELETE;
import static com.lightbend.lagom.javadsl.api.transport.Method.GET;
import static com.lightbend.lagom.javadsl.api.transport.Method.POST;


public interface EmployeeService extends Service {
    
    ServiceCall<Employee,String> insert();
    
    ServiceCall<NotUsed,Employee> getEmployee(String documentId);
    
    ServiceCall<NotUsed,Employee> getEmployeeByName(String name);
    
    ServiceCall <NotUsed,String> deleteEmployee(String documentId);
    
    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("demo").withCalls(
                Service.restCall(POST,"/api/post",this::insert),
                Service.restCall(GET,"/api/get/employee/:documentId",this::getEmployee),
                Service.restCall(DELETE,"/api/delete/employee/:documentId",this::deleteEmployee),
                Service.restCall(GET,"/api/get/name/:empName",this::getEmployeeByName)
                
        ).withAutoAcl(true);
    }
}
