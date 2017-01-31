/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.nlp.word2vec;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import net.liaocy.ml4j.db.Mongo;
import net.liaocy.ml4j.exception.NotFoundTermIDException;
import net.liaocy.ml4j.nlp.dict.Term;
import net.liaocy.ml4j.nlp.dict.Sentence;
import net.liaocy.ml4j.tfidf.tfidf;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

/**
 *
 * @author liaocy
 */
public class Predict {
    private Word2Vec vec;
    private String modelName;
    public Predict(String modelName) throws IOException{
        this.modelName = modelName;
        System.out.println("Loading Word2Vec Model: " + this.modelName);
        this.load(this.modelName);
    }
    private void load(String modelName) throws IOException{
        MongoDatabase db = Mongo.getDB();
        GridFSBucket gridFSBucket = GridFSBuckets.create(db, "word2vecmodels");
        File file = File.createTempFile(modelName, ".w2v");
        OutputStream os = new FileOutputStream(file);
        gridFSBucket.downloadToStreamByName(modelName, os);
        os.close();
        this.vec = WordVectorSerializer.readWord2VecModel(file);
//        System.out.println(file.getAbsolutePath());
        if(!file.delete()){
            file.deleteOnExit();
        }
    }
    public void getWordVector(int wordId){
        double[] vector = this.vec.getWordVector(String.valueOf(wordId));
        for(double scalar : vector) {
            System.out.println(scalar);
        }
    }
    public Sentence getWordsNearest(int wordId, int n) throws NotFoundTermIDException{
        Collection<String> lsts = this.vec.wordsNearest(String.valueOf(wordId), n);
        Term term;
        Sentence terms = new Sentence();
        double simu;
        for(String lst : lsts){
            term = new Term(Integer.parseInt(lst));
            simu = this.getCosineSimilarity(wordId, term.getID());
            term.setSimilarity(simu);
            terms.add(term);
        }
        return terms;
    }
    public double getCosineSimilarity(int wordId1, int wordId2){
        return this.vec.similarity(String.valueOf(wordId1), String.valueOf(wordId2));
    }
    public double[] getAverageSentenceVector(Sentence sentence){
        double[] vec = new double[this.vec.getLayerSize()];
        int count = 0;
        for(Term term : sentence){
            double[] wordvec = this.vec.getWordVector(String.valueOf(term.getID()));
            if(wordvec != null && vec.length == wordvec.length) {
                for(int i = 0; i < vec.length; i++){
                    count++;
                    vec[i] += wordvec[i];
                }
            }
        }
        if(count == 0){
            return null;
        }
        for(int i = 0; i < vec.length; i++){
            vec[i] = vec[i] / count;
        }
        return vec;
    }
    public double[] getIdfWeightedAverageSentenceVector(Sentence sentence, tfidf tfidf){
        double[] vec = new double[this.vec.getLayerSize()];
        double countWeight = 0;
        
        for(Term term : sentence){
            double[] wordvec = this.vec.getWordVector(String.valueOf(term.getID()));
            double weight = tfidf.getNormalizeIdf(term.getID());
            if(wordvec != null && vec.length == wordvec.length) {
                countWeight += weight;
                for(int i = 0; i < vec.length; i++){
                    vec[i] += wordvec[i] * weight;
                }
            }
        }
        if(countWeight == 0){
            return null;
        }
        if(sentence.size() == 1){
            countWeight = 1;
        }
        for(int i = 0; i < vec.length; i++){
            vec[i] = vec[i] / countWeight;
        }
        return vec;
    }
    public double getIdfSentenceAverageWeight(Sentence sentence, tfidf tfidf){
        double countWeight = 0;
        double count = 0.;
        for(Term term : sentence){
            double[] wordvec = this.vec.getWordVector(String.valueOf(term.getID()));
            double weight = tfidf.getNormalizeIdf(term.getID());
            if(wordvec != null) {
                countWeight += weight;
                count++;
            }
        }
        if(count == 0){
            return 0;
        }
        return countWeight / count;
    }
}
