/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.nlp.dict;

import java.util.ArrayList;

/**
 *
 * @author liaocy
 */
public class Terms extends ArrayList<Term> {

    public String joinIdByComma() {
        String result = "";
        for(Term term : this){
            result += term.getID() + ",";
        }
        result = result.substring(0,result.length()-1);
        return result;
    }
    
}
