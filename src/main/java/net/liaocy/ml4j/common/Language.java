/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.common;

/**
 *
 * @author liaocy
 */
public enum Language {
    COMMON("COMMON", "Default", 1),
    EN_US("EN_US", "US English UTF-8", 2),
    JA_JP("JA_JP", "Japanese UTF-8", 3);

    private String name;
    private String description;
    private int index;

    private Language(String name, String description, int index) {
        this.name = name;
        this.description = description;
        this.index = index;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return description;
    }

    public int getIndex() {
        return index;
    }

    public static Language findByIndex(int index) {
        for (Language lang : Language.values()) {
            if (lang.getIndex() == index) {
                return lang;
            }
        }
        return null;
    }
}
