package com.szajna.fractals.controler;

import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

public class ExtFileFilter extends FileFilter {

    private String description;
    private String extensions[];

    public ExtFileFilter(String description, String extension) {
        this(description, new String[] { extension });
    }

    public ExtFileFilter(String description, String extensions[]) {
        
        if (description == null) {
            this.description = extensions[0];
        } else {
            this.description = description;
        }
        this.extensions = (String[]) extensions.clone();
        toLower(this.extensions);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean accept(File file) {

        if (file.isDirectory()) {
            return false;
        } else {
            
            String path = file.getAbsolutePath().toLowerCase();
            for (int i = 0; i < extensions.length; i++) {
                
                String extension = extensions[i];
                if ((path.endsWith(extension) && path.length() > extension.length() &&
                        (path.charAt(path.length() - extension.length() - 1)) == '.')) {
                    return true;
                }
            }
        }
        return false;
    }

    private void toLower(String array[]) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].toLowerCase(Locale.ENGLISH);
        }
    }
}
