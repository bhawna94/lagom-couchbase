package edu.knoldus.project.impl.repository;

import com.couchbase.client.core.CouchbaseException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.AbstractDocument;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryRow;
import com.couchbase.client.java.query.N1qlQuery;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import edu.knoldus.project.api.Employee;
import edu.knoldus.project.impl.utils.CouchbaseConnector;
import edu.knoldus.project.impl.utils.RxJavaUtil;
import rx.Observable;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    
    private Bucket bucket;
    
    private ObjectMapper mapper = new ObjectMapper();
    
    private static final Config CONFIG = ConfigFactory.load();
    
    private static final String BUCKET = CONFIG.getString("bucket-name");
    
    @Inject
    public EmployeeRepositoryImpl(CouchbaseConnector couchbaseConnector) {
     
        this.bucket = couchbaseConnector.getBucket();
    }
    
    @Override
    public CompletableFuture<Employee> getEmployeeById(String documentId) {
    
       
    
        Observable<JsonObject> jsonObject = bucket.async()
                .get(documentId)
                .map(AbstractDocument::content);
    
        return RxJavaUtil.toCompletableFuture(jsonObject)
                .thenApply(this::mapToEmployee);
    
    }
    
    @Override
    public CompletableFuture<Employee> getEmployeeByName(String name) {
    
        String query = "select empId,empName from Employee Where empName = \"" +name + "\"";
        Observable<JsonObject> row = bucket.async()
                .query(N1qlQuery.simple(query))
                .flatMap(queryResult -> queryResult.errors()
                        .flatMap(error ->
                            
                             Observable
                                    .<JsonObject>error(new CouchbaseException(error.toString()))
                        )
                        .switchIfEmpty(queryResult.rows().map(AsyncN1qlQueryRow::value))
                ).singleOrDefault(JsonObject.empty());
    
        return RxJavaUtil.toCompletableFuture(row).thenApply(this::mapToEmployee);
    }
    
    @Override
    public CompletableFuture<String> deleteEmployeeById(String employeeId) {
        bucket.remove(employeeId);
            return CompletableFuture.completedFuture("Deleted");
    }
    
    @Override
    public CompletableFuture<String> insertEmployee(Employee employee) {
        String json;
            JsonNode jsonNode = null;
            try {
                json = mapper.writeValueAsString(employee);
                jsonNode = mapper.readTree(json);

            } catch (IOException ex) {
                ex.getMessage();
            }

            JsonObject empData = JsonObject.fromJson(Objects.requireNonNull(jsonNode).toString());
        
            bucket.upsert(JsonDocument.create(empData.get("empId").toString(), empData));

            return CompletableFuture.completedFuture("Done");

    }
    
    
    private Employee mapToEmployee(JsonObject jsonObject) {
    
        Employee emp = Employee.builder().build();
        
        try {
            emp = mapper.readValue(jsonObject.toString(), Employee.class);
        } catch (IOException ex) {
            ex.getMessage();
        }
        
        return emp;
    }
}
