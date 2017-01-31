/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.tfidf;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Math.log;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.liaocy.ml4j.db.Mongo;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author liaocy
 */
public class tfidf {

    private HashMap<Integer, HashMap<Integer, Integer>> wordDocCount;
    private HashMap<Integer, Integer> docCount;
    private HashMap<Integer, Integer> wordCount;
    private String modelName;
    private Double idfMax = 0.;
    private Double idfMin = 100.;

    public tfidf(String modelName) {
        wordCount = new HashMap<>();
        docCount = new HashMap<>();
        wordDocCount = new HashMap<>();
        this.modelName = modelName;
    }
    

    public void add(int wordId, int docId) {
        if (!docCount.containsKey(docId)) {
            docCount.put(docId, 0);
        }
        docCount.put(docId, docCount.get(docId) + 1);

        if (!wordCount.containsKey(wordId)) {
            wordCount.put(wordId, 0);
        }
        wordCount.put(wordId, wordCount.get(wordId) + 1);

        if (!this.wordDocCount.containsKey(wordId)) {
            this.wordDocCount.put(wordId, new HashMap<Integer, Integer>());
        }
        Map<Integer, Integer> doc = this.wordDocCount.get(wordId);
        if (!doc.containsKey("docId")) {
            doc.put(docId, 0);
        }
        doc.put(docId, doc.get(docId) + 1);
    }

    public double getIdf(int wordId) {
        double idf = docCount.size();
        if (this.wordDocCount.containsKey(wordId)) {
            idf = idf / (1 + this.wordDocCount.get(wordId).size());
            return log(idf);
        }
        return 0.;
    }
    
    public double getNormalizeIdf(int wordId) {
         if (this.wordDocCount.containsKey(wordId)) {
            double idf = this.getIdf(wordId);
            return  (idf - this.idfMin) / (this.idfMax - this.idfMin);
         }
         return 0.;
    }

    public void save() throws IOException {
        MongoDatabase db = Mongo.getDB();
        GridFSBucket gridFSBucket = GridFSBuckets.create(db, "tfidfmodels");

        GridFSFile gfsfi = gridFSBucket.find(new Document("filename", this.modelName)).first();
        if (gfsfi != null) {
            ObjectId id = gfsfi.getObjectId();
            gridFSBucket.delete(id);
        }
        
        for(Entry<Integer, Integer> word : wordCount.entrySet()){
            double idf = this.getIdf(word.getValue());
            this.idfMax = Math.max(this.idfMax, idf);
            this.idfMin = Math.min(this.idfMax, idf);
        }

        try (GridFSUploadStream uploadStream = gridFSBucket.openUploadStream(this.modelName)) {
            try (ObjectOutputStream o = new ObjectOutputStream(uploadStream)) {
                o.writeObject(this.wordDocCount);
                o.writeObject(this.docCount);
                o.writeObject(this.wordCount);
                o.writeDouble(this.idfMax.doubleValue());
                o.writeDouble(this.idfMin.doubleValue());
            }
        }

        System.out.println("Save Model Successed!");
    }

    public void load() throws IOException, ClassNotFoundException {
        MongoDatabase db = Mongo.getDB();
        GridFSBucket gridFSBucket = GridFSBuckets.create(db, "tfidfmodels");
        try (GridFSDownloadStream stream = gridFSBucket.openDownloadStreamByName(this.modelName)) {
            try (ObjectInputStream objIn = new ObjectInputStream(stream)) {
                this.wordDocCount = (HashMap) objIn.readObject();
                this.docCount = (HashMap) objIn.readObject();
                this.wordCount = (HashMap) objIn.readObject();
                this.idfMax = objIn.readDouble();
                this.idfMin = objIn.readDouble();
            }
        }

        System.out.println("Loaded TFIDF Model: " + this.modelName);
    }
}
