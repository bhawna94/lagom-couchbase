package edu.knoldus.project.impl.utils;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CouchbaseConnector {
    
    private Cluster cluster;
    
    private Config configuration = ConfigFactory.load();
    
    @Inject
    public CouchbaseConnector() {
    
    /*    CouchbaseEnvironment couchbaseEnvironment = DefaultCouchbaseEnvironment.builder()
                .connectTimeout(50000)
                .kvTimeout(50000)
                .build();
        
        cluster = CouchbaseCluster.create(couchbaseEnvironment, configuration.getString("couchbase_contact_point_one"));
    */
    cluster = CouchbaseCluster.create(configuration.getString("couchbase_contact_point_one"));
        
        cluster.authenticate(configuration.getString("couchbase.cluster.username"),
                configuration.getString("couchbase.cluster.password"));
        
    }
    
   
    
    public Bucket getBucket() {
        
        return cluster.openBucket(configuration.getString("bucket-name"));
    }
}
