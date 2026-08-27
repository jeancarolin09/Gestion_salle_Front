package com.ecole.gestion_salles_client.ui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel content = new JPanel(cardLayout);
    private final Map<String, JButton> navButtons = new HashMap<>();

    private final ProfPanel profPanel = new ProfPanel();
    private final SallePanel sallePanel = new SallePanel();
    private final OccuperPanel occuperPanel = new OccuperPanel();

    public MainFrame() {
        setTitle("SallesApp");
        setSize(1250, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);

        content.add(profPanel, "profs");
        content.add(sallePanel, "salles");
        content.add(occuperPanel, "occupations");
        add(content, BorderLayout.CENTER);

        showCard("profs");
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(Theme.SIDEBAR_BG);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER),
                BorderFactory.createEmptyBorder(24, 16, 20, 16)));

        JLabel title = new JLabel("  SallesApp");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(Theme.TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 4, 28, 0));
        sidebar.add(title);

        sidebar.add(navButton("Professeurs", "profs", NavIcons.Type.PROF));
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(navButton("Salles", "salles", NavIcons.Type.SALLE));
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(navButton("Occupations", "occupations", NavIcons.Type.OCCUPATION));

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JButton navButton(String label, String cardName, NavIcons.Type iconType) {
        JButton btn = new JButton(label);
        btn.setIcon(new NavIcons(iconType, new Color(100, 116, 139), 17));
        btn.setIconTextGap(10);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setFont(Theme.FONT_SIDEBAR);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.putClientProperty("FlatLaf.style",
                "background: #F8FAFC; foreground: #475569; " +
                        "hoverBackground: #E2E8F0; pressedBackground: #CBD5E1; " +
                        "arc: 10; borderWidth: 0; margin: 4,12,4,12");
        btn.addActionListener(e -> showCard(cardName));
        navButtons.put(cardName, btn);
        return btn;
    }

    private void showCard(String cardName) {
        cardLayout.show(content, cardName);

        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            boolean actif = entry.getKey().equals(cardName);
            JButton b = entry.getValue();
            Color iconColor = actif ? new Color(67, 56, 202) : new Color(100, 116, 139);
            NavIcons.Type t = entry.getKey().equals("profs") ? NavIcons.Type.PROF
                    : entry.getKey().equals("salles") ? NavIcons.Type.SALLE
                    : NavIcons.Type.OCCUPATION;
            b.setIcon(new NavIcons(t, iconColor, 17));

            if (actif) {
                b.putClientProperty("FlatLaf.style",
                        "background: #E0E7FF; foreground: #4338CA; " +
                                "hoverBackground: #E0E7FF; pressedBackground: #C7D2FE; " +
                                "arc: 10; borderWidth: 0; margin: 4,12,4,12");
                b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            } else {
                b.putClientProperty("FlatLaf.style",
                        "background: #F8FAFC; foreground: #475569; " +
                                "hoverBackground: #E2E8F0; pressedBackground: #CBD5E1; " +
                                "arc: 10; borderWidth: 0; margin: 4,12,4,12");
                b.setFont(Theme.FONT_SIDEBAR);
            }
        }

        // Rafraîchit et vide les formulaires à chaque changement d'onglet
        if (cardName.equals("profs")) profPanel.resetPanel();
        else if (cardName.equals("salles")) sallePanel.resetPanel();
        else occuperPanel.refresh();
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}