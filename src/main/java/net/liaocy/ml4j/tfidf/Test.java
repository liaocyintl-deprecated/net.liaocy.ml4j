/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.tfidf;

import java.io.IOException;
import net.liaocy.ml4j.exception.NotFoundTermIDException;
import net.liaocy.ml4j.nlp.dict.Term;

/**
 *
 * @author liaocy
 */
public class Test {

    public static void main(String[] args) throws NotFoundTermIDException, IOException, ClassNotFoundException {
//        Term term = new Term(44612);
        tfidf tfidf = new tfidf("model1");
        tfidf.load();
        System.out.println(tfidf.getIdf(684));
    }
}
