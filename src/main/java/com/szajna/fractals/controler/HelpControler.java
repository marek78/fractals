package com.szajna.fractals.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.szajna.fractals.model.HelpModel;
import com.szajna.fractals.view.HelpDialog;

public class HelpControler implements ActionListener, ComponentListener {

    private HelpModel model;
    private HelpDialog view;
    
    public HelpControler(HelpModel model, HelpDialog view) {
        
        this.model = model;
        this.view = view;
        this.view.addComponentListener(this);
        this.view.getButtonOk().addActionListener(this);
        
        updateFromModel();
        view.pack();
        this.view.setLocationRelativeTo(this.view.getOwner());
    }

    public HelpDialog getView() {
        return view;
    }
    public HelpModel getModel() {
        return model;
    }
    
    private void updateFromModel() {
        view.getLabel().setText(model.getText());
    }
    
    @Override
    public void componentHidden(ComponentEvent event) {
    }

    @Override
    public void componentMoved(ComponentEvent event) {
    }

    @Override
    public void componentResized(ComponentEvent event) {
    }

    @Override
    public void componentShown(ComponentEvent event) {
        updateFromModel();
        view.pack();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        
        if (actionEvent.getActionCommand() == HelpDialog.ACTION_CMD_HELP_OK) {
            view.setVisible(false);
        }
    }
}
