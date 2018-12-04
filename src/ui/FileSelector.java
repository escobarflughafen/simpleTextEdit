package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileSelector {
    private JPanel panel1;
    private JTree fileExplorerTree;
    private JButton actionButton;
    private JButton cancelButton;
    private JComboBox partitionSelectionComboBox;
    private JTextField pathTextField;
    private JEditor editor;
    private JFrame frame;


    public FileSelector(JEditor editor, boolean isToOpen, ActionListener confirmBtnActionListener, int posX, int posY) {
        this.editor = editor;
        actionButton.addActionListener(confirmBtnActionListener);
        frame = new JFrame((isToOpen) ? "Open" : "Save As");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(320, 200));
        frame.setPreferredSize(new Dimension(320, 240));
        frame.setLocation(posX, posY);
        frame.pack();
        frame.setVisible(true);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
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
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        panel1.add(panel2, BorderLayout.CENTER);
        fileExplorerTree = new JTree();
        panel2.add(fileExplorerTree, BorderLayout.CENTER);
        partitionSelectionComboBox = new JComboBox();
        panel2.add(partitionSelectionComboBox, BorderLayout.NORTH);
        pathTextField = new JTextField();
        panel2.add(pathTextField, BorderLayout.SOUTH);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel1.add(panel3, BorderLayout.SOUTH);
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        panel3.add(cancelButton);
        actionButton = new JButton();
        actionButton.setText("Action");
        panel3.add(actionButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}