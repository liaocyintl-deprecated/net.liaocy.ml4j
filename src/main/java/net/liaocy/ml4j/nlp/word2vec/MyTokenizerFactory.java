/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.nlp.word2vec;

import java.io.InputStream;
import net.liaocy.ml4j.common.Language;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

/**
 *
 * @author liaocy
 */
class MyTokenizerFactory implements TokenizerFactory {

    private TokenPreProcess preProcess;
    private Language lang;
    
    MyTokenizerFactory(Language lang) {
        this.lang = lang;
    }

    @Override
    public Tokenizer create(String toTokenize) {
        if (toTokenize == null || toTokenize.isEmpty()) {
            throw new IllegalArgumentException("Unable to proceed; no sentence to tokenize");
        }

        MyTokenizer ret = new MyTokenizer(toTokenize, this.lang);
        ret.setTokenPreProcessor(preProcess);
        return ret;
    }

    @Override
    public void setTokenPreProcessor(TokenPreProcess preProcess) {
        this.preProcess = preProcess;
    }

    @Override
    public TokenPreProcess getTokenPreProcessor() {
        return this.preProcess;
    }

    @Override
    public Tokenizer create(InputStream toTokenize) {
        throw new UnsupportedOperationException();
    }
}
