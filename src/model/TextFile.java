package model;

import util.IOUtil;
import util.TextUtil;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import static java.lang.Boolean.TRUE;

public class TextFile{
    private Status status;
    private TextUtil textUtil;
    private File file;
    private File backup;
    private WordCounter counter;
    private String contents;


    public enum Status{
        SAVED,
        UNSAVED
    }

    public TextFile(){
        this.file = null;
        this.status = Status.SAVED;
        this.textUtil = new TextUtil(this);
        this.counter = new WordCounter(this);
        this.contents = "";
        this.init(file);

    }

    public TextFile(File file){
        this.file = file;
        try {
            if (!this.file.exists()) this.file.createNewFile();
        }   catch (IOException ioe){
            ioe.getMessage();
        }
        this.init(file);

    }

    public TextFile(File file, int threshold){
        this.file = file;
        try {
            if (!this.file.exists()) this.file.createNewFile();
        }   catch (IOException ioe){
            ioe.getMessage();
        }
        this.init(file);
        this.counter.setThreshold(threshold);

    }


    public TextFile(String title){
        init(null);
    }


    public void init(File file){
        this.status = Status.SAVED;
        this.textUtil = new TextUtil(this);
        this.counter = new WordCounter(this);
        this.contents = IOUtil.fileToStr(file);
    }

    public Status getStatus(){
        return this.status;
    }

    public File getFile(){
        return this.file;
    }

    public void setFile(File file){
        this.file = file;
    }

    public WordCounter getCounter(){
        return counter;
    }

    public void setStatus(Status status){
        this.status = status;
    }


    public void setContents(String contents){
        this.contents = contents;
    }

    public String getContents(){
        return this.contents;
    }



    public String toString(){
        return this.contents;
    }

    public void write(String str){
        setStatus(Status.UNSAVED);
        this.textUtil.write(str);
    }

    public void save(){
        if(this.status == Status.UNSAVED)
            this.textUtil.fileSave();
        setStatus(Status.SAVED);
    }

    public void save(File file){
        try{
            if(!file.exists()){
                file.createNewFile();
                textUtil.fileSave(file);
                this.file = file;
            }   else{
                //add override block
                int status = JOptionPane.showConfirmDialog(null, "Override file?");
                if(status == JOptionPane.OK_OPTION){
                    textUtil.fileSave(file);
                    this.file = file;
                }
            }
        }catch (IOException e){
            e.getMessage();
        }

    }
}
