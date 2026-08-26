package com.ecole.gestion_salles_client.ui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel content = new JPanel(cardLayout);

    public MainFrame() {
        setTitle("SallesApp");
        setSize(1100, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);

        content.add(new ProfPanel(), "profs");
        content.add(new SallePanel(), "salles");
        content.add(new OccuperPanel(), "occupations");
        add(content, BorderLayout.CENTER);

        cardLayout.show(content, "profs");
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(190, 0));
        sidebar.setBackground(new Color(250, 250, 248));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 14, 20, 14));

        JLabel title = new JLabel("  SallesApp");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));
        sidebar.add(title);

        sidebar.add(navButton("Professeurs", "profs"));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(navButton("Salles", "salles"));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(navButton("Occupations", "occupations"));

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JButton navButton(String label, String cardName) {
        JButton btn = new JButton(label);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.addActionListener(e -> cardLayout.show(content, cardName));
        return btn;
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}