package com.ksm.imgtouart.gui.other;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImagePanel extends JPanel {
    private BufferedImage image;

    public JImagePanel() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }
}