/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.app;

import com.app.palette.FilterItem;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;

/**
 *
 * @author ferdy
 */
public class PanelFilterPreview extends javax.swing.JPanel {

    private FilterItem filterItem;
    
    public PanelFilterPreview(FilterItem filterItem) {
        initComponents();
        labelName.setText(filterItem.getName());
        this.filterItem = filterItem;
    }
    
    public void setPreviewImage(Image image) {
        if(image == null) {
            return;
        }
        Image scaledImage = image.getScaledInstance(120, 80, Image.SCALE_SMOOTH);
        labelImage.setIcon(new ImageIcon(scaledImage)); 
    }
    
    public FilterItem getFilterItem() {
        return filterItem;
    }
    
    public void setSelected(boolean selected) {
        if(selected) {
            setBorder(new LineBorder(Color.cyan, 2));
        }
        else {
            setBorder(null);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelImage = new javax.swing.JLabel();
        labelName = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(200, 50));
        setPreferredSize(new java.awt.Dimension(100, 100));
        setLayout(new java.awt.BorderLayout());

        labelImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelImage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(labelImage, java.awt.BorderLayout.CENTER);

        labelName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelName.setMinimumSize(new java.awt.Dimension(0, 20));
        labelName.setPreferredSize(new java.awt.Dimension(0, 20));
        add(labelName, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelImage;
    private javax.swing.JLabel labelName;
    // End of variables declaration//GEN-END:variables
}
