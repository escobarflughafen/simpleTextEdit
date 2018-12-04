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
    private JEditor editor;
    private JFrame frame;

    public EditorSettingPanel(final JEditor editor) {
        this.editor = editor;
        frame = new JFrame("Editor Settings");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(320, 240));
        frame.setPreferredSize(new Dimension(480, 320));

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
            previewEditorPane.setText("测试文本\nPreview123456");
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
                fontStyleModel.setSelectedItem("Regular");
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
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        upside.add(panel2, BorderLayout.NORTH);
        panel2.setBorder(BorderFactory.createTitledBorder(null, "Font Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel2.getFont())));
        fontStyleComboBox = new JComboBox();
        fontStyleComboBox.setPreferredSize(new Dimension(128, 27));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(fontStyleComboBox, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setInheritsPopupMenu(true);
        label1.setText("Font style");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label1, gbc);
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(72, 27));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(fontSizeSpinner, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, -1, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Font size");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label2, gbc);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, -1, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Text font");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label3, gbc);
        textFontComboBox = new JComboBox();
        textFontComboBox.setPreferredSize(new Dimension(192, 27));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(textFontComboBox, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        upside.add(panel3, BorderLayout.CENTER);
        panel3.setBorder(BorderFactory.createTitledBorder(null, "Editor Appearance", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel3.getFont())));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, -1, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Foreground color");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(label4, gbc);
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, -1, -1, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Background color");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(label5, gbc);
        foregroundColorComboBox = new JComboBox();
        foregroundColorComboBox.setPreferredSize(new Dimension(108, 27));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(foregroundColorComboBox, gbc);
        backgroundColorComboBox = new JComboBox();
        backgroundColorComboBox.setPreferredSize(new Dimension(108, 27));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(backgroundColorComboBox, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 48, 0, 48);
        panel3.add(spacer1, gbc);
        _previewPanel = new JPanel();
        _previewPanel.setLayout(new BorderLayout(0, 0));
        panel1.add(_previewPanel, BorderLayout.CENTER);
        _previewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null));
        previewPanel = new JPanel();
        previewPanel.setLayout(new BorderLayout(0, 0));
        _previewPanel.add(previewPanel, BorderLayout.CENTER);
        previewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24), "Preview", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, previewPanel.getFont())));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        previewPanel.add(panel4, BorderLayout.CENTER);
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24), null));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        panel4.add(panel5, BorderLayout.CENTER);
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setAlignmentX(0.0f);
        scrollPane1.setAlignmentY(0.0f);
        scrollPane1.setOpaque(false);
        panel5.add(scrollPane1, BorderLayout.CENTER);
        previewEditorPane = new JTextArea();
        scrollPane1.setViewportView(previewEditorPane);
        final JSeparator separator1 = new JSeparator();
        _previewPanel.add(separator1, BorderLayout.NORTH);
        downside = new JPanel();
        downside.setLayout(new BorderLayout(0, 0));
        panel1.add(downside, BorderLayout.SOUTH);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new BorderLayout(0, 0));
        downside.add(panel6, BorderLayout.SOUTH);
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 24, 8, 24), null));
        commandPanel = new JPanel();
        commandPanel.setLayout(new BorderLayout(0, 0));
        panel6.add(commandPanel, BorderLayout.CENTER);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new BorderLayout(0, 0));
        commandPanel.add(panel7, BorderLayout.CENTER);
        defaultButton = new JButton();
        defaultButton.setText("Default");
        panel7.add(defaultButton, BorderLayout.WEST);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridBagLayout());
        panel7.add(panel8, BorderLayout.EAST);
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(cancelButton, gbc);
        OKButton = new JButton();
        OKButton.setText("OK");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(OKButton, gbc);
        final JSeparator separator2 = new JSeparator();
        downside.add(separator2, BorderLayout.CENTER);
        label1.setLabelFor(fontStyleComboBox);
        label2.setLabelFor(fontSizeSpinner);
        label3.setLabelFor(textFontComboBox);
        label4.setLabelFor(foregroundColorComboBox);
        label5.setLabelFor(backgroundColorComboBox);
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
