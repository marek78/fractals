package com.szajna.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    
    /**
     * Helper function which reads the file with the given name and returns 
     * the contents of this file as a String.
     * 
     * @param fileName The name of the file to read.
     * @return The contents of the file
     * @throws IOException 
     */
    public static String readFile(InputStream in) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while (true)
        {
            line = br.readLine();
            if (line == null)
            {
                break;
            }
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }
}
