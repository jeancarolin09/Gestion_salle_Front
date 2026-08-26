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
    private final JTextField txtDesignation = new JTextField(15);

    public SallePanel() {
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setBackground(Color.WHITE);

        // En-tête
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titre = new JLabel("Salles");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel sousTitre = new JLabel("Gérez les salles de classe");
        sousTitre.setForeground(Color.GRAY);
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
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
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

        JLabel icone = new JLabel("🚪", SwingConstants.CENTER);
        icone.setOpaque(true);
        icone.setBackground(new Color(224, 245, 231));
        icone.setPreferredSize(new Dimension(36, 36));
        row.add(icone, BorderLayout.WEST);

        JPanel texte = new JPanel();
        texte.setOpaque(false);
        texte.setLayout(new BoxLayout(texte, BoxLayout.Y_AXIS));
        JLabel nomLabel = new JLabel(s.getDesignation());
        nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel infoLabel = new JLabel(s.getCodeSal());
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        texte.add(nomLabel);
        texte.add(infoLabel);
        row.add(texte, BorderLayout.CENTER);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtCode.setText(s.getCodeSal());
                txtDesignation.setText(s.getDesignation());
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
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void supprimer() {
        try {
            String code = txtCode.getText();
            if (code.isEmpty()) return;
            api.delete(code);
            chargerTout();
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void erreur(Exception ex) {
        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
    }
}