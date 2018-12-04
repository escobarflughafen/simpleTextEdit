package util;
import locale.Strings_zh_CN;
import model.*;
import ui.EditorSettingPanel;
import ui.JEditor;
import ui.WordCount;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;


public class EditorUtil {
    private JEditor editor;
    final static HashMap<String, Color> colorMap = new HashMap<String, Color>(){{
        put("Black", Color.BLACK);
        put("White", Color.WHITE);
        put("Red", Color.RED);
        put("Teal", Color.getColor("008080"));
        put("Green", Color.GREEN);
        put("Cyan", Color.cyan);
        put("Violet", Color.getColor("800080"));
        put("Pink", Color.pink);
        put("Light Gray", Color.LIGHT_GRAY);
        put("Gray", Color.GRAY);
        put("Dark Gray", Color.darkGray);
        put("Yellow", Color.YELLOW);
        put("Blue", Color.blue);
        put("Magenta", Color.MAGENTA);
        put("Orange", Color.ORANGE);

    }};



    public static Font fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    public EditorUtil(JEditor editor){
        this.editor = editor;
    }

    public void fileAutoSave(){
        if(editor.isAutosaveToggled && editor.getCurrentFile().getFile() != null){
            if(editor.getCurrentFile().getCounter().getDiff() > editor.getCurrentFile().getCounter().getThreshold()){
                saveFile();
                System.out.println(editor.getCurrentFile().toString());
                editor.getCurrentFile().getCounter().setDiff(0);

            }
        }

    }

    public void redo(){

    }

    public Boolean isSaved(int i){
        return (editor.getFile(i).getStatus().equals(TextFile.Status.SAVED));
    }

    public void exitAction(){
        /*
        if (isSaved(0)){
            editor.exit();
        }   else{
            switch (editor.toSave()){
                case 0: //to save
                    editor.exit();
                case 1: // discard
                    break;
                case 2: // cancel
                    break;
            }

        }
        */
        editor.exit();
    }

    public static Color getColor(String color){
        if(colorMap.containsKey(color))
            return colorMap.get(color);
        else
            return Color.BLACK;

    }

    public static DefaultComboBoxModel getAllFonts2Model(){
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for(int i=0; i<fonts.length; i++){
            model.addElement(fonts[i].getName());
            System.out.println(fonts[i].getStyle());
        }
        return model;
    }

    public void setEditorExterior(String font, int style, int size, int bg, int fg){
        this.editor.getFontSelectorComboBox().getModel().setSelectedItem(font);
        this.editor.getFontStyleSelectorComboBox().setSelectedIndex(style);
        if(size < 6)
            this.editor.getFontSizeSpinner().setValue(6);
        else if (size > 512)
            this.editor.getFontSizeSpinner().setValue(512);
        else
            this.editor.getFontSizeSpinner().setValue(size);
        this.editor.getBackgroundSelectorComboBox().getModel().setSelectedItem(bg);
        this.editor.getFontColorSelectorComboBox().getModel().setSelectedItem(fg);
    }

