package util;
import model.TextFile;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class IOUtil {
    public static Vector<String> fileToLines(File file){
        if(file != null) {
            Vector<String> lines = new Vector<String>();
            try {
                Scanner fscan = new Scanner(file);
                while (fscan.hasNext()) {
                    lines.add(fscan.nextLine());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (lines.size() == 0) lines.add("");
            return lines;
        }   else{
            return new Vector<String>(){{
                add("");
            }};
        }
    }

    public static String fileToStr(File file){

        String str = "";
        if(file != null){
            try {
                Scanner scanner = new Scanner(file);
                while(scanner.hasNextLine()){
                    str += scanner.nextLine();
                    str += "\n";
                }

                scanner.close();
            }   catch (Exception e){
                e.printStackTrace();
            }

        }
        return str;
    }

    /*public static TextFile createFile(String filename){

    }*/

    public static void createFile(String filename, String contents){

    }

    public static void createFile(URI uri){

    }

    public static void createFile(URI uri, String contents){

    }


    public static void writeToFile(String str, File file){
        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(str.getBytes());
            fos.close();
        }   catch(IOException e){
            e.getMessage();
        }
    }

    public static Vector<JMenuItem> files2MenuItems(File index){
        return null;
    }

}
