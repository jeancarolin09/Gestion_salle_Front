package com.ecole.gestion_salles_client.ui;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public final class Theme {

    public static final Color PRIMARY = new Color(79, 70, 229);
    public static final Color TEXT_DARK = new Color(30, 41, 59);
    public static final Color TEXT_GRAY = new Color(100, 116, 139);
    public static final Color BORDER = new Color(226, 232, 240);
    public static final Color SIDEBAR_BG = new Color(248, 250, 252);
    public static final Color ACTIVE_BG = new Color(224, 231, 255);
    public static final Color ACTIVE_TEXT = new Color(67, 56, 202);

    // Accents de module — chaque section garde sa couleur d'identité
    public static final Color ACCENT_PROF = new Color(79, 70, 229);      // indigo
    public static final Color ACCENT_PROF_BG = new Color(224, 231, 255);
    public static final Color ACCENT_SALLE = new Color(16, 150, 90);     // émeraude
    public static final Color ACCENT_SALLE_BG = new Color(212, 245, 227);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SIDEBAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SECTION_LABEL = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_CHIP = new Font("Segoe UI", Font.BOLD, 11);

    private Theme() {}

    /** Bouton d'action principale (Ajouter) — indigo plein, hover plus foncé */
    public static void primaryButton(JButton btn) {
        btn.setFont(FONT_BUTTON);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.putClientProperty("FlatLaf.style",
                "background: #4F46E5; foreground: #FFFFFF; " +
                        "hoverBackground: #4338CA; pressedBackground: #3730A3; " +
                        "arc: 10; borderWidth: 0");
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Bouton d'action principale avec un accent personnalisé (ex. émeraude pour les Salles) */
    public static void primaryButton(JButton btn, Color base, Color hover, Color pressed) {
        btn.setFont(FONT_BUTTON);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.putClientProperty("FlatLaf.style",
                "background: " + toHex(base) + "; foreground: #FFFFFF; " +
                        "hoverBackground: " + toHex(hover) + "; pressedBackground: " + toHex(pressed) + "; " +
                        "arc: 10; borderWidth: 0");
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Bouton secondaire (Modifier) — gris clair, hover visible */
    public static void secondaryButton(JButton btn) {
        btn.setFont(FONT_BUTTON);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.putClientProperty("FlatLaf.style",
                "background: #F1F5F9; foreground: #1E293B; " +
                        "hoverBackground: #E2E8F0; pressedBackground: #CBD5E1; " +
                        "arc: 10; borderWidth: 0");
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Bouton destructif (Supprimer) — rouge, hover plus soutenu */
    public static void dangerButton(JButton btn) {
        btn.setFont(FONT_BUTTON);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.putClientProperty("FlatLaf.style",
                "background: #FFFFFF; foreground: #DC2626; " +
                        "hoverBackground: #FEE2E2; pressedBackground: #FECACA; " +
                        "borderColor: #FCA5A5; arc: 10");
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Bouton discret type "lien" (ex. Annuler) — sans fond, texte coloré */
    public static void linkButton(JButton btn) {
        btn.setFont(FONT_BODY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(TEXT_GRAY);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Champs de texte avec bordure qui réagit au survol/focus */
    public static void styledField(JTextComponent field) {
        field.setFont(FONT_BODY);
        field.putClientProperty("FlatLaf.style",
                "arc: 8; focusedBorderColor: #4F46E5; borderColor: #E2E8F0");
        field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(),
                BorderFactory.createEmptyBorder(5, 9, 5, 9)));
    }

    /** Champ en pilule (même langage que l'écran de connexion) pour les barres de recherche */
    public static void pillSearchField(JTextComponent field) {
        field.setFont(FONT_BODY);
        field.putClientProperty("FlatLaf.style",
                "arc: 999; background: #F5F5F3; borderWidth: 0; focusWidth: 0; " +
                        "innerFocusWidth: 0; margin: 4,4,4,4");
        field.setOpaque(false);
    }

    /** Petit badge arrondi (ex. code professeur, code salle) */
    public static JComponent chip(String text, Color bg, Color fg) {
        RoundedPanel chip = new RoundedPanel();
        chip.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        chip.setCardBackground(bg);
        chip.setArc(999);
        chip.setBorder(BorderFactory.createEmptyBorder(2, 9, 2, 9));
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_CHIP);
        lbl.setForeground(fg);
        chip.add(lbl);
        return chip;
    }

    private static String toHex(Color c) {
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }
}