package com.szajna.fractals.model;

import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class ResolutionComboModel implements ComboBoxModel {

    private ArrayList<Resolution> list = null;
    private Object selectedItem = null;

    public ResolutionComboModel() {
        
        list = new ArrayList<Resolution>();
        list.add(Resolution.VGA);
        list.add(Resolution.SVGA);
        list.add(Resolution.WSVGA);
        list.add(Resolution.XGA);
        list.add(Resolution.XGAPLUS);
        list.add(Resolution.WXGA_16_9);
        list.add(Resolution.WXGA_5_3);
        list.add(Resolution.WXGA_16_10);
        list.add(Resolution.SXGA_MINUS);
        list.add(Resolution.SXGA);
        list.add(Resolution.HD1);
        list.add(Resolution.HD2);
        list.add(Resolution.SXGA_PLUS);
        list.add(Resolution.WXGA_PLUS);
        list.add(Resolution.HD_PLUS);
        list.add(Resolution.UXGA);
        list.add(Resolution.WSXGA_PLUS);
        list.add(Resolution.FHD);
        list.add(Resolution.WUXGA);
        list.add(Resolution.QWXGA);
        list.add(Resolution.WQHD);
        list.add(Resolution.WQXGA);
    }
    
    @Override
    public void addListDataListener(ListDataListener arg0) {
    }

    @Override
    public Object getElementAt(int index) {
        if (index < 0 || index > list.size())
            return null;
        else
            return list.get(index);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public void removeListDataListener(ListDataListener arg0) {
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = anItem;
    }
}
