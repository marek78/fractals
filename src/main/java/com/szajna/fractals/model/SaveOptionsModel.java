package com.szajna.fractals.model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

import com.szajna.fractals.App;
import com.szajna.util.Log;
import com.szajna.util.Filesystem;

public class SaveOptionsModel {

    public static enum ResolutionType {
        FRACTAL_VIEW,
        SCREEN,
        CUSTOM;
        
        private static final ResolutionType fromString(String type) {
            
            if (type.equals("FRACTAL_VIEW"))
                return FRACTAL_VIEW;
            else if (type.equals("SCREEN"))
                return SCREEN;
            else if (type.equals("CUSTOM"))
                return CUSTOM;
            else
                return null;
        }
    }

    private static final String LOG_TAG = SaveOptionsModel.class.getSimpleName();
    private static final String RESOLUTION_TYPE_KEY   = "resolutionType: ";
    private static final String CUSTOM_RESOLUTION_KEY = "customResolution: ";
    
    private ResolutionType resolutionType;
    private Resolution resolution;
    
    public SaveOptionsModel() {
        
        boolean readSuccessful = readConfigFile(); 
        if (! readSuccessful) { // use the defaults
            this.resolutionType = ResolutionType.FRACTAL_VIEW;
            this.resolution = Resolution.XGA;
        }
    }

    public ResolutionType getResolutionType() {
        return resolutionType;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolutionType(ResolutionType resolutionType) {
        this.resolutionType = resolutionType;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }
    
    /**
     * Saves configuration file.
     */
    public void saveConfigFile() {
        
        File file = new File(Filesystem.getUserDefaultDir(), 
                App.APP_CONFIG_DIR + System.getProperty("file.separator") + App.APP_CONFIG_FILE);
        
        Writer output = null;
        try {
            
            final File parentDirectory = file.getParentFile();
            if (null != parentDirectory) {
                parentDirectory.mkdirs();
            }
            
            file.createNewFile();
            output = new PrintWriter(new FileWriter(file));

            output.write(RESOLUTION_TYPE_KEY + resolutionType + "\n");
            output.write(CUSTOM_RESOLUTION_KEY + resolution + "\n");
            
        } catch (IOException e) {
            
            e.printStackTrace();
        } finally {
            
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Reads configuration file.
     * @return true if read was successful, otherwise false.
     */
    private boolean readConfigFile() {
        
        File file = new File(Filesystem.getUserDefaultDir(), 
                App.APP_CONFIG_DIR + System.getProperty("file.separator") + App.APP_CONFIG_FILE);

        ResolutionType resTypeFromFile = null;
        Resolution resFromFile = null;
        FileInputStream fstream = null;
        DataInputStream in = null;
        
        try {
            
            fstream = new FileInputStream(file);
            in = new DataInputStream(fstream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                
                if (line.startsWith(RESOLUTION_TYPE_KEY))
                    resTypeFromFile = ResolutionType.fromString(line.substring(RESOLUTION_TYPE_KEY.length()));
                else if (line.startsWith(CUSTOM_RESOLUTION_KEY))
                    resFromFile = Resolution.fromString(line.substring(CUSTOM_RESOLUTION_KEY.length()));
            }
            
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG, "No configuration file at: " + file.getAbsolutePath());
            Log.d(LOG_TAG, "Default config will be used");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if (resTypeFromFile != null && resFromFile != null) {
            
            this.resolutionType = resTypeFromFile;
            this.resolution = resFromFile;
            return true;
        } else {
            
            return false;
        }
    }
}

