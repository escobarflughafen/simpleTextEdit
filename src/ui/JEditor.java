package ui;


import locale.Strings_zh_CN;
import model.TextFile;
import util.EditorUtil;
import util.FileUtil;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

public class JEditor {
    //privatep
    private EditorUtil editorUtil;
    private JFrame frame;
    private JPanel panel1;
    private JTabbedPane editorTabbedPane;
    private JComboBox fontSelectorComboBox;
    private JSpinner fontSizeSpinner;
    private JComboBox fontColorSelectorComboBox;
    private JComboBox backgroundSelectorComboBox;
    private JPanel menuBarPanel;
    private JButton wordCountButton;
    private JLabel cursorIndicatorLabel;
    private JPanel toolBarLeftPanel;
    private JPanel toolBarRightPanel;
    private JPanel toolBarMidPanel;
    private JPanel menuPanel;
    private JToolBar statusToolBar;
    private JTextField searchTextField;
    private JTextArea textEditorPane;
    private JComboBox fontStyleSelectorComboBox;
    private JLabel statusLabel;
    private JPanel EditorWindowPanel;
    private JToolBar settingsToolBar;
    private JPanel editorViewSettingsPanel;
    private JButton saveButton;
    private JPanel stylesOptionPane;
    private JPanel windowManagerPane;
    private JButton closeButton;
    private JButton openButton;
    private JButton newButton;
    private JPanel Panel1;
    private JPanel TextEditorPane;
    private JPanel removableEditorPane;
    private JScrollPane settingsToolBarScrollPane;
    private JButton Open;
    private JMenuBar menuBar;
    private JMenu openRecentMenu;
    private Vector<TextFile> files;
    private TextFile currentFile;
    private FileUtil fileUtil;
    private JPopupMenu menu;
    public boolean isAutosaveToggled;
    private int nowEditingTag;
    private String lastSearch;
    private int lastSearchPos;
    private int continueSearchPos;
    private int lastCaretPos = 0;


    public boolean search(String pattern) {
        boolean found = false;
        int currentPos = lastCaretPos;
        System.out.println(lastCaretPos);
        if (!pattern.equals(lastSearch)) {
            continueSearchPos = 0;
            lastSearch = pattern;
        }
        int findAt = editorUtil.search(pattern, currentPos, (currentPos != -1));
        if (findAt != -1) {
            found = true;
            textEditorPane.setSelectionStart(findAt);
            textEditorPane.setSelectionEnd(findAt + lastSearch.length());
            try {
                textEditorPane.getHighlighter().removeAllHighlights();
                textEditorPane.getHighlighter().addHighlight(findAt, findAt + lastSearch.length(), new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN));
            } catch (BadLocationException ble) {
                ble.printStackTrace();
            }
        }
        continueSearchPos = findAt + lastSearch.length();
        if (continueSearchPos == -1 || continueSearchPos >= textEditorPane.getText().length()) {
            continueSearchPos = 0;
        }
        return found;
    }

    public boolean searchAll(String pattern) {
        return true;
    }

    public JEditor() {
        lastSearch = "";
        lastSearchPos = 0;
        continueSearchPos = 0;
        newButton.setText(Strings_zh_CN.EDITOR_SETTINGS_COMMAND_NEW);
        openButton.setText(Strings_zh_CN.EDITOR_SETTINGS_COMMAND_OPEN);
        saveButton.setText(Strings_zh_CN.EDITOR_SETTINGS_COMMAND_SAVE);

        {
            DefaultComboBoxModel styleModel = new DefaultComboBoxModel();
            styleModel.addElement(Strings_zh_CN.FONT_STYLE_REGULAR);
            styleModel.addElement(Strings_zh_CN.FONT_STYLE_BOLD);
            styleModel.addElement(Strings_zh_CN.FONT_STYLE_ITALIC + "/" + Strings_zh_CN.FONT_STYLE_OBLIQUE);
            fontStyleSelectorComboBox.setModel(styleModel);
        }

        this.editorUtil = new EditorUtil(this);
        this.fileUtil = new FileUtil(this);
        this.files = new Vector<TextFile>();
        this.isAutosaveToggled = true;
        this.init();
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                searchTextField.setBackground(Color.WHITE);
                int currentPos = continueSearchPos;
                if (e.getKeyChar() == '\n') {
                    search(searchTextField.getText());
                }

                //update continueSearchPos
                // if (e.getKeyChar() == '\n') {
                //     continueSearchPos = currentPos + searchTextField.getText().length();
                // }
            }
        });

        // for init testing
        this.files.add(new TextFile());
        this.currentFile = files.get(0);
        this.currentFile.setStatus(TextFile.Status.SAVED);


