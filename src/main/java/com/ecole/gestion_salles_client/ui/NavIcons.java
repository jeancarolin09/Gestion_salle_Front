package com.ecole.gestion_salles_client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

public class NavIcons implements Icon {

    public enum Type { PROF, SALLE, OCCUPATION, SEARCH, PLUS, TRASH, EDIT, CHEVRON_LEFT, CHEVRON_RIGHT, WARNING }

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
            case SEARCH -> {
                double r = size * 0.55;
                g2.draw(new Ellipse2D.Double(size * 0.05, size * 0.05, r, r));
                double cx = size * 0.05 + r * 0.85, cy = size * 0.05 + r * 0.85;
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(new Line2D.Double(cx, cy, size * 0.95, size * 0.95));
            }
            case PLUS -> {
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(new Line2D.Double(size * 0.5, size * 0.08, size * 0.5, size * 0.92));
                g2.draw(new Line2D.Double(size * 0.08, size * 0.5, size * 0.92, size * 0.5));
            }
            case TRASH -> {
                g2.draw(new RoundRectangle2D.Double(size * 0.22, size * 0.30, size * 0.56, size * 0.60, 3, 3));
                g2.drawLine((int) (size * 0.10), (int) (size * 0.22), (int) (size * 0.90), (int) (size * 0.22));
                g2.drawLine((int) (size * 0.38), (int) (size * 0.08), (int) (size * 0.62), (int) (size * 0.08));
                g2.drawLine((int) (size * 0.38), (int) (size * 0.08), (int) (size * 0.30), (int) (size * 0.22));
                g2.drawLine((int) (size * 0.62), (int) (size * 0.08), (int) (size * 0.70), (int) (size * 0.22));
                g2.drawLine((int) (size * 0.38), (int) (size * 0.42), (int) (size * 0.38), (int) (size * 0.78));
                g2.drawLine((int) (size * 0.62), (int) (size * 0.42), (int) (size * 0.62), (int) (size * 0.78));
            }
            case EDIT -> {
                g2.setStroke(new BasicStroke(size * 0.16f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
                g2.draw(new Line2D.Double(size * 0.22, size * 0.85, size * 0.68, size * 0.28));
                g2.setStroke(new BasicStroke(1.6f));
                g2.draw(new Line2D.Double(size * 0.68, size * 0.28, size * 0.85, size * 0.10));
                g2.draw(new Line2D.Double(size * 0.85, size * 0.10, size * 0.94, size * 0.20));
                g2.draw(new Line2D.Double(size * 0.94, size * 0.20, size * 0.78, size * 0.38));
            }
            case CHEVRON_LEFT -> {
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(new Line2D.Double(size * 0.62, size * 0.15, size * 0.30, size * 0.5));
                g2.draw(new Line2D.Double(size * 0.30, size * 0.5, size * 0.62, size * 0.85));
            }
            case CHEVRON_RIGHT -> {
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(new Line2D.Double(size * 0.38, size * 0.15, size * 0.70, size * 0.5));
                g2.draw(new Line2D.Double(size * 0.70, size * 0.5, size * 0.38, size * 0.85));
            }
            case WARNING -> {
                int[] xs = {(int) (size * 0.5), (int) (size * 0.06), (int) (size * 0.94)};
                int[] ys = {(int) (size * 0.08), (int) (size * 0.92), (int) (size * 0.92)};
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawPolygon(xs, ys, 3);
                g2.fillOval((int) (size * 0.46), (int) (size * 0.68), (int) (size * 0.08), (int) (size * 0.08));
                g2.draw(new Line2D.Double(size * 0.5, size * 0.38, size * 0.5, size * 0.58));
            }
        }
        g2.dispose();
    }
}