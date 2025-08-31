/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tindaklanjutku.custom;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPasswordField extends JPasswordField {
    private int arcWidth = 20;
    private int arcHeight = 20;
    private Color borderColor = Color.GRAY;

    public RoundedPasswordField() {
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 10)); // Padding internal
    }

    public RoundedPasswordField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 10)); // Padding internal
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Menggambar background rounded
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, arcWidth, arcHeight);

        // Memanggil paintComponent super untuk menggambar konten
        super.paintComponent(g2);
        
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Menggambar border rounded
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arcWidth, arcHeight);
        
        g2.dispose();
    }

    // Metode untuk mengubah warna border
    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    // Metode untuk mengubah radius sudut
    public void setCornerRadius(int radius) {
        this.arcWidth = radius;
        this.arcHeight = radius;
        repaint();
    }
}