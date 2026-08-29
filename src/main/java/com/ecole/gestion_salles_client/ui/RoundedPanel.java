package com.ecole.gestion_salles_client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Panneau à coins arrondis réutilisable — sert de "carte" (liste, détail,
 * avatar, badge) pour donner un langage visuel cohérent à toute l'application.
 */
public class RoundedPanel extends JPanel {

    private Color cardBackground = Color.WHITE;
    private Color cardBorderColor = null;
    private int arc = 14;
    private float borderWidth = 1f;

    public RoundedPanel() {
        setOpaque(false);
    }

    public void setCardBackground(Color c) {
        this.cardBackground = c;
        repaint();
    }

    public void setCardBorderColor(Color c) {
        this.cardBorderColor = c;
        repaint();
    }

    public void setArc(int arc) {
        this.arc = arc;
    }

    public void setBorderWidth(float w) {
        this.borderWidth = w;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        float bw = borderWidth;
        RoundRectangle2D shape = new RoundRectangle2D.Float(
                bw / 2f, bw / 2f, w - bw, h - bw, arc, arc);

        g2.setColor(cardBackground);
        g2.fill(shape);

        if (cardBorderColor != null) {
            g2.setColor(cardBorderColor);
            g2.setStroke(new BasicStroke(bw));
            g2.draw(shape);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}