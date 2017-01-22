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
public class Sentence extends ArrayList<Term>{
    
    
    @Override
    public String toString(){
        String output = "";
        for(Term term: this){
            output += term.toString() + " ";
        }
        return output;
    }
}
