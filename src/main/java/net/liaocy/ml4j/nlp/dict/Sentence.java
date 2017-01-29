/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.nlp.dict;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    
    @Override
    public String toString(){
        String str = "";
        for(Term term : this){
            str += term.toString() + ";";
        }
        return str;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Sentence))
            return false; 
        
        Sentence sen = (Sentence) obj;
        if(this.size() != sen.size()){
            return false;
        }
        for(int i = 0; i < this.size(); i++) {
            if(!Objects.equals(this.get(i).getID(), sen.get(i).getID())){
                return false;
            }
        }
        return true;
    }
    
    public List<Integer> getTermIdsList(){
        List<Integer> ids = new ArrayList<>();
        this.forEach((term) -> {
            ids.add(term.getID());
        });
        return ids;
    }
}
