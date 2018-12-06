package util;
import locale.Strings_zh_CN;

import java.awt.Event;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;


public class UndoHelper {
    public static final String ACTION_KEY_UNDO = Strings_zh_CN.MENU_EDIT_UNDO;
    public static final String ACTION_KEY_REDO = Strings_zh_CN.MENU_EDIT_REDO;
    UndoManager undoManager = new UndoManager();

    public UndoHelper(JTextComponent textComponent) {
        ActionMap amap = textComponent.getActionMap();
        InputMap imap = textComponent.getInputMap();
        if (amap.get(ACTION_KEY_UNDO) == null) {
            UndoAction undoAction = new UndoAction();
            amap.put(ACTION_KEY_UNDO, undoAction);
            imap.put((KeyStroke) undoAction.getValue(Action.ACCELERATOR_KEY), ACTION_KEY_UNDO);
        }
        if (amap.get(ACTION_KEY_REDO) == null) {
            RedoAction redoAction = new RedoAction();
            amap.put(ACTION_KEY_REDO, redoAction);
            imap.put((KeyStroke) redoAction.getValue(Action.ACCELERATOR_KEY), ACTION_KEY_REDO);
        }
        textComponent.getDocument().addDocumentListener(new DocListener());
    }
    public UndoManager getUndoManager() { return undoManager; }

    class UndoAction extends AbstractAction {
        UndoAction() {
            super(Strings_zh_CN.MENU_EDIT_UNDO);
            putValue(MNEMONIC_KEY, new Integer('U'));
            putValue(SHORT_DESCRIPTION, Strings_zh_CN.MENU_EDIT_UNDO);
            putValue(LONG_DESCRIPTION, Strings_zh_CN.MENU_EDIT_UNDO);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('Z', Event.CTRL_MASK));
        }
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        }
    }


    class RedoAction extends AbstractAction {
        RedoAction() {
            super(Strings_zh_CN.MENU_EDIT_REDO);
            putValue(MNEMONIC_KEY, new Integer('R'));
            putValue(SHORT_DESCRIPTION, Strings_zh_CN.MENU_EDIT_REDO);
            putValue(LONG_DESCRIPTION, Strings_zh_CN.MENU_EDIT_REDO);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('Y', Event.CTRL_MASK));
        }
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        }
    }

    private class DocListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            if (e instanceof DefaultDocumentEvent) {
                DefaultDocumentEvent de = (DefaultDocumentEvent) e;
                undoManager.addEdit(de);
            }
        }
        public void removeUpdate(DocumentEvent e) {
            if (e instanceof DefaultDocumentEvent) {
                DefaultDocumentEvent de = (DefaultDocumentEvent) e;
                undoManager.addEdit(de);
            }
        }
        public void changedUpdate(DocumentEvent e) {

        }
    }

    public void redo(){
        if(undoManager.canRedo()){
            undoManager.redo();
        }
    }

    public void undo(){
        if(undoManager.canUndo()){
            undoManager.undo();
        }
    }
}