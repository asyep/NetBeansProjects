package com.mycompany.tindaklanjutku.custom;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {

    private int cornerRadius = 25;
    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;

    public RoundedButton(String text) {
        super(text);
        initDefaults();
    }

    public RoundedButton() {
        super();
        initDefaults();
    }

    private void initDefaults() {
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setBorder(new EmptyBorder(5, 15, 5, 15));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Set warna default
        setBackground(new Color(70, 130, 180)); // SteelBlue
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 14));
        
        // Set warna untuk berbagai state
        hoverBackgroundColor = getBackground().brighter();
        pressedBackgroundColor = getBackground().darker();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tentukan warna berdasarkan state tombol
        Color bgColor = getBackground();
        if (getModel().isPressed()) {
            bgColor = pressedBackgroundColor != null ? pressedBackgroundColor : bgColor.darker();
        } else if (getModel().isRollover()) {
            bgColor = hoverBackgroundColor != null ? hoverBackgroundColor : bgColor.brighter();
        }

        // Gambar background rounded
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Gambar teks dengan font yang sudah dikustomisasi
        g2.setColor(getForeground());
        g2.setFont(getFont()); // Pastikan menggunakan font yang disetel
        FontMetrics metrics = g2.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(getText())) / 2;
        int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.drawString(getText(), x, y);

        g2.dispose();
    }

    // =================== GETTER & SETTER ===================
    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }

    public Color getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    public void setHoverBackgroundColor(Color hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public Color getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }

    // =================== METODE UTAMA ===================
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        // Update warna turunan secara otomatis
        if (hoverBackgroundColor == null) {
            hoverBackgroundColor = bg.brighter();
        }
        if (pressedBackgroundColor == null) {
            pressedBackgroundColor = bg.darker();
        }
    }
    
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        repaint(); // Penting: paksa ulang gambar saat font diubah
    }
}