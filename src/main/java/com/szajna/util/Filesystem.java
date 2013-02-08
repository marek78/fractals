package com.szajna.util;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class Filesystem {

	private Filesystem() {}
	
	/**
	 * Gets user default directory.
	 * It is typically the "My Documents" folder on Windows, and the user's home directory on Unix. 
	 * @return user default directory.
	 */
	public static File getUserDefaultDir() {
		
		JFileChooser chooser = new JFileChooser();
	    FileSystemView view = chooser.getFileSystemView();
	    return view.getDefaultDirectory();
	}
}
