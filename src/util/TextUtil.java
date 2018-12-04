package util;

import model.TextFile;
import model.WordCounter;

import java.io.File;
import java.util.Vector;

public class TextUtil {
    private TextFile textFile;
    private File asf;

    public TextUtil(TextFile textFile){
        this.textFile = textFile;

    }

    public void fileSave(){
        String str = textFile.toString();
        IOUtil.writeToFile(str, textFile.getFile());
    }

    public void fileSave(File file){
        String str = textFile.toString();
        IOUtil.writeToFile(str, file);
    }

    public void implictAutoSave(){
        WordCounter counter = textFile.getCounter();
        if(counter.isTimeToAutosave()){
            //autosave procedure
        }
    }

    public void AutoSave(){
        WordCounter counter = textFile.getCounter();
        if(counter.isTimeToAutosave()){
            //autosave procedure
        }
    }

    public void write(String text){
        textFile.setContents(text);
    }
}
