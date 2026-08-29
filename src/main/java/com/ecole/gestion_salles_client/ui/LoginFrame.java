package com.ecole.gestion_salles_client.ui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

public class LoginFrame extends JFrame {

    // Identifiants de démo — à remplacer plus tard par une vraie vérification (API, etc.)
    private static final String DEMO_USER = "admin";
    private static final String DEMO_PASSWORD = "admin";

    // Palette
    private static final Color RED = new Color(0xE6, 0x00, 0x23);
    private static final Color RED_DEEP = new Color(0xAD, 0x08, 0x1B);
    private static final Color PINK_SOFT = new Color(0xFF, 0xDC, 0xE5);
    private static final Color PINK_MID = new Color(0xFF, 0xB3, 0xC7);
    private static final Color INK = new Color(0x11, 0x11, 0x11);
    private static final Color GRAY_TEXT = new Color(0x76, 0x76, 0x76);

    private static final Font FONT_HEADLINE = new Font("SansSerif", Font.BOLD, 30);
    private static final Font FONT_SUB = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font FONT_FIELD_LABEL = new Font("SansSerif", Font.BOLD, 11);
    private static final Font FONT_FIELD = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 12);

    private final JTextField champUtilisateur = pillField("Nom d'utilisateur");
    private final JPasswordField champMotDePasse = pillPasswordField("Mot de passe");
    private final JLabel lblErreur = new JLabel(" ");

    public LoginFrame() {
        setTitle("Gestion des Salles — Connexion");
        setSize(1040, 640);
        setMinimumSize(new Dimension(760, 560));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildShowcasePanel(), BorderLayout.WEST);
        add(buildFormPanel(), BorderLayout.CENTER);
    }

    // ---------- Panneau gauche : collage "pins" façon masonry ----------

    private JPanel buildShowcasePanel() {
        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, RED, getWidth(), getHeight(), RED_DEEP);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setPreferredSize(new Dimension(400, 0));
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());

        JPanel collage = buildPinCollage();
        panel.add(collage, BorderLayout.CENTER);

        JPanel textBlock = new JPanel();
        textBlock.setOpaque(false);
        textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));
        textBlock.setBorder(new EmptyBorder(0, 40, 44, 40));

        JLabel marque = new JLabel("Gestion des Salles");
        marque.setFont(new Font("SansSerif", Font.BOLD, 20));
        marque.setForeground(Color.WHITE);
        marque.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel accroche = new JLabel("<html>Organisez vos salles<br>en un coup d'œil.</html>");
        accroche.setFont(new Font("SansSerif", Font.BOLD, 25));
        accroche.setForeground(Color.WHITE);
        accroche.setAlignmentX(Component.LEFT_ALIGNMENT);
        accroche.setBorder(new EmptyBorder(10, 0, 0, 0));

        textBlock.add(marque);
        textBlock.add(accroche);

        panel.add(textBlock, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildPinCollage() {
        JPanel collage = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cols = 3;
                int gap = 12;
                int pad = 36;
                int colWidth = (getWidth() - pad * 2 - gap * (cols - 1)) / cols;
                int[] colY = new int[cols];
                for (int i = 0; i < cols; i++) colY[i] = 30;

                Random rnd = new Random(7); // motif stable entre les rendus
                Color[] tones = { Color.WHITE, PINK_SOFT, PINK_MID, new Color(255, 255, 255, 180) };

                int totalPins = 11;
                for (int i = 0; i < totalPins; i++) {
                    int col = i % cols;
                    int h = 60 + rnd.nextInt(90);
                    int x = pad + col * (colWidth + gap);
                    int y = colY[col];

                    g2.setColor(tones[rnd.nextInt(tones.length)]);
                    g2.fill(new RoundRectangle2D.Float(x, y, colWidth, h, 18, 18));

                    colY[col] = y + h + gap;
                }

                g2.dispose();
            }
        };
        collage.setOpaque(false);
        return collage;
    }

    // ---------- Panneau droit : formulaire ----------

    private JPanel buildFormPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);
        form.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));

        JLabel logo = new JLabel("●");
        logo.setFont(new Font("SansSerif", Font.BOLD, 34));
        logo.setForeground(RED);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titre = new JLabel("Bon retour");
        titre.setFont(FONT_HEADLINE);
        titre.setForeground(INK);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setBorder(new EmptyBorder(14, 0, 4, 0));

        JLabel sousTitre = new JLabel("Connectez-vous pour accéder à l'application");
        sousTitre.setFont(FONT_SUB);
        sousTitre.setForeground(GRAY_TEXT);
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        sousTitre.setBorder(new EmptyBorder(0, 0, 28, 0));

        form.add(logo);
        form.add(titre);
        form.add(sousTitre);

        form.add(fieldBlock("Nom d'utilisateur", champUtilisateur));
        form.add(Box.createVerticalStrut(14));
        form.add(fieldBlock("Mot de passe", champMotDePasse));
        form.add(Box.createVerticalStrut(8));

        lblErreur.setForeground(RED);
        lblErreur.setFont(FONT_SMALL);
        lblErreur.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblErreur);
        form.add(Box.createVerticalStrut(14));

        JButton btnConnexion = pillButton("Se connecter");
        btnConnexion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConnexion.addActionListener(e -> tenterConnexion());
        getRootPane().setDefaultButton(btnConnexion);
        form.add(btnConnexion);

        form.add(Box.createVerticalStrut(22));
        form.add(buildDivider());
        form.add(Box.createVerticalStrut(22));

        JLabel demo = new JLabel("La clé du succès réside dans la régularité");
        demo.setFont(FONT_SMALL);
        demo.setForeground(GRAY_TEXT);
        demo.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(demo);

        GridBagConstraints gbc = new GridBagConstraints();
        wrapper.add(form, gbc);
        return wrapper;
    }

    private JPanel buildDivider() {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(360, 20));

        JSeparator left = new JSeparator();
        left.setForeground(new Color(0xE5, 0xE5, 0xE5));
        JSeparator right = new JSeparator();
        right.setForeground(new Color(0xE5, 0xE5, 0xE5));

        JLabel ou = new JLabel("A p p l i c a t i o n ");
        ou.setFont(FONT_SMALL);
        ou.setForeground(GRAY_TEXT);

        row.add(left, BorderLayout.WEST);
        row.add(ou, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    private JPanel fieldBlock(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.setMaximumSize(new Dimension(360, 70));

        JLabel l = new JLabel(label.toUpperCase());
        l.setFont(FONT_FIELD_LABEL);
        l.setForeground(GRAY_TEXT);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setBorder(new EmptyBorder(0, 4, 6, 0));

        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setMaximumSize(new Dimension(360, 46));
        field.setPreferredSize(new Dimension(360, 46));

        p.add(l);
        p.add(field);
        return p;
    }

    // ---------- Composants "pilule" ----------

    private static JTextField pillField(String placeholder) {
        JTextField field = new JTextField();
        stylePillField(field, placeholder);
        return field;
    }

    private static JPasswordField pillPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        stylePillField(field, placeholder);
        return field;
    }

    private static void stylePillField(JTextField field, String placeholder) {
        field.setFont(FONT_FIELD);
        field.setForeground(INK);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.putClientProperty("FlatLaf.style",
                "arc: 999; background: #EFEFEF; focusedBackground: #E4E4E4; " +
                        "borderWidth: 0; focusWidth: 0; innerFocusWidth: 0; " +
                        "margin: 4,18,4,18");
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.putClientProperty("FlatLaf.style",
                        "arc: 999; background: #E4E4E4; borderColor: #111111; " +
                                "focusedBorderColor: #111111; borderWidth: 2; focusWidth: 0; " +
                                "innerFocusWidth: 0; margin: 4,18,4,18");
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.putClientProperty("FlatLaf.style",
                        "arc: 999; background: #EFEFEF; borderWidth: 0; focusWidth: 0; " +
                                "innerFocusWidth: 0; margin: 4,18,4,18");
            }
        });
    }

    private static JButton pillButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.WHITE);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.putClientProperty("FlatLaf.style",
                "arc: 999; background: #E60023; foreground: #FFFFFF; " +
                        "hoverBackground: #AD081B; pressedBackground: #8C0616; borderWidth: 0");
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(360, 46));
        btn.setPreferredSize(new Dimension(360, 46));
        return btn;
    }

    // ---------- Logique de connexion ----------

    private void tenterConnexion() {
        String utilisateur = champUtilisateur.getText().trim();
        String motDePasse = new String(champMotDePasse.getPassword());

        if (DEMO_USER.equals(utilisateur) && DEMO_PASSWORD.equals(motDePasse)) {
            lblErreur.setText(" ");
            dispose();
            SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
        } else {
            lblErreur.setText("Identifiant ou mot de passe incorrect.");
            champMotDePasse.setText("");
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}