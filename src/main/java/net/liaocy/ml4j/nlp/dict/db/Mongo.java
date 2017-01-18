/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.nlp.dict.db;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

/**
 *
 * @author liaocy
 */
public class Mongo {

    private static MongoClient client = null;
    private static String dbName = "dict";

    public static MongoDatabase getDB() {
        if (client == null) {
            client = new MongoClient("localhost", 27017);
        }
        return client.getDatabase(dbName);
    }

    public static void save(MongoCollection<Document> collection, Document document) {
        Object id = document.get("_id");
        if (id == null) {
            collection.insertOne(document);
        } else {
            collection.replaceOne(eq("_id", id), document, new UpdateOptions().upsert(true));
        }
    }
}