    public static Font getFont(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    public DefaultTreeModel getFileTreeModel(String root){
        return null;
    }

    public void setMainEditorTagNum(int n){
        editor.getCurrentFile().write(editor.getMainEditorPane().getText());
        if(n >= editor.getEditorTabbedPane().getTabCount()) return;
        editor.getEditorTabbedPane().setSelectedIndex(n);
        editor.setCurrentFile(editor.getFiles().get(n));
        JPanel newPane = new JPanel();
        newPane.setLayout(new BorderLayout());
        newPane.add(editor.getRemovableEditorPane(), BorderLayout.CENTER);
        editor.getEditorTabbedPane().setComponentAt(n, newPane);
        editor.setNowEditingTag(n);
        editor.getMainEditorPane().setText(editor.getCurrentFile().toString());
        setTitleWithStatus(editor.getCurrentFile().getStatus());
    }



    public void newWindow(TextFile file){
        editor.getFiles().add(file);
        editor.getEditorTabbedPane().addTab(((file.getFile() == null) ? "Untitled" : file.getFile().getName()) + ((file.getStatus() == TextFile.Status.UNSAVED) ? " (unsaved)" : "") , null);
        setMainEditorTagNum(editor.getEditorTabbedPane().getTabCount() - 1);
    }

    public void openFileToNewWindow(File file){
        newWindow(new TextFile(file));
    }

    public void closeWindow(){
        // 弹出确认保存Dialog
//        editor.getCurrentFile().save();
        int action = -1;
        if(editor.getCurrentFile().getStatus() == TextFile.Status.UNSAVED){
            action = JOptionPane.showConfirmDialog(null, Strings_zh_CN.EDITOR_SETTINGS_COMMAND_SAVE + " " + editor.getEditorTabbedPane().getTitleAt(editor.getEditorTabbedPane().getSelectedIndex()) + Strings_zh_CN.QUESTION_MARK, Strings_zh_CN.EDITOR_CLOSE_WINDOW_TITLE, JOptionPane.YES_NO_CANCEL_OPTION);
            if(action == JOptionPane.OK_OPTION)
                saveFile();
        }
        if(action == JOptionPane.OK_OPTION || action == JOptionPane.NO_OPTION || editor.getCurrentFile().getStatus() == TextFile.Status.SAVED) {
            int index = editor.getEditorTabbedPane().getSelectedIndex();
            editor.getFiles().remove(index);
            if (editor.getFiles().size() > 0) {
                editor.getEditorTabbedPane().removeTabAt(index);
                if (index > 0) {
                    setMainEditorTagNum(index - 1);
                } else {
                    setMainEditorTagNum(index);
                }
            } else {
                /*
                    //create new Empty file
                    editor.getFiles().add(new TextFile());
                    setMainEditorTagNum(0);
                */
                editor.exit();

            }
        }


    }

    public void setFileStatus(TextFile.Status status){
        this.editor.getCurrentFile().setStatus(status);
        setTitleWithStatus(status);
    }

    public void saveFile(File file){
        editor.getCurrentFile().save(file);
    }

    public void saveFileAs(TextFile file){

    }

    public void saveFile(){
        editor.getCurrentFile().write(editor.getMainEditorPane().getText());
        if(editor.getCurrentFile().getFile() == null){
            // using JFileChooser
            ExplorerToSaveFile(editor.getCurrentFile());
        }
        else {
            editor.getCurrentFile().save();
        }
        if(editor.getCurrentFile().getStatus() == TextFile.Status.SAVED && editor.getCurrentFile().getFile() != null) {
            editor.getEditorTabbedPane().setTitleAt(editor.getEditorTabbedPane().getSelectedIndex(), this.editor.getCurrentFile().getFile().getName());
            editor.getFrame().setTitle(editor.getCurrentFile().getFile().getName());
        }

    }

    public void saveAll(){
        int tag = editor.getEditorTabbedPane().getSelectedIndex();
        for(int i=0; i<editor.getFiles().size(); i++){
            setMainEditorTagNum(i);
            saveFile();
        }
        setMainEditorTagNum(tag);
    }


    public void openWordCounter(){
        new WordCount((editor.getFrame().getLocation().x + editor.getFrame().getWidth()*2) / 5, (editor.getFrame().getLocation().y + editor.getFrame().getHeight()*2) / 5, this.editor);
    }

    public void ExplorerToOpenFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showDialog(null, "Open");
        File file = fileChooser.getSelectedFile();
        //detecting file has opened
        if(file != null){
            int chk = checkFileOpenedAt(file);
            if(chk != -1) {
                setMainEditorTagNum(chk);
                return;
            }
            openFileToNewWindow(file);
        }
    }

    public void ExplorerToSaveFile(TextFile file) {

        JFileChooser fileSaver = new JFileChooser();
        int selection = fileSaver.showSaveDialog(null);
        if (selection == JFileChooser.APPROVE_OPTION) {
            if (fileSaver.getSelectedFile() != null) {
                int override = 0;
                if (fileSaver.getSelectedFile().exists()) {
                    override = JOptionPane.showConfirmDialog(null, Strings_zh_CN.EDITOR_SAVE_OVERRIDE_FILE_MESG, Strings_zh_CN.EDITOR_SAVE_OVERRIDE_FILE_TITLE, JOptionPane.YES_NO_OPTION);
                }
                if (override == 0)
                    file.setFile(fileSaver.getSelectedFile());
                file.save();
            }

        }
    }

    public int checkFileOpenedAt(File file){
        for(int i=0; i<editor.getFiles().size(); i++){
            if(editor.getFiles().get(i).getFile() != null){
                if(editor.getFiles().get(i).getFile().equals(file)){
                    return i;
                }
            }

        }
        return -1;
    }

