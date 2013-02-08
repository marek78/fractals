package com.szajna.fractals;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.szajna.fractals.view.MainWindow;

public class App {
	
	public static final String APP_NAME       	= "Fractals";
	public static final String APP_NAME_SPACED	= App.getSpacedAppName();
	public static final String APP_VERSION    	= "1.0";
	
	public static final String APP_CONFIG_DIR  	= ".fractals";
	public static final String APP_CONFIG_FILE 	= "fractals.cfg";
	
	public static void main(String args[]) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
	    SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainWindow mainWindow = new MainWindow(APP_NAME);
				mainWindow.setVisible(true);
			}
		});
	}
	
	private static String getSpacedAppName() {
		
		StringBuilder sb = new StringBuilder(APP_NAME.length());
		for (int i = 0; i < APP_NAME.length(); ++i) {
			
			if (i > 0) {
				sb.append(' ');
			}
			sb.append(APP_NAME.charAt(i));
		}
		return sb.toString().toUpperCase();
	}
}
