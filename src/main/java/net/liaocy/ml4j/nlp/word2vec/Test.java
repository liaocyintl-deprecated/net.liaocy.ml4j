/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.nlp.word2vec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.liaocy.ml4j.common.Language;
import net.liaocy.ml4j.exception.NotFoundTermIDException;
import net.liaocy.ml4j.nlp.dict.Term;
import net.liaocy.ml4j.nlp.morpheme.Morpheme;


/**
 *
 * @author liaocy
 */
public class Test {

    public static void main(String[] args) throws IOException, NotFoundTermIDException {
//        Collection<String> sentences = new ArrayList<>();
//        sentences.add("I have a dream.");
//        sentences.add("I wanna everyone be happy.");
//        new Train().train(sentences, Language.EN_US, "model1");

//        for (int i = 0; i < 30; i++) {
//            new Thread(new Test()).start();
//        }

//        DocumentPreprocessor dp = new DocumentPreprocessor(arg);
//        for (List<HasWord> sentence : dp) {
//          System.out.println(sentence);
//        }
        Predict pre = new Predict("model1");
        pre.getWordVector(0);
        pre.getWordsNearest(0, 3);
    }


}
