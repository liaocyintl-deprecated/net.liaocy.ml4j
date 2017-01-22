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
import net.liaocy.ml4j.common.Language;
import net.liaocy.ml4j.db.Mongo;
import net.liaocy.ml4j.exception.NotFoundTermIDException;
import org.bson.Document;

/**
 *
 * @author liaocy
 */
public class Term {

    private Integer id;
    private String surface = null;
    private String pos = null;
    private Language lang = null;
    private Double[] vector = null;
    private Double similarity = null;

    private MongoDatabase db = Mongo.getDB();
    private MongoCollection<Document> colTerm = db.getCollection("term");
    
    public Term(int id) throws NotFoundTermIDException {
        this.id = id;
        findTermByID();
    }

    public Term(String surface, String pos) {
        this(surface, pos, Language.COMMON);
    }
    
    

    public Term(String surface, String pos, Language lang) {
        this.surface = surface;
        this.pos = pos;
        this.lang = lang;
        findOrInsertTermBySurfacePosLang();
    }

    private void findTermByID() throws NotFoundTermIDException {
        Document query = new Document("id", this.id);
        Document docTerm = this.colTerm.find(query).first();
        if (docTerm != null) {
            this.surface = docTerm.getString("s");
            this.pos = docTerm.getString("p");
            this.lang = Language.findByIndex(docTerm.getInteger("l"));
            
            List<Double> vec = (List<Double>) docTerm.get("v");
            if(vec == null){
                this.vector = null;
            } else {
                this.vector = new Double[vec.size()];
                for(int i = 0; i < vec.size(); i++){
                    this.vector[i] = vec.get(i);
                }
            }
            
        } else {
            throw new NullPointerException("Not Found This Term ID");
        }
    }

    private void findOrInsertTermBySurfacePosLang() {
        Document query = new Document("s", this.surface).append("p", this.pos).append("l", this.lang.getIndex());
        Document docTerm = this.colTerm.find(query).first();
        if (docTerm == null) {
            synchronized (this) {
                this.id = (int) colTerm.count();
                query.append("id", this.id);
                colTerm.insertOne(query);
            }
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
    
    public void saveVector(float[] vector){
        double[] vectorfloat = new double[vector.length];
        for(int i = 0; i < vectorfloat.length; i++){
            vectorfloat[i] = vector[i];
        }
        saveVector(vectorfloat);
    }
    
    public void saveVector(double[] vector){
        Double[] vectorDouble = new Double[vector.length];
        for(int i = 0; i < vectorDouble.length; i++){
            vectorDouble[i] = vector[i];
        }
        saveVector(vectorDouble);
    }
    
    public void saveVector(Double[] vector){
        this.vector = vector;
        Document query = new Document("id", this.id);
        Document docTerm = this.colTerm.find(query).first();
        docTerm.append("v", Arrays.asList(vector));
        Mongo.save(colTerm, docTerm);
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }
    
    @Override
    public String toString(){
        return this.id + "," + this.getSurface() + "," + this.getPOS() + "," + this.getSimilarity();
    }
}
