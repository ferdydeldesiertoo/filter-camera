/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.app;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JSpinner;

/**
 *
 * @author ferdy
 */
public class PanelCustomEffects extends javax.swing.JPanel {

    /**
     * Creates new form PanelCustomEffects
     */
    public PanelCustomEffects() {
        initComponents();
    }

    public JButton getButtonReset() {
        return buttonReset;
    }

    public JCheckBox getCheckBoxInvert() {
        return checkBoxInvert;
    }

    public JSpinner getSpinnerBlur() {
        return spinnerBlur;
    }

    public JSlider getSliderBrightness() {
        return sliderBrightness;
    }

    public JSlider getSliderContrast() {
        return sliderContrast;
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        sliderBrightness = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        sliderContrast = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        spinnerBlur = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        checkBoxInvert = new javax.swing.JCheckBox();
        buttonReset = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(100, 3000));
        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel6.setLayout(new java.awt.GridLayout(0, 1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Brillo:");
        jPanel6.add(jLabel1);

        sliderBrightness.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        sliderBrightness.setMinimum(-100);
        sliderBrightness.setValue(0);
        jPanel6.add(sliderBrightness);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Contraste:");
        jPanel6.add(jLabel2);

        sliderContrast.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        sliderContrast.setMaximum(200);
        sliderContrast.setValue(100);
        jPanel6.add(sliderContrast);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Desenfoque:");
        jPanel6.add(jLabel3);

        spinnerBlur.setModel(new javax.swing.SpinnerNumberModel(1, 1, 71, 2));
        spinnerBlur.setPreferredSize(new java.awt.Dimension(50, 22));
        jPanel6.add(spinnerBlur);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Invertir colores:");
        jPanel6.add(jLabel4);

        checkBoxInvert.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        checkBoxInvert.setText("Invertir colores");
        checkBoxInvert.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel6.add(checkBoxInvert);

        buttonReset.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        buttonReset.setText("Restablecer");
        jPanel6.add(buttonReset);

        jScrollPane1.setViewportView(jPanel6);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonReset;
    private javax.swing.JCheckBox checkBoxInvert;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider sliderBrightness;
    private javax.swing.JSlider sliderContrast;
    private javax.swing.JSpinner spinnerBlur;
    // End of variables declaration//GEN-END:variables
}
