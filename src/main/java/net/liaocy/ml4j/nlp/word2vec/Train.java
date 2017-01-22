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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import net.liaocy.ml4j.common.Language;
import net.liaocy.ml4j.db.Mongo;
import net.liaocy.ml4j.nlp.dict.Terms;
import net.liaocy.ml4j.nlp.morpheme.Morpheme;
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

    public void train(Collection<String> sentences, Language lang, String modelName) throws IOException {
        //pos tags
        Collection<String> commaSentences = new ArrayList<>();
        Morpheme morpheme = new Morpheme(lang);
        Terms terms;
        for(String sentence: sentences){
            terms = morpheme.segment(sentence);
            commaSentences.add(terms.joinIdByComma());
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
                .learningRate( 0.025 )
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        System.out.println("Fitting Word2Vec model....");
        vec.fit();

        System.out.println("Save Model...");
        this.saveModel(modelName, vec);
    }

    private void saveModel(String modelName, Word2Vec vec) throws IOException  {
        MongoDatabase db = Mongo.getDB();
        GridFSBucket gridFSBucket = GridFSBuckets.create(db, "word2vecmodels");
        try (GridFSUploadStream uploadStream = gridFSBucket.openUploadStream(modelName)) {
            WordVectorSerializer.writeWord2VecModel(vec, uploadStream);
            System.out.println("Save Model Successed!");
        }
    }
}
