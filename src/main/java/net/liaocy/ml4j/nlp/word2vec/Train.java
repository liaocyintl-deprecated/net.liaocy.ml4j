/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.nlp.word2vec;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import net.liaocy.ml4j.common.Language;
import net.liaocy.ml4j.db.Mongo;
import net.liaocy.ml4j.nlp.dict.Sentence;
import net.liaocy.ml4j.nlp.morpheme.Morpheme;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;

/**
 *
 * @author liaocy
 */
public class Train {

    public void train(String[] sentences, Language lang, String modelName) throws IOException {
        //pos tags
        Collection<Sentence> sens = new ArrayList<>();
        Morpheme morpheme = new Morpheme(lang);
        Sentence terms;
        for (String sentence : sentences) {
            terms = morpheme.segment(sentence);
            sens.add(terms);
        }
        //pos tags -- end
        this.train(sens.toArray(new Sentence[sens.size()]), lang, modelName);
    }

    public void train(Sentence[] sentences, Language lang, String modelName) throws IOException {
        //pos tags
        Collection<String> commaSentences = new ArrayList<>();
        String commaSentence;
        for (Sentence sentence : sentences) {
            commaSentence = sentence.joinIdByComma();
            if (commaSentence.length() > 0) {
                commaSentences.add(commaSentence);
            }
        }
        //pos tags -- end

        System.out.println("Load & Vectorize Sentences....");
        SentenceIterator iter = new CollectionSentenceIterator(commaSentences);
        iter.setPreProcessor(new SentencePreProcessor() {
            @Override
            public String preProcess(String sentence) {
                return sentence;
            }
        });

        MyTokenizerFactory t = new MyTokenizerFactory(lang);
        t.setTokenPreProcessor(new TokenPreProcess() {
            @Override
            public String preProcess(String token) {
                return token;
            }
        });

        System.out.println("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(0)
                .iterations(1)
                .layerSize(200)
                .seed(42)
                .windowSize(5)
                .learningRate(0.025)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        System.out.println("Fitting Word2Vec model....");
        vec.fit();

        System.out.println("Save Model...");
        this.saveModel(modelName, vec);
    }

    private void saveModel(String modelName, Word2Vec vec) throws IOException {
        MongoDatabase db = Mongo.getDB();
        GridFSBucket gridFSBucket = GridFSBuckets.create(db, "word2vecmodels");
        
        GridFSFile gfsfi = gridFSBucket.find(new Document("filename", modelName)).first();
        if(gfsfi != null) {
            ObjectId id = gfsfi.getObjectId();
            gridFSBucket.delete(id);
        }
        
        try (GridFSUploadStream uploadStream = gridFSBucket.openUploadStream(modelName)) {
            WordVectorSerializer.writeWord2VecModel(vec, uploadStream);
            System.out.println("Save Model Successed!");
        }
    }
}
