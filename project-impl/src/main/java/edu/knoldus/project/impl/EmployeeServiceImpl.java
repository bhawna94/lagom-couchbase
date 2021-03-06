package edu.knoldus.project.impl;

import akka.NotUsed;
import com.couchbase.client.core.CouchbaseException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import edu.knoldus.project.api.Employee;
import edu.knoldus.project.api.EmployeeService;
import javafx.beans.Observable;
import rx.Observer;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;


public class EmployeeServiceImpl implements EmployeeService {
    private Cluster cluster;
    private Bucket bucket;
    private ObjectMapper mapper = new ObjectMapper();

    public EmployeeServiceImpl() {
        cluster = couchbaseConnector();
        loadBucket(cluster);
    }


    private Cluster couchbaseConnector() {
        Config configuration = ConfigFactory.load("application.conf");

        try {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>");
            cluster = CouchbaseCluster.create(configuration.getString("couchbase_contact_point_one"));
            cluster.authenticate(configuration.getString("couchbase.cluster.username"),
                    configuration.getString("couchbase.cluster.password"));

            return cluster;
        } catch (CouchbaseException ex) {
            return null;
        }
    }

    private void loadBucket(Cluster cluster) {
        Config conf = ConfigFactory.load("application.conf");
        bucket = cluster.openBucket(conf.getString("bucket-name"));
    }

    @Override
    public ServiceCall<Employee, String> insert() {

        return request -> {
            String json;
            JsonNode jsonNode = null;
            try {
                json = mapper.writeValueAsString(request);
                jsonNode = mapper.readTree(json);

            } catch (IOException ex) {
                ex.getMessage();
            }

            JsonObject empData = JsonObject.fromJson(jsonNode.toString());
            bucket.upsert(JsonDocument.create(empData.get("ename").toString(), empData));

            return CompletableFuture.completedFuture("Done");


        };
    }

    @Override
    public ServiceCall<NotUsed, Employee> getEmployee(String documentId) {
        return request -> {
            Employee emp = null;
            JsonObject jsonObject = bucket.get(documentId).content();
            String jsonString = jsonObject.toString();
            try {
                emp = mapper.readValue(jsonString, Employee.class);
            } catch (IOException ex) {
                ex.getMessage();
            }
            return CompletableFuture.completedFuture(emp);

        };
    }

    @Override
    public ServiceCall<NotUsed, String> deleteEmployee(String documentId) {
        return request -> {
            bucket.remove(documentId);
            return CompletableFuture.completedFuture("Deleted");

        };
    }
}
