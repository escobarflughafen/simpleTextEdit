package ui;

import locale.Strings_zh_CN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Searcher {
    private JPanel panel1;
    private JTextField replaceWordTextField;
    private JButton replaceButton;
    private JTextField keyWordTextField;
    private JButton findButton;
    private JButton replaceAllButton;
    private JRadioButton nextRadioButton;
    private JRadioButton prevRadioButton;
    private JLabel directionLabel;
    private JLabel keyWordLabel;
    private JLabel replaceWordLabel;
    private JLabel resultLabel;
    private JEditor editor;
    private JFrame frame;

    private void search() {
        boolean found = editor.search(keyWordTextField.getText());
        if (found) {
            keyWordTextField.setBackground(Color.YELLOW);
            resultLabel.setText(Strings_zh_CN.EDITOR_STATUS_SEARCH_TEXT_0 + Strings_zh_CN.COMMA_MARK + Strings_zh_CN.EDITOR_STATUS_SEARCH_TEXT_POS + editor.getCursorIndicatorLabel().getText());
        } else {
            keyWordTextField.setBackground(Color.PINK);
            resultLabel.setText(Strings_zh_CN.EDITOR_STATUS_SEARCH_NOTFOUND);
        }
    }

    private void replace() {
        if (editor.getMainEditorPane().getSelectionStart() != editor.getMainEditorPane().getSelectionEnd() && editor.getMainEditorPane().getSelectedText().equals(keyWordTextField.getText())) {

            editor.getEditorUtil().replaceWith(replaceWordTextField.getText());
            resultLabel.setText(Strings_zh_CN.EDITOR_STATUS_REPLACED + Strings_zh_CN.COMMA_MARK + Strings_zh_CN.EDITOR_STATUS_SEARCH_TEXT_POS + editor.getCursorIndicatorLabel().getText());

        } else {
            boolean found = editor.search(keyWordTextField.getText());

            if (found) {
                int startpos = editor.getMainEditorPane().getSelectionStart();
                editor.getEditorUtil().replaceWith(replaceWordTextField.getText());
                editor.getMainEditorPane().setSelectionStart(startpos);
                editor.getMainEditorPane().setSelectionEnd(startpos + replaceWordTextField.getText().length());
                resultLabel.setText(Strings_zh_CN.EDITOR_STATUS_REPLACED + Strings_zh_CN.COMMA_MARK + Strings_zh_CN.EDITOR_STATUS_SEARCH_TEXT_POS + editor.getCursorIndicatorLabel().getText());

            } else {
                keyWordTextField.setBackground(Color.PINK);
                resultLabel.setText(Strings_zh_CN.EDITOR_STATUS_SEARCH_NOTFOUND);
            }
        }
    }

    private void replaceAll() {
        boolean found = editor.search(keyWordTextField.getText());
        if (found) {

            editor.getMainEditorPane().setText(editor.getMainEditorPane().getText().replaceAll(keyWordTextField.getText(), replaceWordTextField.getText()));
            resultLabel.setText(Strings_zh_CN.EDITOR_STATUS_ALL_REPLACED);
        } else {
            keyWordTextField.setBackground(Color.PINK);
            resultLabel.setText(Strings_zh_CN.EDITOR_STATUS_SEARCH_NOTFOUND);
        }
    }

    public Searcher(JEditor editor) {
        this.editor = editor;

        resultLabel.setText("");
        keyWordLabel.setText(Strings_zh_CN.SEARCH_KEYWORD_LABEL);
        replaceWordLabel.setText(Strings_zh_CN.SEARCH_REPLACE_LABEL);
        findButton.setText(Strings_zh_CN.SEARCH_COMMAND_FIND);
        replaceButton.setText(Strings_zh_CN.SEARCH_COMMAND_REPLACE);
        replaceAllButton.setText(Strings_zh_CN.SEARCH_COMMAND_REPLACE_ALL);
        findButton.addActionListener(e -> search());

        frame = new JFrame(Strings_zh_CN.SEARCH_WINDOW);
        frame.setContentPane(panel1);
        frame.setMinimumSize(new Dimension(64, 24));
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(480, 144));

        frame.setLocation((int) (editor.getFrame().getLocation().getX() * 1.5), (int) (editor.getFrame().getLocation().getY() * 1.5));
        frame.pack();
        frame.setVisible(true);
        keyWordTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                keyWordTextField.setBackground(Color.WHITE);
                if (e.getKeyChar() == '\n') {
                    search();
                }
            }
        });
        keyWordTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                keyWordTextField.setBackground(Color.WHITE);
            }
        });
        keyWordTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                keyWordTextField.setBackground(Color.WHITE);
            }
        });
        replaceButton.addActionListener(e -> replace());
        replaceWordTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == '\n') {
                    replace();
                }
            }
        });
        replaceAllButton.addActionListener(e -> replaceAll());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 4, new Insets(4, 8, 4, 8), -1, -1));
        panel1.add(panel2, BorderLayout.CENTER);
        replaceButton = new JButton();
        replaceButton.setText("Replace");
        panel2.add(replaceButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        replaceWordTextField = new JTextField();
        panel2.add(replaceWordTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        keyWordTextField = new JTextField();
        keyWordTextField.setText("");
        panel2.add(keyWordTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        findButton = new JButton();
        findButton.setText("Find");
        panel2.add(findButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        replaceAllButton = new JButton();
        replaceAllButton.setText("Replace All");
        panel2.add(replaceAllButton, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        keyWordLabel = new JLabel();
        keyWordLabel.setText("Label");
        panel2.add(keyWordLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        replaceWordLabel = new JLabel();
        replaceWordLabel.setText("Label");
        panel2.add(replaceWordLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resultLabel = new JLabel();
        resultLabel.setText("Label");
        panel2.add(resultLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        keyWordLabel.setLabelFor(keyWordTextField);
        replaceWordLabel.setLabelFor(replaceWordTextField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
