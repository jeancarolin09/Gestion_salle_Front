package com.ecole.gestion_salles_client.ui;

import com.ecole.gestion_salles_client.model.Salle;
import com.ecole.gestion_salles_client.service.SalleApiService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SallePanel extends JPanel {

    private final SalleApiService api = new SalleApiService();
    private final JPanel listContainer = new JPanel();

    private final JTextField txtCode = new JTextField(8);
    private final JTextField txtDesignation = new JTextField(18);
    private JPanel selectedRow = null;

    public SallePanel() {
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setBackground(Color.WHITE);

        // En-tête
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titre = new JLabel("Salles");
        titre.setFont(Theme.FONT_TITLE);
        titre.setForeground(Theme.TEXT_DARK);
        JLabel sousTitre = new JLabel("Gérez les salles de classe");
        sousTitre.setFont(Theme.FONT_SUBTITLE);
        sousTitre.setForeground(Theme.TEXT_GRAY);
        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);
        titles.add(titre);
        titles.add(sousTitre);
        header.add(titles, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Liste
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // Formulaire bas
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setOpaque(false);
        form.add(new JLabel("Code:")); form.add(txtCode);
        form.add(new JLabel("Désignation:")); form.add(txtDesignation);
        Theme.styledField(txtCode);
        Theme.styledField(txtDesignation);

        JButton btnAjouter = new JButton("Ajouter", new NavIcons(NavIcons.Type.PLUS, Color.WHITE, 13));
        JButton btnModifier = new JButton("Modifier", new NavIcons(NavIcons.Type.EDIT, new Color(30, 41, 59), 13));
        JButton btnSupprimer = new JButton("Supprimer", new NavIcons(NavIcons.Type.TRASH, new Color(220, 38, 38), 13));
        btnAjouter.setIconTextGap(8);
        btnModifier.setIconTextGap(8);
        btnSupprimer.setIconTextGap(8);
        Theme.primaryButton(btnAjouter);
        Theme.secondaryButton(btnModifier);
        Theme.dangerButton(btnSupprimer);
        form.add(btnAjouter); form.add(btnModifier); form.add(btnSupprimer);
        add(form, BorderLayout.SOUTH);

        btnAjouter.addActionListener(e -> ajouter());
        btnModifier.addActionListener(e -> modifier());
        btnSupprimer.addActionListener(e -> supprimer());

        chargerTout();
    }

    private void chargerTout() {
        try {
            afficherListe(api.getAll());
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void afficherListe(List<Salle> salles) {
        listContainer.removeAll();
        selectedRow = null;
        for (Salle s : salles) {
            listContainer.add(buildRow(s));
            listContainer.add(Box.createVerticalStrut(4));
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel buildRow(Salle s) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JLabel icone = new JLabel(new NavIcons(NavIcons.Type.SALLE, new Color(16, 150, 90), 18), SwingConstants.CENTER);
        icone.setHorizontalAlignment(SwingConstants.CENTER);
        icone.setOpaque(true);
        icone.setBackground(new Color(224, 245, 231));
        icone.setPreferredSize(new Dimension(36, 36));
        row.add(icone, BorderLayout.WEST);

        JPanel texte = new JPanel();
        texte.setOpaque(false);
        texte.setLayout(new BoxLayout(texte, BoxLayout.Y_AXIS));
        JLabel nomLabel = new JLabel(s.getDesignation());
        nomLabel.setFont(Theme.FONT_BODY_BOLD);
        JLabel infoLabel = new JLabel(s.getCodeSal());
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setFont(Theme.FONT_BODY);
        texte.add(nomLabel);
        texte.add(infoLabel);
        row.add(texte, BorderLayout.CENTER);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtCode.setText(s.getCodeSal());
                txtDesignation.setText(s.getDesignation());

                if (selectedRow != null) {
                    selectedRow.setBackground(Color.WHITE);
                }
                row.setBackground(new Color(220, 245, 230));
                selectedRow = row;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (row != selectedRow) {
                    row.setBackground(new Color(240, 250, 244));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (row != selectedRow) {
                    row.setBackground(Color.WHITE);
                }
            }
        });
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return row;
    }

    private void ajouter() {
        try {
            Salle s = new Salle();
            s.setCodeSal(txtCode.getText());
            s.setDesignation(txtDesignation.getText());
            api.create(s);
            chargerTout();
            JOptionPane.showMessageDialog(this, "Salle ajoutée avec succès.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void modifier() {
        try {
            String code = txtCode.getText();
            if (code.isEmpty()) return;
            Salle s = new Salle();
            s.setDesignation(txtDesignation.getText());
            api.update(code, s);
            chargerTout();
            JOptionPane.showMessageDialog(this, "Salle modifiée avec succès.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void supprimer() {
        try {
            String code = txtCode.getText();
            if (code.isEmpty()) return;
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Supprimer cette salle ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            api.delete(code);
            chargerTout();
            JOptionPane.showMessageDialog(this, "Salle supprimée.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            erreur(ex);
        }

    }
    public void resetPanel() {
        txtCode.setText("");
        txtDesignation.setText("");
        chargerTout();
    }

    private void erreur(Exception ex) {
        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
    }
}