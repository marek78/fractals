package com.szajna.fractals.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.szajna.fractals.model.ResolutionComboModel;

public class SaveOptionsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	public static final String ACTION_CMD_SAVE_OPTIONS_OK 				= "SaveOptions.OK";
	public static final String ACTION_CMD_SAVE_OPTIONS_CANCEL			= "SaveOptions.Cancel";
	public static final String ACTION_CMD_SAVE_OPTIONS_COMBO_RES_LIST	= "SaveOptions.comboResList";
	public static final String ACTION_CMD_SAVE_OPTIONS_RADIO_VIEW_RES	= "SaveOptions.RadioViewRes";
	public static final String ACTION_CMD_SAVE_OPTIONS_RADIO_SCREEN_RES	= "SaveOptions.RadioScreenRes";
	public static final String ACTION_CMD_SAVE_OPTIONS_RADIO_CUSTOM_RES	= "SaveOptions.RadioCustomRes";
	
	private JButton buttonOK;
	private JButton buttonCancel;
	private JComboBox comboResList;
	
	private JRadioButton radioViewRes;
	private JRadioButton radioScreenRes;
	private JRadioButton radioCustomRes;
	
	public SaveOptionsDialog(Frame owner, String title, boolean modal) {
		
		super(owner, title, modal);
		
		this.setMinimumSize(new Dimension(240, 0));
		this.setResizable(false);
		
	    JPanel optionsPanel = new JPanel(new GridLayout(0,1,3,3));
	    optionsPanel.setBorder(BorderFactory.createTitledBorder("Resolution"));

	    radioViewRes = new JRadioButton();
	    radioScreenRes = new JRadioButton();
	    radioCustomRes = new JRadioButton();
	    setRadioButtonTexts(0, 0);
	    
	    comboResList = new JComboBox(new ResolutionComboModel());
	    buttonOK = new JButton("OK");
	    buttonCancel = new JButton("Cancel");

	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    buttonPanel.add(buttonOK);
	    buttonPanel.add(buttonCancel);
	    
	    optionsPanel.add(radioViewRes);
	    optionsPanel.add(radioScreenRes);
	    optionsPanel.add(radioCustomRes);
	    optionsPanel.add(comboResList);

	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	    mainPanel.add(optionsPanel);
	    mainPanel.add(buttonPanel);
	    
	    ButtonGroup group = new ButtonGroup();
	    group.add(radioViewRes);
	    group.add(radioScreenRes);
	    group.add(radioCustomRes);

	    buttonOK.setActionCommand(ACTION_CMD_SAVE_OPTIONS_OK);
	    buttonCancel.setActionCommand(ACTION_CMD_SAVE_OPTIONS_CANCEL);
	    comboResList.setActionCommand(ACTION_CMD_SAVE_OPTIONS_COMBO_RES_LIST);
	    radioViewRes.setActionCommand(ACTION_CMD_SAVE_OPTIONS_RADIO_VIEW_RES);
	    radioScreenRes.setActionCommand(ACTION_CMD_SAVE_OPTIONS_RADIO_SCREEN_RES);
	    radioCustomRes.setActionCommand(ACTION_CMD_SAVE_OPTIONS_RADIO_CUSTOM_RES);
	    
	    this.getContentPane().add(mainPanel, BorderLayout.PAGE_START);
	    this.pack();
	    this.setLocationRelativeTo(owner);
	}
	
	/**
	 * Set the radio buttons texts.
	 */
	public void setRadioButtonTexts(int fractalViewWidth, int fractalViewHeight) {
		
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		String screenSizeString = "" + screenSize.width + "x" + screenSize.height;
		String fractalSizeString = "" + fractalViewWidth + "x" + fractalViewHeight;
		
	    radioViewRes.setText("Use fractal view resolution (" + fractalSizeString + ")");
	    radioScreenRes.setText("Use screen resolution (" + screenSizeString + ")");
	    radioCustomRes.setText("Use custom resolution");
	}
	
	public JButton getButtonOK() {
		return buttonOK;
	}

	public JButton getButtonCancel() {
		return buttonCancel;
	}

	public JComboBox getComboResList() {
		return comboResList;
	}

	public JRadioButton getRadioViewRes() {
		return radioViewRes;
	}

	public JRadioButton getRadioScreenRes() {
		return radioScreenRes;
	}

	public JRadioButton getRadioCustomRes() {
		return radioCustomRes;
	}
}

