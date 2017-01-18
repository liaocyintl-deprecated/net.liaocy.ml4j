/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.nlp.dict;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import java.util.List;
import net.liaocy.ml4j.nlp.dict.db.Mongo;
import org.bson.Document;

/**
 *
 * @author liaocy
 */
public class Term {

    private Integer id;
    private String surface = null;
    private String pos = null;
    private String lang = null;
    private Double[] vector = null;

    private MongoDatabase db = Mongo.getDB();
    private MongoCollection<Document> colTerm = db.getCollection("term");
    
    public Term(int id) {
        this.id = id;
        findTermByID();
    }

    public Term(String surface, String pos) {
        this(surface, pos, "common");
    }
    
    

    public Term(String surface, String pos, String lang) {
        this.surface = surface;
        this.pos = pos;
        this.lang = lang;
        findOrInsertTermBySurfacePosLang();
    }

    private void findTermByID() {
        Document query = new Document("id", this.id);
        Document docTerm = this.colTerm.find(query).first();
        if (docTerm != null) {
            this.surface = docTerm.getString("s");
            this.pos = docTerm.getString("p");
            this.lang = docTerm.getString("l");
            
            List<Double> vec = (List<Double>) docTerm.get("v");
            if(vec == null){
                this.vector = null;
            } else {
                this.vector = new Double[vec.size()];
                for(int i = 0; i < vec.size(); i++){
                    this.vector[i] = vec.get(i);
                }
            }
            
        }
    }

    private void findOrInsertTermBySurfacePosLang() {
        Document query = new Document("s", this.surface).append("p", this.pos).append("l", this.lang);
        Document docTerm = this.colTerm.find(query).first();
        if (docTerm == null) {
            this.id = (int) colTerm.count();
            query.append("id", this.id);
            colTerm.insertOne(query);
        } else {
            this.id = docTerm.getInteger("id");
            
            List<Double> vec = (List<Double>) docTerm.get("v");
            if(vec == null){
                this.vector = null;
            } else {
                this.vector = new Double[vec.size()];
                for(int i = 0; i < vec.size(); i++){
                    this.vector[i] = vec.get(i);
                }
            }
            
        }
    }
    
    public Integer getID(){
        return this.id;
    }
    
    public String getSurface(){
        return this.surface;
    }
    
    public String getPOS(){
        return this.pos;
    }
    
    public void saveVector(Double[] vector){
        this.vector = vector;
        Document query = new Document("id", this.id);
        Document docTerm = this.colTerm.find(query).first();
        docTerm.append("v", Arrays.asList(vector));
        Mongo.save(colTerm, docTerm);
    }
    
    @Override
    public String toString(){
        return this.id + "," + this.getSurface() + "," + this.getPOS();
    }
}
