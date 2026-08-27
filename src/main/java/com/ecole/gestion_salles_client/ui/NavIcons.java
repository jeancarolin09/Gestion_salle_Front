package com.ecole.gestion_salles_client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class NavIcons implements Icon {

    public enum Type { PROF, SALLE, OCCUPATION }

    private final Type type;
    private final Color color;
    private final int size;

    public NavIcons(Type type, Color color, int size) {
        this.type = type;
        this.color = color;
        this.size = size;
    }

    @Override
    public int getIconWidth() { return size; }

    @Override
    public int getIconHeight() { return size; }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1.8f));

        switch (type) {
            case PROF -> {
                g2.fill(new Ellipse2D.Double(size * 0.28, size * 0.05, size * 0.44, size * 0.44));
                g2.fill(new RoundRectangle2D.Double(size * 0.10, size * 0.55, size * 0.80, size * 0.42, 8, 8));
            }
            case SALLE -> {
                g2.draw(new RoundRectangle2D.Double(size * 0.15, size * 0.08, size * 0.60, size * 0.84, 4, 4));
                g2.fillOval((int) (size * 0.62), (int) (size * 0.46), (int) (size * 0.08), (int) (size * 0.08));
            }
            case OCCUPATION -> {
                g2.draw(new RoundRectangle2D.Double(size * 0.08, size * 0.18, size * 0.84, size * 0.74, 6, 6));
                g2.drawLine((int) (size * 0.08), (int) (size * 0.40), (int) (size * 0.92), (int) (size * 0.40));
                g2.drawLine((int) (size * 0.28), (int) (size * 0.05), (int) (size * 0.28), (int) (size * 0.22));
                g2.drawLine((int) (size * 0.72), (int) (size * 0.05), (int) (size * 0.72), (int) (size * 0.22));
            }
        }
        g2.dispose();
    }
}