package com.szajna.fractals.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HelpDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    public static final String ACTION_CMD_HELP_OK               = "Help.OK";
    
    private JLabel label;
    private JButton buttonOk;
    
    public HelpDialog(Frame owner, String title, boolean modal) {
        
        super(owner, title, modal);
        
        this.setMinimumSize(new Dimension(240, 240));
        this.setResizable(false);
        this.setBackground(Color.BLACK);
        
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        label = new JLabel();
        label.setOpaque(true);
        label.setFont(new Font("Verdana", Font.BOLD, 16));
        label.setBackground(Color.BLACK);
        label.setForeground(Color.GRAY);
        labelPanel.add(label);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonOk = new JButton("OK");
        buttonOk.setActionCommand(ACTION_CMD_HELP_OK);
        buttonPanel.add(buttonOk);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(labelPanel);
        mainPanel.add(buttonPanel);
        
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(owner);
    }

    public JLabel getLabel() {
        return label;
    }
    
    public JButton getButtonOk() {
        return buttonOk;
    }
}

