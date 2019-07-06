package com.szajna.fractals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.szajna.fractals.model.FractalModel;

public class AppConfig {
    
    public static final String PROP_KEY_CALC_ITERATIONS = "calc.iterations";
    public static final String PROP_KEY_COLOR_THEME = "color.theme";
    
    private static final AppConfig instance = new AppConfig();
    private String configPath;

    private Properties defaultProperties = new Properties();
    private Properties appProperties;
    
    private AppConfig() {
        
        defaultProperties.setProperty(PROP_KEY_CALC_ITERATIONS, String.valueOf(FractalModel.ITERATIONS_COUNT_DEFAULT));
        defaultProperties.setProperty(PROP_KEY_COLOR_THEME, String.valueOf(0));
        
        String fs = System.getProperty("file.separator");
        configPath = System.getProperty("user.home") + fs + ".fractals" + fs + "config.txt";

        // if file already exists will do nothing
        File configFile = new File(configPath);
        try {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static AppConfig getInstance() {
        return instance;
    }
    
    public Properties getAppProperties() {
        return appProperties;
    }
    
    public int getIntProperty(String key) {
        
        String valueString = appProperties.getProperty(key);
        int value;
        
        if (valueString != null) {
            try {
                value = Integer.parseInt(valueString);
            } catch (NumberFormatException e) {
                value = Integer.MIN_VALUE;
            }
        } else {
            value = Integer.MIN_VALUE;
        }
        return value;
    }
    
    public void setIntProperty(String key, int value) {

        appProperties.setProperty(key, String.valueOf(value));
    }
    
    public void readFromFile() {
        
        appProperties = new Properties(defaultProperties);
        FileInputStream in;
        
        try {
            in = new FileInputStream(configPath);
            appProperties.load(in);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeToFile() {
        
        try {
            FileOutputStream out = new FileOutputStream(configPath);
            appProperties.store(out, "---App configuration---");
            out.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
