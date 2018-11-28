package com.szajna.fractals.controler;

import static com.szajna.fractals.view.SaveOptionsDialog.ACTION_CMD_SAVE_OPTIONS_CANCEL;
import static com.szajna.fractals.view.SaveOptionsDialog.ACTION_CMD_SAVE_OPTIONS_COMBO_RES_LIST;
import static com.szajna.fractals.view.SaveOptionsDialog.ACTION_CMD_SAVE_OPTIONS_OK;
import static com.szajna.fractals.view.SaveOptionsDialog.ACTION_CMD_SAVE_OPTIONS_RADIO_CUSTOM_RES;
import static com.szajna.fractals.view.SaveOptionsDialog.ACTION_CMD_SAVE_OPTIONS_RADIO_SCREEN_RES;
import static com.szajna.fractals.view.SaveOptionsDialog.ACTION_CMD_SAVE_OPTIONS_RADIO_VIEW_RES;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.szajna.fractals.model.Resolution;
import com.szajna.fractals.model.SaveOptionsModel;
import com.szajna.fractals.model.SaveOptionsModel.ResolutionType;
import com.szajna.fractals.view.FractalView;
import com.szajna.fractals.view.SaveOptionsDialog;


public class SaveOptionsControler implements ActionListener, ComponentListener {

    private SaveOptionsModel model;
    private SaveOptionsDialog view;
    private FractalView fractalView;
    
    public SaveOptionsControler(SaveOptionsModel model, SaveOptionsDialog view, 
            FractalView fractalView) {
        
        this.model = model;
        this.view = view;
        this.fractalView = fractalView;
        
        this.view.addComponentListener(this);
        this.view.getButtonCancel().addActionListener(this);
        this.view.getButtonOK().addActionListener(this);
        this.view.getComboResList().addActionListener(this);
        this.view.getRadioViewRes().addActionListener(this);
        this.view.getRadioScreenRes().addActionListener(this);
        this.view.getRadioCustomRes().addActionListener(this);
        
        updateViewFromModel();
    }

    public SaveOptionsModel getModel() {
        return model;
    }

    public SaveOptionsDialog getView() {
        return view;
    }

    /**
     * ActionListener implementation.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand() == ACTION_CMD_SAVE_OPTIONS_CANCEL) {
            
            view.setVisible(false);
        } else if (actionEvent.getActionCommand() == ACTION_CMD_SAVE_OPTIONS_OK) {
            
            updateModel();  // OK accepts changes
            model.saveConfigFile();
            view.setVisible(false);
            
        } else if (actionEvent.getActionCommand() == ACTION_CMD_SAVE_OPTIONS_COMBO_RES_LIST) {
        } else if (actionEvent.getActionCommand() == ACTION_CMD_SAVE_OPTIONS_RADIO_VIEW_RES) {
            view.getComboResList().setEnabled(false);
        } else if (actionEvent.getActionCommand() == ACTION_CMD_SAVE_OPTIONS_RADIO_SCREEN_RES) {
            view.getComboResList().setEnabled(false);
        } else if (actionEvent.getActionCommand() == ACTION_CMD_SAVE_OPTIONS_RADIO_CUSTOM_RES) {
            view.getComboResList().setEnabled(true);
        }
    }

    /**
     * Synchronizes model to reflect the view state.
     */
    private void updateModel() {
        
        if (view.getRadioViewRes().isSelected()) {
            model.setResolutionType(ResolutionType.FRACTAL_VIEW);
        } else if (view.getRadioScreenRes().isSelected()) {
            model.setResolutionType(ResolutionType.SCREEN);
        } else if (view.getRadioCustomRes().isSelected()) {
            model.setResolutionType(ResolutionType.CUSTOM);
        } else {
            throw new IllegalStateException(
                    "SaveOptionsControler.updateModel: no known radio button selected");
        }
        model.setResolution((Resolution)view.getComboResList().getSelectedItem());
    }
    
    /**
     * Synchronizes view to reflect the model state.
     */
    private void updateViewFromModel() {

        view.getComboResList().setSelectedItem(model.getResolution());
        SaveOptionsModel.ResolutionType type =  model.getResolutionType();
        switch (type) {
        
        case FRACTAL_VIEW:
        default:
            view.getRadioViewRes().setSelected(true);
            view.getComboResList().setEnabled(false);
            break;
            
        case SCREEN:
            view.getRadioScreenRes().setSelected(true);
            view.getComboResList().setEnabled(false);
            break;
            
        case CUSTOM:
            view.getRadioCustomRes().setSelected(true);
            view.getComboResList().setEnabled(true);
            break;
        }
    }

    /**
     * ComponentListener implementation.
     */
    @Override
    public void componentHidden(ComponentEvent event) {
        updateViewFromModel();
    }

    /**
     * ComponentListener implementation.
     */
    @Override
    public void componentMoved(ComponentEvent event) {
    }

    /**
     * ComponentListener implementation.
     */
    @Override
    public void componentResized(ComponentEvent event) {
    }

    /**
     * ComponentListener implementation.
     */
    @Override
    public void componentShown(ComponentEvent event) {
        
        updateViewFromModel();
        int w = fractalView.getWidth();
        int h = fractalView.getHeight();
        view.setRadioButtonTexts(w, h);
    }
}

