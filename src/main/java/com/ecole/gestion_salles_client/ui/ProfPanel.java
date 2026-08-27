package com.ecole.gestion_salles_client.ui;

import com.ecole.gestion_salles_client.model.Prof;
import com.ecole.gestion_salles_client.service.ProfApiService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class ProfPanel extends JPanel {

    private final ProfApiService api = new ProfApiService();
    private final JPanel listContainer = new JPanel();
    private final JTextField txtRecherche = new JTextField();

    private final JTextField txtCode = new JTextField(3);
    private final JTextField txtNom = new JTextField(12);
    private final JTextField txtPrenom = new JTextField(15);
    private final JTextField txtGrade = new JTextField(17);
    private JPanel selectedRow = null;

    private List<Prof> tousLesProfs = List.of();

    public ProfPanel() {
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titre = new JLabel("Professeurs");
        titre.setFont(Theme.FONT_TITLE);
        titre.setForeground(Theme.TEXT_DARK);
        JLabel sousTitre = new JLabel("Gérez le corps enseignant");
        sousTitre.setFont(Theme.FONT_SUBTITLE);
        sousTitre.setForeground(Theme.TEXT_GRAY);
        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);
        titles.add(titre);
        titles.add(sousTitre);
        header.add(titles, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);

        JPanel searchWrap = new JPanel(new BorderLayout());
        searchWrap.setOpaque(false);
        JLabel searchIcon = new JLabel(new NavIcons(NavIcons.Type.SEARCH, Theme.TEXT_GRAY, 15));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 8));
        searchWrap.add(searchIcon, BorderLayout.WEST);
        searchWrap.add(txtRecherche, BorderLayout.CENTER);
        searchWrap.setBackground(new Color(245, 245, 243));
        searchWrap.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        center.add(searchWrap, BorderLayout.NORTH);
        Theme.styledField(txtRecherche);

        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(null);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        form.setOpaque(false);

        Theme.styledField(txtCode);
        Theme.styledField(txtNom);
        Theme.styledField(txtPrenom);
        Theme.styledField(txtGrade);

        JLabel lblCode = new JLabel("Code:"); lblCode.setFont(Theme.FONT_LABEL);
        JLabel lblNom = new JLabel("Nom:"); lblNom.setFont(Theme.FONT_LABEL);
        JLabel lblPrenom = new JLabel("Prénom:"); lblPrenom.setFont(Theme.FONT_LABEL);
        JLabel lblGrade = new JLabel("Grade:"); lblGrade.setFont(Theme.FONT_LABEL);

        form.add(lblCode); form.add(txtCode);
        form.add(lblNom); form.add(txtNom);
        form.add(lblPrenom); form.add(txtPrenom);
        form.add(lblGrade); form.add(txtGrade);

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

        // Recherche automatique : filtre à chaque frappe (nom OU prénom OU code)
        txtRecherche.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrer(); }
            public void removeUpdate(DocumentEvent e) { filtrer(); }
            public void changedUpdate(DocumentEvent e) { filtrer(); }
        });

        btnAjouter.addActionListener(e -> ajouter());
        btnModifier.addActionListener(e -> modifier());
        btnSupprimer.addActionListener(e -> supprimer());

        chargerTout();
    }

    private void chargerTout() {
        try {
            tousLesProfs = api.getAll();
            afficherListe(tousLesProfs);
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void filtrer() {
        String q = txtRecherche.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            afficherListe(tousLesProfs);
            return;
        }
        List<Prof> filtres = tousLesProfs.stream()
                .filter(p ->
                        (p.getNom() != null && p.getNom().toLowerCase().contains(q)) ||
                                (p.getPrenom() != null && p.getPrenom().toLowerCase().contains(q)) ||
                                (p.getCodeProf() != null && p.getCodeProf().toLowerCase().contains(q)))
                .collect(Collectors.toList());
        afficherListe(filtres);
    }

    private void afficherListe(List<Prof> profs) {
        listContainer.removeAll();
        selectedRow = null;
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

        JLabel avatar = new JLabel(initiales(p), SwingConstants.CENTER);
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
        nomLabel.setFont(Theme.FONT_BODY_BOLD);
        JLabel infoLabel = new JLabel(p.getCodeProf() + " · " + safe(p.getGrade()));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setFont(Theme.FONT_BODY);
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

                if (selectedRow != null) {
                    selectedRow.setBackground(Color.WHITE);
                }
                row.setBackground(new Color(224, 231, 255));
                selectedRow = row;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (row != selectedRow) {
                    row.setBackground(new Color(243, 244, 251));
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

    private String initiales(Prof p) {
        String n = p.getNom() == null || p.getNom().isEmpty() ? "?" : p.getNom().substring(0, 1);
        String pr = p.getPrenom() == null || p.getPrenom().isEmpty() ? "" : p.getPrenom().substring(0, 1);
        return (n + pr).toUpperCase();
    }

    private String safe(String s) { return s == null ? "" : s; }

    private void ajouter() {
        try {
            Prof p = new Prof();
            p.setCodeProf(txtCode.getText());
            p.setNom(txtNom.getText());
            p.setPrenom(txtPrenom.getText());
            p.setGrade(txtGrade.getText());
            api.create(p);
            chargerTout();
            JOptionPane.showMessageDialog(this, "Professeur ajouté avec succès.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Professeur modifié avec succès.",
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
                    "Supprimer ce professeur ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            api.delete(code);
            chargerTout();
            JOptionPane.showMessageDialog(this, "Professeur supprimé.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            erreur(ex);
        }
    }
    public void resetPanel() {
        txtCode.setText("");
        txtNom.setText("");
        txtPrenom.setText("");
        txtGrade.setText("");
        txtRecherche.setText("");
        chargerTout();
    }

    private void erreur(Exception ex) {
        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}