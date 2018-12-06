package ui;

import util.EditorUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import locale.*;

public class EditorSettingPanel {
    private JPanel panel1;
    private JButton defaultButton;
    private JButton cancelButton;
    private JButton OKButton;
    private JTextArea previewEditorPane;
    private JComboBox textFontComboBox;
    private JComboBox fontStyleComboBox;
    private JSpinner fontSizeSpinner;
    private JPanel previewPanel;
    private JPanel commandPanel;
    private JComboBox foregroundColorComboBox;
    private JComboBox backgroundColorComboBox;
    private JPanel upside;
    private JPanel _previewPanel;
    private JPanel downside;
    private JLabel fgLabel;
    private JLabel bgLabel;
    private JLabel fontLabel;
    private JLabel fontStyleLabel;
    private JLabel fontSizeLabel;
    private JPanel fontSettingsPanel;
    private JPanel editorAppearancePanel;
    private JEditor editor;
    private JFrame frame;

    public EditorSettingPanel(final JEditor editor) {
        //LOCALE SETTINGS

        defaultButton.setText(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_COMMAND_DEFAULT);
        cancelButton.setText(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_COMMAND_CANCEL);
        OKButton.setText(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_COMMAND_OK);
        fgLabel.setText(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_EDITORSETTINGS_FG);
        bgLabel.setText(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_EDITORSETTINGS_BG);
        fontLabel.setText(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_FONTSETTINGS_FONTNAME);
        fontSizeLabel.setText(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_FONTSETTINGS_FONTSIZE);
        fontStyleLabel.setText(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_FONTSETTINGS_FONTSTYLE);
        fontSettingsPanel.setName(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_FONTSETTINGS_HEADER);
        editorAppearancePanel.setName(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_EDITORSETTINGS_HEADER);
        previewPanel.setName(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_PREVIEW_HEADER);


        this.editor = editor;
        frame = new JFrame(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_TITLE);
        frame.setContentPane(panel1);
        frame.setMinimumSize(new Dimension(320, 240));
        frame.setPreferredSize(new Dimension(480, 320));
        frame.setResizable(false);
        frame.setLocation((int) (editor.getFrame().getLocation().getX() * 1.5), (int) (editor.getFrame().getLocation().getY() * 1.5));

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });


        frame.pack();
        frame.setVisible(true);

        //require deepcopy
        final DefaultComboBoxModel textFontModel = new DefaultComboBoxModel();
        final DefaultComboBoxModel fontStyleModel = new DefaultComboBoxModel();
        final DefaultComboBoxModel fgModel = new DefaultComboBoxModel();
        final DefaultComboBoxModel bgModel = new DefaultComboBoxModel();

        for (int i = 0; i < editor.getFontSelectorComboBox().getModel().getSize(); i++) {
            textFontModel.addElement(editor.getFontSelectorComboBox().getModel().getElementAt(i));
        }
        for (int i = 0; i < editor.getFontStyleSelectorComboBox().getModel().getSize(); i++) {
            fontStyleModel.addElement(editor.getFontStyleSelectorComboBox().getModel().getElementAt(i));
        }
        for (int i = 0; i < editor.getFontColorSelectorComboBox().getModel().getSize(); i++) {
            fgModel.addElement(editor.getFontColorSelectorComboBox().getModel().getElementAt(i));
        }

        for (int i = 0; i < editor.getBackgroundSelectorComboBox().getModel().getSize(); i++) {
            bgModel.addElement(editor.getBackgroundSelectorComboBox().getModel().getElementAt(i));
        }

        previewEditorPane.setLineWrap(true);
        try {
            if (editor.getMainEditorPane().getSelectionStart() - editor.getMainEditorPane().getSelectionEnd() != 0) {
                previewEditorPane.setText((editor.getMainEditorPane().getSelectedText().length() > 36) ? editor.getMainEditorPane().getSelectedText().substring(0, 36) : editor.getMainEditorPane().getSelectedText() + "\n测试文本\nPreview123456");
            } else {
                previewEditorPane.setText((editor.getMainEditorPane().getText().length() > 36) ? editor.getMainEditorPane().getText(0, 36) : editor.getMainEditorPane().getText() + "\n测试文本\nPreview123456");
            }
        } catch (BadLocationException e) {
            previewEditorPane.setText(Strings_zh_CN.MENU_TOOLS_EDITORSETTINGS_PREVIEW_TEXT);
        }
        textFontModel.setSelectedItem(editor.getFontSelectorComboBox().getSelectedItem());
        fontStyleModel.setSelectedItem(editor.getFontStyleSelectorComboBox().getSelectedItem());
        fgModel.setSelectedItem(editor.getFontColorSelectorComboBox().getSelectedItem());
        bgModel.setSelectedItem(editor.getBackgroundSelectorComboBox().getSelectedItem());
        previewEditorPane.setForeground(editor.getMainEditorPane().getForeground());
        previewEditorPane.setBackground(editor.getMainEditorPane().getBackground());
        previewEditorPane.setFont(editor.getMainEditorPane().getFont());
        textFontComboBox.setModel(textFontModel);
        fontStyleComboBox.setModel(fontStyleModel);
        fontSizeSpinner.setModel(new SpinnerNumberModel(12, 6, 512, 1));
        fontSizeSpinner.setValue(editor.getMainEditorPane().getFont().getSize());
        foregroundColorComboBox.setModel(fgModel);
        backgroundColorComboBox.setModel(bgModel);

        textFontComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewEditorPane.setFont(EditorUtil.getFont(textFontComboBox.getModel().getSelectedItem().toString(), fontStyleComboBox.getSelectedIndex(), Integer.valueOf(fontSizeSpinner.getValue().toString()), previewEditorPane.getFont()));
            }
        });
        fontStyleComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewEditorPane.setFont(EditorUtil.getFont(textFontComboBox.getModel().getSelectedItem().toString(), fontStyleComboBox.getSelectedIndex(), Integer.valueOf(fontSizeSpinner.getValue().toString()), previewEditorPane.getFont()));
            }
        });

        foregroundColorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewEditorPane.setForeground(EditorUtil.getColor(foregroundColorComboBox.getModel().getSelectedItem().toString()));
            }
        });
        backgroundColorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewEditorPane.setBackground(EditorUtil.getColor(backgroundColorComboBox.getModel().getSelectedItem().toString()));
            }
        });
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                previewEditorPane.setFont(EditorUtil.getFont(textFontComboBox.getModel().getSelectedItem().toString(), fontStyleComboBox.getSelectedIndex(), Integer.valueOf(fontSizeSpinner.getValue().toString()), previewEditorPane.getFont()));
            }
        });
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.getBackgroundSelectorComboBox().setSelectedItem(bgModel.getSelectedItem());
                editor.getFontColorSelectorComboBox().setSelectedItem(fgModel.getSelectedItem());
                editor.getFontSizeSpinner().setValue(fontSizeSpinner.getValue());
                editor.getFontStyleSelectorComboBox().setSelectedItem(fontStyleModel.getSelectedItem());
                editor.getFontSelectorComboBox().setSelectedItem(textFontModel.getSelectedItem());
                frame.dispose();
            }
        });
        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFontModel.setSelectedItem("Courier");
                fontStyleModel.setSelectedItem(fontStyleModel.getElementAt(0));
                fontSizeSpinner.setValue(16);
                fgModel.setSelectedItem("Black");
                bgModel.setSelectedItem("White");
            }
        });
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
        panel1.setPreferredSize(new Dimension(360, 480));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null));
        upside = new JPanel();
        upside.setLayout(new BorderLayout(0, 0));
        panel1.add(upside, BorderLayout.NORTH);
        upside.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(8, 24, 0, 24), null));
        fontSettingsPanel = new JPanel();
        fontSettingsPanel.setLayout(new GridBagLayout());
        upside.add(fontSettingsPanel, BorderLayout.NORTH);
        fontSettingsPanel.setBorder(BorderFactory.createTitledBorder(null, "Font Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, fontSettingsPanel.getFont())));
        fontStyleComboBox = new JComboBox();
        fontStyleComboBox.setPreferredSize(new Dimension(128, 27));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fontSettingsPanel.add(fontStyleComboBox, gbc);
        fontStyleLabel = new JLabel();
        Font fontStyleLabelFont = this.$$$getFont$$$(null, -1, -1, fontStyleLabel.getFont());
        if (fontStyleLabelFont != null) fontStyleLabel.setFont(fontStyleLabelFont);
        fontStyleLabel.setInheritsPopupMenu(true);
        fontStyleLabel.setText("Font style");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        fontSettingsPanel.add(fontStyleLabel, gbc);
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(72, 27));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fontSettingsPanel.add(fontSizeSpinner, gbc);
        fontSizeLabel = new JLabel();
        Font fontSizeLabelFont = this.$$$getFont$$$(null, -1, -1, fontSizeLabel.getFont());
        if (fontSizeLabelFont != null) fontSizeLabel.setFont(fontSizeLabelFont);
        fontSizeLabel.setText("Font size");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        fontSettingsPanel.add(fontSizeLabel, gbc);
        fontLabel = new JLabel();
        Font fontLabelFont = this.$$$getFont$$$(null, -1, -1, fontLabel.getFont());
        if (fontLabelFont != null) fontLabel.setFont(fontLabelFont);
        fontLabel.setText("Text font");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        fontSettingsPanel.add(fontLabel, gbc);
        textFontComboBox = new JComboBox();
        textFontComboBox.setPreferredSize(new Dimension(192, 27));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fontSettingsPanel.add(textFontComboBox, gbc);
        editorAppearancePanel = new JPanel();
        editorAppearancePanel.setLayout(new GridBagLayout());
        upside.add(editorAppearancePanel, BorderLayout.CENTER);
        editorAppearancePanel.setBorder(BorderFactory.createTitledBorder(null, "Editor Appearance", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, editorAppearancePanel.getFont())));
        fgLabel = new JLabel();
        Font fgLabelFont = this.$$$getFont$$$(null, -1, -1, fgLabel.getFont());
        if (fgLabelFont != null) fgLabel.setFont(fgLabelFont);
        fgLabel.setText("Foreground color");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        editorAppearancePanel.add(fgLabel, gbc);
        bgLabel = new JLabel();
        Font bgLabelFont = this.$$$getFont$$$(null, -1, -1, bgLabel.getFont());
        if (bgLabelFont != null) bgLabel.setFont(bgLabelFont);
        bgLabel.setText("Background color");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        editorAppearancePanel.add(bgLabel, gbc);
        foregroundColorComboBox = new JComboBox();
        foregroundColorComboBox.setPreferredSize(new Dimension(108, 27));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editorAppearancePanel.add(foregroundColorComboBox, gbc);
        backgroundColorComboBox = new JComboBox();
        backgroundColorComboBox.setPreferredSize(new Dimension(108, 27));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editorAppearancePanel.add(backgroundColorComboBox, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 48, 0, 48);
        editorAppearancePanel.add(spacer1, gbc);
        _previewPanel = new JPanel();
        _previewPanel.setLayout(new BorderLayout(0, 0));
        panel1.add(_previewPanel, BorderLayout.CENTER);
        _previewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null));
        previewPanel = new JPanel();
        previewPanel.setLayout(new BorderLayout(0, 0));
        _previewPanel.add(previewPanel, BorderLayout.CENTER);
        previewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24), "Preview", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, previewPanel.getFont())));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        previewPanel.add(panel2, BorderLayout.CENTER);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24), null));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel2.add(panel3, BorderLayout.CENTER);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setAlignmentX(0.0f);
        scrollPane1.setAlignmentY(0.0f);
        scrollPane1.setOpaque(false);
        panel3.add(scrollPane1, BorderLayout.CENTER);
        previewEditorPane = new JTextArea();
        scrollPane1.setViewportView(previewEditorPane);
        final JSeparator separator1 = new JSeparator();
        _previewPanel.add(separator1, BorderLayout.NORTH);
        downside = new JPanel();
        downside.setLayout(new BorderLayout(0, 0));
        panel1.add(downside, BorderLayout.SOUTH);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        downside.add(panel4, BorderLayout.SOUTH);
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 24, 8, 24), null));
        commandPanel = new JPanel();
        commandPanel.setLayout(new BorderLayout(0, 0));
        panel4.add(commandPanel, BorderLayout.CENTER);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        commandPanel.add(panel5, BorderLayout.CENTER);
        defaultButton = new JButton();
        defaultButton.setText("Default");
        panel5.add(defaultButton, BorderLayout.WEST);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        panel5.add(panel6, BorderLayout.EAST);
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel6.add(cancelButton, gbc);
        OKButton = new JButton();
        OKButton.setText("OK");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel6.add(OKButton, gbc);
        final JSeparator separator2 = new JSeparator();
        downside.add(separator2, BorderLayout.CENTER);
        fontStyleLabel.setLabelFor(fontStyleComboBox);
        fontSizeLabel.setLabelFor(fontSizeSpinner);
        fontLabel.setLabelFor(textFontComboBox);
        fgLabel.setLabelFor(foregroundColorComboBox);
        bgLabel.setLabelFor(backgroundColorComboBox);
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
}
