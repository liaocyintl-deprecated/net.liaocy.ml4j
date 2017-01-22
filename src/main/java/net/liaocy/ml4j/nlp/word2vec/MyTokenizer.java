/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.nlp.word2vec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.liaocy.ml4j.common.Language;
import net.liaocy.ml4j.nlp.dict.Term;
import net.liaocy.ml4j.nlp.morpheme.Morpheme;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;

/**
 *
 * @author liaocy
 */
class MyTokenizer implements Tokenizer {

    
    private TokenPreProcess preProcess;
    private List<String> termIds;
    private int index;

    public MyTokenizer(String toTokenize, Language lang) {

       this.termIds = new ArrayList<String>(Arrays.asList(toTokenize.split(",")));
       this. index = (this.termIds.isEmpty()) ? -1 : 0;
    }

    @Override
    public int countTokens() {
        return termIds.size();
    }

    @Override
    public List<String> getTokens() {
        List<String> ret = new ArrayList<>();
        while (hasMoreTokens()) {
            ret.add(nextToken());
        }
        return ret;
    }

    @Override
    public boolean hasMoreTokens() {
        if (index < 0) {
            return false;
        } else {
            return index < termIds.size();
        }
    }

    @Override
    public String nextToken() {
        if (index < 0) {
            return null;
        }

        String tok = termIds.get(index);
        index++;
        if (preProcess != null) {
            return preProcess.preProcess(tok);
        } else {
            return String.valueOf(tok);
        }
    }

    @Override
    public void setTokenPreProcessor(TokenPreProcess preProcess) {
        this.preProcess = preProcess;
    }

}
