package com.baekzombie.hudvoca.record;

import java.io.Serializable;

public class VocaInfo implements Serializable {
    private int id;
    private String vocabulary; //영단어
    private String mean; //뜻

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }
}
