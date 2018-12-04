package util;
import model.TextFile;
import ui.JEditor;

import java.io.*;
import java.util.*;


public class FileUtil {
    private JEditor editor;
    public FileUtil(JEditor editor){
        this.editor = editor;
    }

    public int wc(){

        editor.getCurrentFile().getCounter().setFsize(editor.getMainEditorPane().getText().length());
        editor.getCurrentFile().getCounter().setDiff(editor.getCurrentFile().getCounter().getDiff() + 1);
        return editor.getMainEditorPane().getText().length();
    }

    public static String textFileToString(TextFile file){
        return file.toString();
    }

}
