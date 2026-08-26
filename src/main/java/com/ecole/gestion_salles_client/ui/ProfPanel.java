package com.ecole.gestion_salles_client.ui;

import com.ecole.gestion_salles_client.model.Prof;
import com.ecole.gestion_salles_client.service.ProfApiService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

public class ProfPanel extends JPanel {

    private final ProfApiService api = new ProfApiService();
    private final JPanel listContainer = new JPanel();
    private final JTextField txtRecherche = new JTextField();

    private final JTextField txtCode = new JTextField(8);
    private final JTextField txtNom = new JTextField(10);
    private final JTextField txtPrenom = new JTextField(10);
    private final JTextField txtGrade = new JTextField(10);

    public ProfPanel() {
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setBackground(Color.WHITE);

        // En-tête
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titre = new JLabel("Professeurs");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel sousTitre = new JLabel("Gérez le corps enseignant");
        sousTitre.setForeground(Color.GRAY);
        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);
        titles.add(titre);
        titles.add(sousTitre);
        header.add(titles, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Centre : recherche + liste + formulaire
        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);

        txtRecherche.setBorder(BorderFactory.createTitledBorder(""));
        JPanel searchWrap = new JPanel(new BorderLayout());
        searchWrap.setOpaque(false);
        JLabel loupe = new JLabel(" 🔍 ");
        searchWrap.add(loupe, BorderLayout.WEST);
        searchWrap.add(txtRecherche, BorderLayout.CENTER);
        searchWrap.setBackground(new Color(245, 245, 243));
        searchWrap.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        center.add(searchWrap, BorderLayout.NORTH);

        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(null);
        center.add(scroll, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        // Formulaire bas (ajout/modif/suppression, plus simple, plus discret)
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setOpaque(false);
        form.add(new JLabel("Code:")); form.add(txtCode);
        form.add(new JLabel("Nom:")); form.add(txtNom);
        form.add(new JLabel("Prénom:")); form.add(txtPrenom);
        form.add(new JLabel("Grade:")); form.add(txtGrade);
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        form.add(btnAjouter); form.add(btnModifier); form.add(btnSupprimer);
        add(form, BorderLayout.SOUTH);

        // Actions
        txtRecherche.addActionListener(e -> rechercher());
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

    private void rechercher() {
        try {
            String nom = txtRecherche.getText().trim();
            afficherListe(nom.isEmpty() ? api.getAll() : api.searchByNom(nom));
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void afficherListe(List<Prof> profs) {
        listContainer.removeAll();
        for (Prof p : profs) {
            listContainer.add(buildRow(p));
            listContainer.add(Box.createVerticalStrut(4));
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel buildRow(Prof p) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        String initiales = initiales(p);
        JLabel avatar = new JLabel(initiales, SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(new Color(224, 231, 255));
        avatar.setForeground(new Color(60, 80, 200));
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        avatar.setPreferredSize(new Dimension(36, 36));
        row.add(avatar, BorderLayout.WEST);

        JPanel texte = new JPanel();
        texte.setOpaque(false);
        texte.setLayout(new BoxLayout(texte, BoxLayout.Y_AXIS));
        JLabel nomLabel = new JLabel(p.getNom() + " " + safe(p.getPrenom()));
        nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel infoLabel = new JLabel(p.getCodeProf() + " · " + safe(p.getGrade()));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        texte.add(nomLabel);
        texte.add(infoLabel);
        row.add(texte, BorderLayout.CENTER);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtCode.setText(p.getCodeProf());
                txtNom.setText(p.getNom());
                txtPrenom.setText(safe(p.getPrenom()));
                txtGrade.setText(safe(p.getGrade()));
            }
        });
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return row;
    }

    private String initiales(Prof p) {
        String n = p.getNom() == null || p.getNom().isEmpty() ? "?" : p.getNom().substring(0, 1);
        String pr = p.getPrenom() == null || p.getPrenom().isEmpty() ? "" : p.getPrenom().substring(0, 1);
        return (n + pr).toUpperCase();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private void ajouter() {
        try {
            Prof p = new Prof();
            p.setCodeProf(txtCode.getText());
            p.setNom(txtNom.getText());
            p.setPrenom(txtPrenom.getText());
            p.setGrade(txtGrade.getText());
            api.create(p);
            chargerTout();
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void modifier() {
        try {
            String code = txtCode.getText();
            if (code.isEmpty()) return;
            Prof p = new Prof();
            p.setNom(txtNom.getText());
            p.setPrenom(txtPrenom.getText());
            p.setGrade(txtGrade.getText());
            api.update(code, p);
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