// Archivo: FilterItemRenderer.java
package com.app.palette;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class FilterItemRenderer extends JLabel implements ListCellRenderer<FilterItem> {

    public FilterItemRenderer() {
        setOpaque(true);
        setBorder(new EmptyBorder(2, 5, 2, 5)); // Un poco de padding se ve bien
        setHorizontalAlignment(SwingConstants.LEFT);
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends FilterItem> list,
                                                  FilterItem value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            setText(value.getName());
            if (index != -1) {
                Image preview = value.getPreviewImage();
                if (preview != null) {
                    Image scaledPreview = preview.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    setIcon(new ImageIcon(scaledPreview));
                    setIconTextGap(10); // Espacio entre ícono y texto
                }
            } else {
                setIcon(null); // No mostrar ícono en la caja principal
            }
        }
        
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}