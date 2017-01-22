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
public class Sentence extends ArrayList<Term> {

    public String joinIdByComma() {
        String result = "";
        for(Term term : this){
            result += term.getID() + ",";
        }
        if(result.length() > 1){
            result = result.substring(0,result.length()-1);
        }
        return result;
    }
    
    public String toString(){
        String str = "";
        for(Term term : this){
            str += term.toString() + ";";
        }
        return str;
    }
}
