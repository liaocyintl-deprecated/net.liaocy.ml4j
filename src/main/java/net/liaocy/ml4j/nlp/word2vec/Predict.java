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
}