    public void select(int start, int end){
        if(!(start > end)){
            if(!(start > editor.getMainEditorPane().getText().length())){
                if(!(end > editor.getMainEditorPane().getText().length())){
                    editor.getMainEditorPane().setSelectionStart(start);
                    editor.getMainEditorPane().setSelectionEnd(end);
                }   else{
                    editor.getMainEditorPane().setSelectionStart(start);
                    editor.getMainEditorPane().setSelectionEnd(editor.getMainEditorPane().getText().length()-1);
                }
            }
        }
    }

    public void setTitleWithStatus(TextFile.Status status){
        editor.getFrame().setTitle(this.editor.getEditorTabbedPane().getTitleAt(this.editor.getEditorTabbedPane().getSelectedIndex()) + ((status == TextFile.Status.UNSAVED) ? Strings_zh_CN.EDITOR_UNSAVED_PLACEHOLDER : ""));
    }


    public void caretJumpTo(int pos){
        editor.getMainEditorPane().setCaretPosition(pos);
    }

    public int search(String text, String pattern){
        int pos;
        pos = Pattern.compile(pattern).matcher(text).end();
        return 0;
    }

    public int searchInRange(String pattern, int from, int to){
        int pos = 0;
        try {
            String text = editor.getMainEditorPane().getText(from, to - from);
            System.out.println(Pattern.compile("(.*)" + pattern + "(.*)").matcher(text).start());


        }catch (BadLocationException e){
            e.printStackTrace();
        }
        return pos;
    }

    public int searchAfter(String pattern, int from){
        return searchInRange(pattern, from, editor.getMainEditorPane().getText().length());
    }

    public int searchBefore(String pattern, int from){
        return searchInRange(pattern, 0, from);

    }

    public void replaceWith(String text, int start, int end){
        editor.getMainEditorPane().setText(editor.getMainEditorPane().getText().substring(0, start) + text + editor.getMainEditorPane().getText().substring(end));
        editor.getMainEditorPane().setCaretPosition(end);
    }

    public void replaceWith(String text){
        replaceWith(text, editor.getMainEditorPane().getSelectionStart(), editor.getMainEditorPane().getSelectionEnd());
    }

    public void addPlaceholderTo(final JTextField textComponent, final String placeholder){
        textComponent.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (textComponent.getText().equals("")) {
                    textComponent.setForeground(Color.gray);
                    textComponent.setText(placeholder);
                }
            }
        });
        textComponent.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                textComponent.setForeground(Color.BLACK);
                if (textComponent.getText().equals(placeholder)) {
                    textComponent.setText("");
                }
            }
        });
    }

    public void editorExit(){
        boolean allSaved = false;
        for(int i=0; i<editor.getFiles().size(); i++){
            if(editor.getFiles().get(i).getStatus() == TextFile.Status.UNSAVED)
                break;
            if(i == editor.getFiles().size() - 1 && editor.getFiles().get(i).getStatus() == TextFile.Status.SAVED)
                allSaved = true;
        }
        if(!allSaved) {
            /*
            int closeOperation = JOptionPane.showConfirmDialog(null, "Save all files?", "exit", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (closeOperation) {
                case JOptionPane.OK_OPTION:
                    for(int i=0; i<editor.getFiles().size(); i++){
                        setMainEditorTagNum(i);
                        closeWindow();
                    }

                case JOptionPane.NO_OPTION:
                    editor.getFrame().dispose();
                    editor.exit();
                    break;

                case JOptionPane.CANCEL_OPTION:
                    break;
            */
            int fileNum = editor.getFiles().size();
            for(int i=0; i<fileNum; i++){
                setMainEditorTagNum(0);
                closeWindow();
            }
        }   else{
            editor.exit();
        }
    }

    public void about(){
        JOptionPane.showConfirmDialog(null, "TextEditor v1.0.0", "About", JOptionPane.YES_NO_OPTION);
    }

    public void setMainEditorFont(){
        editor.getMainEditorPane().setFont(EditorUtil.getFont(editor.getFontSelectorComboBox().getModel().getSelectedItem().toString(), editor.getFontStyleSelectorComboBox().getSelectedIndex(), Integer.valueOf(editor.getFontSizeSpinner().getValue().toString()), editor.getMainEditorPane().getFont()));

    }

    public void setBackgroundAndForegroundColor(){
        editor.getMainEditorPane().setBackground(getColor(editor.getBackgroundSelectorComboBox().getModel().getSelectedItem().toString()));
        editor.getMainEditorPane().setForeground(getColor(editor.getFontColorSelectorComboBox().getModel().getSelectedItem().toString()));

    }

    public void openEditorSetting(){
        new EditorSettingPanel(this.editor);
    }
}
