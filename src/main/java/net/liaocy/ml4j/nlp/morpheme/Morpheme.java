/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.nlp.morpheme;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import net.liaocy.ml4j.common.Language;
import net.liaocy.ml4j.nlp.dict.Paragraph;
import net.liaocy.ml4j.nlp.dict.Sentence;
import net.liaocy.ml4j.nlp.dict.Term;
import net.liaocy.ml4j.nlp.dict.Terms;

/**
 *
 * @author liaocy
 */
public class Morpheme {

    private Language lang;
    private Properties props;
    private StanfordCoreNLP pipeline;
    private Annotation annotation;

    public Morpheme(Language lang) {
        this.lang = lang;

        switch (this.lang) {
            case JA_JP:

                break;
            case EN_US:
                this.props = new Properties();
                this.props.setProperty("annotators", "tokenize, ssplit, pos");
                this.pipeline = new StanfordCoreNLP(this.props);
                break;
        }
    }

    public Terms segment(String text) {
        switch (this.lang) {
            case JA_JP:

                break;
            case EN_US:
                return this.EN_US(text);
        }
        return null;
    }

    private  Terms EN_US(String text) {
        Terms terms = new Terms();
        this.annotation = new Annotation(text);
        this.pipeline.annotate(this.annotation);
        List<CoreMap> sens = this.annotation.get(CoreAnnotations.SentencesAnnotation.class);
        String surface, pos;
        for (CoreMap sen : sens) {
            for (CoreLabel token : sen.get(CoreAnnotations.TokensAnnotation.class)) {
                surface = token.get(CoreAnnotations.TextAnnotation.class);
                pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                Term term = new Term(surface, pos, this.lang);
                terms.add(term);
            }
        }
        
        return terms;
    }
}
