package model;

import util.TextUtil;

import java.io.File;

public class WordCounter {
    private long fsize;
    private int diff;
    private int threshold;
    private TextFile file;




    public WordCounter(TextFile file){
        this.file = file;
        this.fsize = wc(file.getFile());
        this.threshold = 64;
        this.diff = 0;
    }

    public static long wc(File file){
        if(file == null)    return 0;
        return file.getTotalSpace();
    }

    public void setFsize(long size){
        diff += size - this.fsize;
        this.fsize = size;
    }

    public void setDiff(int diff){
        this.diff = diff;

    }
    public int getDiff(){
        return diff;
    }

    public long getFsize(){
        return this.fsize;
    }

    public String toString(){
        return null;
    }

    public void setThreshold(int threshold){
        this.threshold = threshold;
    }

    public int getThreshold(){
        return this.threshold;
    }

    public Boolean isTimeToAutosave(){
        return diff > threshold;
    }
}