//        textEditorPane.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                super.mouseClicked(e);
//                textEditorPane.setFont(EditorUtil.getFont(fontSelectorComboBox.getModel().getSelectedItem().toString(), fontStyleSelectorComboBox.getSelectedIndex(), Integer.valueOf(fontSizeSpinner.getValue().toString()), textEditorPane.getFont()));
//
//            }
//        });


        fontSelectorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.setMainEditorFont();
            }
        });
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                editorUtil.setMainEditorFont();
            }
        });
        fontStyleSelectorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.setMainEditorFont();
            }
        });
        fontColorSelectorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.setBackgroundAndForegroundColor();
            }
        });
        backgroundSelectorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.setBackgroundAndForegroundColor();
            }
        });


        textEditorPane.addInputMethodListener(new InputMethodListener() {
            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {

            }

            @Override
            public void caretPositionChanged(InputMethodEvent event) {
                wordCountButton.setText(textEditorPane.getCaretPosition() + "/" + String.valueOf(fileUtil.wc()));

            }

        });
        textEditorPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                editorUtil.setFileStatus(TextFile.Status.UNSAVED);

            }
        });
        textEditorPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                editorUtil.setFileStatus(TextFile.Status.UNSAVED);
            }
        });
        textEditorPane.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                wordCountButton.setText(textEditorPane.getCaretPosition() + "/" + String.valueOf(fileUtil.wc()));
            }
        });
        textEditorPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });
        searchTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        // Create Placeholder for Searchbox
        editorUtil.addPlaceholderTo(searchTextField, Strings_zh_CN.EDITOR_SEARCHBOX_PLACEHOLDER);


        textEditorPane.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                editorUtil.fileAutoSave();
                lastCaretPos = textEditorPane.getCaretPosition();
                try {
                    if (textEditorPane.getSelectionStart() != textEditorPane.getSelectionEnd()) {
                        wordCountButton.setText(textEditorPane.getSelectedText().length() + "/" + String.valueOf(fileUtil.wc()));
                    } else {
                        wordCountButton.setText(textEditorPane.getCaretPosition() + "/" + String.valueOf(fileUtil.wc()));
                    }
                    int lineNum = textEditorPane.getLineOfOffset(textEditorPane.getCaretPosition());
                    int columnNum = textEditorPane.getCaretPosition() - textEditorPane.getLineStartOffset(lineNum);
                    cursorIndicatorLabel.setText(Strings_zh_CN.EDITOR_POSITION_INDICATOR_LN + " " + Integer.valueOf(lineNum + 1) + Strings_zh_CN.EDITOR_POSITION_INDICATOR_COMMA_SEPERATOR + Strings_zh_CN.EDITOR_POSITION_INDICATOR_COL + " " + Integer.valueOf(columnNum + 1));

                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });

        wordCountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.openWordCounter();
            }
        });
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.newWindow(new TextFile());
            }
        });
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.ExplorerToOpenFile();
            }
        });


        editorTabbedPane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
            }
        });
        editorTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (nowEditingTag != editorTabbedPane.getSelectedIndex()) {
                    //move editing zone to current tab
                    editorUtil.setMainEditorTagNum(editorTabbedPane.getSelectedIndex());
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.saveFile();
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.closeWindow();
            }
        });
        searchTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                continueSearchPos = 0;
                searchTextField.setBackground(Color.WHITE);
                textEditorPane.getHighlighter().removeAllHighlights();
            }
        });
        searchTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                continueSearchPos = 0;
                searchTextField.setBackground(Color.WHITE);
                textEditorPane.getHighlighter().removeAllHighlights();
            }
        });
    }


    private void init() {
        fontColorSelectorComboBox.setToolTipText("select TextEdit's font size");
        fontColorSelectorComboBox.setToolTipText("select TextEdit's foreground color");
        backgroundSelectorComboBox.setToolTipText("select TextEdit's background color");
        fontStyleSelectorComboBox.setToolTipText("select TextEdit's font style");
        fontSizeSpinner.setModel(new SpinnerNumberModel(16, 6, 512, 1));

        fontSelectorComboBox.setModel(EditorUtil.getAllFonts2Model());
        fontSelectorComboBox.setSelectedItem("Courier");

        frame = new JFrame("TextEdit");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 640));
        frame.setPreferredSize(new Dimension(800, 640));
        frame.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - frame.getWidth() / 2, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) - frame.getHeight() / 2);

        editorTabbedPane.setTabPlacement(JTabbedPane.TOP);
        settingsToolBarScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        settingsToolBarScrollPane.getHorizontalScrollBar().setSize(0, 0);
        menuBar = new JMenuBar();
        menuBar.add(new JMenu(Strings_zh_CN.MENU_FILE));
        menuBar.getMenu(0).add(new JMenuItem(Strings_zh_CN.MENU_FILE_NEW)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.newWindow(new TextFile());
            }
        });
        menuBar.getMenu(0).add(new JMenuItem(Strings_zh_CN.MENU_FILE_OPEN)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.ExplorerToOpenFile();
            }
        });
        menuBar.getMenu(0).addSeparator();
        menuBar.getMenu(0).add(new JMenuItem(Strings_zh_CN.MENU_FILE_SAVE)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.saveFile();
            }
        });
        menuBar.getMenu(0).add(new JMenuItem(Strings_zh_CN.MENU_FILE_SAVEAS)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.ExplorerToSaveFile(currentFile);
            }
        });
        menuBar.getMenu(0).add(new JMenuItem(Strings_zh_CN.MENU_FILE_SAVEAS)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.saveAll();
            }
        });
        menuBar.getMenu(0).addSeparator();
        menuBar.getMenu(0).add(new JMenuItem(Strings_zh_CN.MENU_FILE_EXIT)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.editorExit();
            }
        });

        menuBar.add(new JMenu(Strings_zh_CN.MENU_EDIT));
        menuBar.getMenu(1).add(new JMenuItem(Strings_zh_CN.MENU_EDIT_UNDO)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        menuBar.getMenu(1).add(new JMenuItem(Strings_zh_CN.MENU_EDIT_REDO));
        menuBar.getMenu(1).addSeparator();
        JMenuItem cut = menuBar.getMenu(1).add(new JMenuItem(Strings_zh_CN.MENU_EDIT_CUT));
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditorPane.cut();
            }
        });

        menuBar.getMenu(1).add(new JMenuItem(Strings_zh_CN.MENU_EDIT_COPY)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditorPane.copy();
            }
        });

        menuBar.getMenu(1).add(new JMenuItem(Strings_zh_CN.MENU_EDIT_PASTE)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditorPane.paste();
            }
        });
        menuBar.getMenu(1).addSeparator();
        menuBar.getMenu(1).add(new JMenuItem(Strings_zh_CN.MENU_EDIT_FIND_AND_REPLACE)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.openFindAndReplace();
            }
        });
        //menuBar.getMenu(1).add(new JMenuItem(Strings_zh_CN.MENU_EDIT_FINDNEXT));
        //menuBar.getMenu(1).add(new JMenuItem(Strings_zh_CN.MENU_EDIT_FINDPREV));
        JMenu transformationMenu = new JMenu(Strings_zh_CN.MENU_EDIT_TRANSFORMATIONS);
        menuBar.getMenu(1).add(transformationMenu);
        transformationMenu.add(new JMenuItem(Strings_zh_CN.MENU_EDIT_TRANSFORMATIONS_UPPERCASE)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uppercased = textEditorPane.getSelectedText().toUpperCase();
                editorUtil.replaceWith(uppercased);
            }
        });
        transformationMenu.add(new JMenuItem(Strings_zh_CN.MENU_EDIT_TRANSFORMATIONS_LOWERCASE)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String lowercased = textEditorPane.getSelectedText().toLowerCase();
                editorUtil.replaceWith(lowercased);
            }
        });
        menuBar.getMenu(1).addSeparator();
        JMenuItem jmpTo = new JMenuItem(Strings_zh_CN.MENU_EDIT_JUMPTO);
        menuBar.getMenu(1).add(jmpTo);
        jmpTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = Integer.valueOf(JOptionPane.showInputDialog(null, Strings_zh_CN.MENU_EDIT_JUMPTO_MESG, Strings_zh_CN.MENU_EDIT_JUMPTO_TITLE, JOptionPane.QUESTION_MESSAGE));
                editorUtil.caretJumpTo(value);
            }
        });

        menuBar.add(new JMenu(Strings_zh_CN.MENU_TOOLS));
        menuBar.getMenu(2).add(new JMenuItem(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.openEditorSetting();
            }
        });
        menuBar.getMenu(2).add(new JMenuItem(Strings_zh_CN.MENU_TOOLS_WORDCOUNT)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.openWordCounter();
            }
        });
        menuBar.getMenu(2).addSeparator();
        final JCheckBoxMenuItem ast = new JCheckBoxMenuItem(Strings_zh_CN.MENU_TOOLS_AUTOSAVE);
        ast.setState(true);
        menuBar.getMenu(2).add(ast);
        ast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isAutosaveToggled = ast.getState();
            }
        });
        final JCheckBoxMenuItem lwt = new JCheckBoxMenuItem(Strings_zh_CN.MENU_TOOLS_LINEWRAP);
        lwt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditorPane.setLineWrap(lwt.getState());
            }
        });
        lwt.setState(true);
        menuBar.getMenu(2).add(lwt);

        menuBar.add(new JMenu(Strings_zh_CN.MENU_HELP));
        menuBar.getMenu(3).add(new JMenuItem(Strings_zh_CN.MENU_HELP_ABOUT)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorUtil.about();
            }
        });


        menuBar.setOpaque(true);
        menuBarPanel.add(menuBar);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                editorUtil.editorExit();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        frame.pack();
        frame.setVisible(true);

    }

    public void exit() {
        System.exit(1);
    }

    public JTextArea getMainEditorPane() {
        return this.textEditorPane;
    }

    public JSpinner getFontSizeSpinner() {
        return this.fontSizeSpinner;
    }

    public JComboBox getFontSelectorComboBox() {
        return this.fontSelectorComboBox;
    }

    public JComboBox getFontColorSelectorComboBox() {
        return this.fontColorSelectorComboBox;
    }

    public JComboBox getFontStyleSelectorComboBox() {
        return this.fontStyleSelectorComboBox;
    }

    public JComboBox getBackgroundSelectorComboBox() {
        return this.backgroundSelectorComboBox;
    }

    public int getTabNum() {
        return this.files.size();
    }

    public TextFile getFile(int index) {
        return this.files.get(index);
    }

    public TextFile getCurrentFile() {
        return this.currentFile;
    }

    public void setCurrentFile(TextFile file) {
        this.currentFile = file;
    }

    public JFrame getFrame() {
        return this.frame;
    }

    public Vector<TextFile> getFiles() {
        return this.files;
    }

    public JTabbedPane getEditorTabbedPane() {
        return this.editorTabbedPane;
    }

    public JPanel getRemovableEditorPane() {
        return removableEditorPane;
    }

    public void setNowEditingTag(int x) {
        nowEditingTag = x;
    }

    public JLabel getStatusLabel() {
        return this.statusLabel;
    }

    public JTextField getSearchTextField() {
        return this.searchTextField;
    }

    public EditorUtil getEditorUtil() {
        return this.editorUtil;
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
        menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout(0, 0));
        menuPanel.setBackground(new Color(-328966));
        menuPanel.setPreferredSize(new Dimension(256, 24));
        panel1.add(menuPanel, BorderLayout.NORTH);
        menuBarPanel = new JPanel();
        menuBarPanel.setLayout(new BorderLayout(0, 0));
        menuBarPanel.setBackground(new Color(-328966));
        menuPanel.add(menuBarPanel, BorderLayout.WEST);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setBackground(new Color(-328966));
        menuPanel.add(panel2, BorderLayout.EAST);
        searchTextField = new JTextField();
        Font searchTextFieldFont = this.$$$getFont$$$(null, -1, 12, searchTextField.getFont());
        if (searchTextFieldFont != null) searchTextField.setFont(searchTextFieldFont);
        searchTextField.setOpaque(false);
        searchTextField.setPreferredSize(new Dimension(256, 27));
        searchTextField.setText("");
        searchTextField.setToolTipText("Search...");
        panel2.add(searchTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        EditorWindowPanel = new JPanel();
        EditorWindowPanel.setLayout(new BorderLayout(0, 0));
        EditorWindowPanel.setMinimumSize(new Dimension(240, 240));
        panel1.add(EditorWindowPanel, BorderLayout.CENTER);
        editorTabbedPane = new JTabbedPane();
        editorTabbedPane.setBackground(new Color(-1250068));
        editorTabbedPane.setOpaque(true);
        editorTabbedPane.setRequestFocusEnabled(true);
        editorTabbedPane.setTabPlacement(1);
        EditorWindowPanel.add(editorTabbedPane, BorderLayout.CENTER);
        Panel1 = new JPanel();
        Panel1.setLayout(new BorderLayout(0, 0));
        Panel1.setPreferredSize(new Dimension(10000, 10000));
        editorTabbedPane.addTab("Untitled", Panel1);
        removableEditorPane = new JPanel();
        removableEditorPane.setLayout(new BorderLayout(0, 0));
        removableEditorPane.setPreferredSize(new Dimension(6728, 7522));
        removableEditorPane.setRequestFocusEnabled(true);
        Panel1.add(removableEditorPane, BorderLayout.CENTER);
        TextEditorPane = new JPanel();
        TextEditorPane.setLayout(new BorderLayout(0, 0));
        removableEditorPane.add(TextEditorPane, BorderLayout.CENTER);
        TextEditorPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null));
        final JScrollPane scrollPane1 = new JScrollPane();
        TextEditorPane.add(scrollPane1, BorderLayout.CENTER);
        textEditorPane = new JTextArea();
        textEditorPane.setFocusCycleRoot(false);
        Font textEditorPaneFont = this.$$$getFont$$$("Courier", Font.PLAIN, 16, textEditorPane.getFont());
        if (textEditorPaneFont != null) textEditorPane.setFont(textEditorPaneFont);
        textEditorPane.setLineWrap(true);
        scrollPane1.setViewportView(textEditorPane);
        statusToolBar = new JToolBar();
        statusToolBar.setFloatable(false);
        statusToolBar.setMinimumSize(new Dimension(428, 24));
        statusToolBar.setPreferredSize(new Dimension(428, 24));
        removableEditorPane.add(statusToolBar, BorderLayout.SOUTH);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        statusToolBar.add(panel3);
        toolBarLeftPanel = new JPanel();
        toolBarLeftPanel.setLayout(new BorderLayout(0, 0));
        panel3.add(toolBarLeftPanel, BorderLayout.WEST);
        wordCountButton = new JButton();
        wordCountButton.setActionCommand("");
        wordCountButton.setHorizontalTextPosition(0);
        wordCountButton.setInheritsPopupMenu(false);
        wordCountButton.setLabel("");
        wordCountButton.setText("");
        toolBarLeftPanel.add(wordCountButton, BorderLayout.CENTER);
        toolBarRightPanel = new JPanel();
        toolBarRightPanel.setLayout(new BorderLayout(0, 0));
        panel3.add(toolBarRightPanel, BorderLayout.EAST);
        cursorIndicatorLabel = new JLabel();
        cursorIndicatorLabel.setHorizontalAlignment(0);
        cursorIndicatorLabel.setHorizontalTextPosition(0);
        cursorIndicatorLabel.setText("");
        toolBarRightPanel.add(cursorIndicatorLabel, BorderLayout.CENTER);
        toolBarMidPanel = new JPanel();
        toolBarMidPanel.setLayout(new BorderLayout(0, 0));
        panel3.add(toolBarMidPanel, BorderLayout.CENTER);
        statusLabel = new JLabel();
        statusLabel.setHorizontalAlignment(0);
        statusLabel.setHorizontalTextPosition(0);
        statusLabel.setText("");
        toolBarMidPanel.add(statusLabel, BorderLayout.CENTER);
        settingsToolBarScrollPane = new JScrollPane();
        settingsToolBarScrollPane.setHorizontalScrollBarPolicy(30);
        settingsToolBarScrollPane.setOpaque(false);
        settingsToolBarScrollPane.setVerticalScrollBarPolicy(21);
        removableEditorPane.add(settingsToolBarScrollPane, BorderLayout.NORTH);
        settingsToolBar = new JToolBar();
        settingsToolBar.setBackground(new Color(-1250068));
        settingsToolBar.setEnabled(true);
        settingsToolBar.setFloatable(false);
        settingsToolBar.setFocusCycleRoot(false);
        settingsToolBar.setMaximumSize(new Dimension(2147483647, 32));
        settingsToolBar.setMinimumSize(new Dimension(399, 27));
        settingsToolBar.setPreferredSize(new Dimension(413, 32));
        settingsToolBar.setRollover(true);
        settingsToolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        settingsToolBarScrollPane.setViewportView(settingsToolBar);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 5));
        panel4.setBackground(new Color(-1250068));
        panel4.setMaximumSize(new Dimension(2147483647, 32));
        panel4.setMinimumSize(new Dimension(399, 27));
        panel4.setPreferredSize(new Dimension(413, 32));
        settingsToolBar.add(panel4);
        editorViewSettingsPanel = new JPanel();
        editorViewSettingsPanel.setLayout(new BorderLayout(0, 0));
        editorViewSettingsPanel.setBackground(new Color(-1250068));
        editorViewSettingsPanel.setDoubleBuffered(false);
        editorViewSettingsPanel.setEnabled(true);
        editorViewSettingsPanel.setMinimumSize(new Dimension(389, 27));
        editorViewSettingsPanel.setPreferredSize(new Dimension(403, 32));
        panel4.add(editorViewSettingsPanel, BorderLayout.CENTER);
        stylesOptionPane = new JPanel();
        stylesOptionPane.setLayout(new GridBagLayout());
        editorViewSettingsPanel.add(stylesOptionPane, BorderLayout.CENTER);
        fontSelectorComboBox = new JComboBox();
        Font fontSelectorComboBoxFont = this.$$$getFont$$$(null, -1, 12, fontSelectorComboBox.getFont());
        if (fontSelectorComboBoxFont != null) fontSelectorComboBox.setFont(fontSelectorComboBoxFont);
        fontSelectorComboBox.setMaximumSize(new Dimension(32767, 27));
        fontSelectorComboBox.setMinimumSize(new Dimension(96, 27));
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Courier");
        defaultComboBoxModel1.addElement("Consolas");
        defaultComboBoxModel1.addElement("Menlo");
        defaultComboBoxModel1.addElement("Serif");
        defaultComboBoxModel1.addElement("Times New Roman");
        defaultComboBoxModel1.addElement("Garamond");
        defaultComboBoxModel1.addElement("Hiragino Sans");
        defaultComboBoxModel1.addElement("Hiragino Serif");
        defaultComboBoxModel1.addElement("Toppan Bunkyu Mincho");
        fontSelectorComboBox.setModel(defaultComboBoxModel1);
        fontSelectorComboBox.setOpaque(false);
        fontSelectorComboBox.setPreferredSize(new Dimension(256, 27));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        stylesOptionPane.add(fontSelectorComboBox, gbc);
        fontColorSelectorComboBox = new JComboBox();
        Font fontColorSelectorComboBoxFont = this.$$$getFont$$$(null, -1, 12, fontColorSelectorComboBox.getFont());
        if (fontColorSelectorComboBoxFont != null) fontColorSelectorComboBox.setFont(fontColorSelectorComboBoxFont);
        fontColorSelectorComboBox.setMinimumSize(new Dimension(72, 27));
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("Black");
        defaultComboBoxModel2.addElement("White");
        defaultComboBoxModel2.addElement("Red");
        defaultComboBoxModel2.addElement("Teal");
        defaultComboBoxModel2.addElement("Green");
        defaultComboBoxModel2.addElement("Cyan");
        defaultComboBoxModel2.addElement("Violet");
        defaultComboBoxModel2.addElement("Pink");
        defaultComboBoxModel2.addElement("Light Gray");
        defaultComboBoxModel2.addElement("Gray");
        defaultComboBoxModel2.addElement("Dark Gray");
        defaultComboBoxModel2.addElement("Yellow");
        defaultComboBoxModel2.addElement("Blue");
        defaultComboBoxModel2.addElement("Customize...");
        fontColorSelectorComboBox.setModel(defaultComboBoxModel2);
        fontColorSelectorComboBox.setPreferredSize(new Dimension(80, 27));
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        stylesOptionPane.add(fontColorSelectorComboBox, gbc);
        backgroundSelectorComboBox = new JComboBox();
        Font backgroundSelectorComboBoxFont = this.$$$getFont$$$(null, -1, 12, backgroundSelectorComboBox.getFont());
        if (backgroundSelectorComboBoxFont != null) backgroundSelectorComboBox.setFont(backgroundSelectorComboBoxFont);
        backgroundSelectorComboBox.setMinimumSize(new Dimension(64, 27));
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("White");
        defaultComboBoxModel3.addElement("Black");
        defaultComboBoxModel3.addElement("Red");
        defaultComboBoxModel3.addElement("Teal");
        defaultComboBoxModel3.addElement("Green");
        defaultComboBoxModel3.addElement("Cyan");
        defaultComboBoxModel3.addElement("Violet");
        defaultComboBoxModel3.addElement("Pink");
        defaultComboBoxModel3.addElement("Light Gray");
        defaultComboBoxModel3.addElement("Gray");
        defaultComboBoxModel3.addElement("Dark Gray");
        defaultComboBoxModel3.addElement("Yellow");
        defaultComboBoxModel3.addElement("Blue");
        defaultComboBoxModel3.addElement("Customize...");
        backgroundSelectorComboBox.setModel(defaultComboBoxModel3);
        backgroundSelectorComboBox.setPreferredSize(new Dimension(80, 27));
        backgroundSelectorComboBox.setSelectedIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        stylesOptionPane.add(backgroundSelectorComboBox, gbc);
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setAlignmentY(0.5f);
        Font fontSizeSpinnerFont = this.$$$getFont$$$(null, -1, 12, fontSizeSpinner.getFont());
        if (fontSizeSpinnerFont != null) fontSizeSpinner.setFont(fontSizeSpinnerFont);
        fontSizeSpinner.setMaximumSize(new Dimension(48, 327));
        fontSizeSpinner.setMinimumSize(new Dimension(48, 26));
        fontSizeSpinner.setPreferredSize(new Dimension(48, 28));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        stylesOptionPane.add(fontSizeSpinner, gbc);
        fontStyleSelectorComboBox = new JComboBox();
        Font fontStyleSelectorComboBoxFont = this.$$$getFont$$$(null, -1, 12, fontStyleSelectorComboBox.getFont());
        if (fontStyleSelectorComboBoxFont != null) fontStyleSelectorComboBox.setFont(fontStyleSelectorComboBoxFont);
        fontStyleSelectorComboBox.setMinimumSize(new Dimension(64, 27));
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        fontStyleSelectorComboBox.setModel(defaultComboBoxModel4);
        fontStyleSelectorComboBox.setPreferredSize(new Dimension(108, 27));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        stylesOptionPane.add(fontStyleSelectorComboBox, gbc);
        windowManagerPane = new JPanel();
        windowManagerPane.setLayout(new BorderLayout(0, 0));
        editorViewSettingsPanel.add(windowManagerPane, BorderLayout.WEST);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        windowManagerPane.add(panel5, BorderLayout.CENTER);
        newButton = new JButton();
        newButton.setMaximumSize(new Dimension(48, 27));
        newButton.setMinimumSize(new Dimension(32, 27));
        newButton.setPreferredSize(new Dimension(48, 27));
        newButton.setText("New");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(newButton, gbc);
        openButton = new JButton();
        openButton.setMaximumSize(new Dimension(48, 27));
        openButton.setMinimumSize(new Dimension(32, 27));
        openButton.setPreferredSize(new Dimension(48, 27));
        openButton.setText("Open");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(openButton, gbc);
        saveButton = new JButton();
        saveButton.setMaximumSize(new Dimension(48, 27));
        saveButton.setMinimumSize(new Dimension(32, 27));
        saveButton.setPreferredSize(new Dimension(48, 27));
        saveButton.setText("Save");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(saveButton, gbc);
        closeButton = new JButton();
        closeButton.setMinimumSize(new Dimension(12, 27));
        closeButton.setPreferredSize(new Dimension(27, 27));
        closeButton.setText("×");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(closeButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
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

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }


    /**
     * @noinspection ALL
     */


}
