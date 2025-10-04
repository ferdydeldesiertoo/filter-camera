/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.app.palette;

import java.awt.Image;

public class FilterItem {
    private final String name;
    private Image previewImage;
    
    public FilterItem(String name, Image previewImage) {
        this.name = name;
        this.previewImage = previewImage;
    }

    public String getName() {
        return name;
    }

    public Image getPreviewImage() {
        return previewImage;
    }
    
    public void setPreviewImage(Image previewImage) {
        this.previewImage = previewImage;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    
}
