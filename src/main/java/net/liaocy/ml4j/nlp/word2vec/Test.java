/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.nlp.word2vec;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.liaocy.ml4j.common.Language;
import net.liaocy.ml4j.exception.NotFoundTermIDException;
import net.liaocy.ml4j.nlp.dict.Sentence;
import net.liaocy.ml4j.nlp.dict.Term;
import net.liaocy.ml4j.nlp.morpheme.Morpheme;


/**
 *
 * @author liaocy
 */
public class Test {

    public static void main(String[] args) throws IOException, NotFoundTermIDException {
//        Matrix b = Matrix.random(300,300);
//        SingularValueDecomposition svd = b.svd();
//        Matrix U =svd.getU();
//        Matrix V = svd.getV();
//        System.out.println("here");
        Predict predict = new Predict("newyork2");
        Sentence sen = predict.getWordsNearest(167, 50);
        System.out.println(sen);
    }


}
