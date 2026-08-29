package com.ecole.gestion_salles_client.ui;

import com.ecole.gestion_salles_client.model.Prof;
import com.ecole.gestion_salles_client.service.ProfApiService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private final JLabel lblCount = new JLabel();

    private final JTextField txtCode = new JTextField();
    private final JTextField txtNom = new JTextField();
    private final JTextField txtPrenom = new JTextField();
    private final JTextField txtGrade = new JTextField();

    private final JLabel lblDetailTitre = new JLabel();
    private final JButton btnAjouter = new JButton("Ajouter", new NavIcons(NavIcons.Type.PLUS, Color.WHITE, 13));
    private final JButton btnModifier = new JButton("Enregistrer", new NavIcons(NavIcons.Type.EDIT, Color.WHITE, 13));
    private final JButton btnSupprimer = new JButton("Supprimer", new NavIcons(NavIcons.Type.TRASH, new Color(220, 38, 38), 13));
    private final JButton btnAnnuler = new JButton("Annuler la modification");

    private RoundedPanel selectedRow = null;
    private boolean modeEdition = false;
    private List<Prof> tousLesProfs = List.of();

    public ProfPanel() {
        setLayout(new BorderLayout(0, 18));
        setBorder(new EmptyBorder(24, 24, 24, 24));
        setBackground(Color.WHITE);

        add(buildHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(20, 0));
        body.setOpaque(false);
        body.add(buildListSection(), BorderLayout.CENTER);
        body.add(buildDetailCard(), BorderLayout.EAST);
        add(body, BorderLayout.CENTER);

        txtRecherche.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrer(); }
            public void removeUpdate(DocumentEvent e) { filtrer(); }
            public void changedUpdate(DocumentEvent e) { filtrer(); }
        });

        btnAjouter.addActionListener(e -> ajouter());
        btnModifier.addActionListener(e -> modifier());
        btnSupprimer.addActionListener(e -> supprimer());
        btnAnnuler.addActionListener(e -> nouveauMode());

        nouveauMode();
        chargerTout();
    }

    // ---------- En-tête ----------

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);
        JLabel titre = new JLabel("Professeurs");
        titre.setFont(Theme.FONT_TITLE);
        titre.setForeground(Theme.TEXT_DARK);
        lblCount.setFont(Theme.FONT_SUBTITLE);
        lblCount.setForeground(Theme.TEXT_GRAY);
        titles.add(titre);
        titles.add(lblCount);
        header.add(titles, BorderLayout.WEST);

        JButton btnNouveau = new JButton("Nouveau professeur", new NavIcons(NavIcons.Type.PLUS, Color.WHITE, 13));
        btnNouveau.setIconTextGap(8);
        Theme.primaryButton(btnNouveau);
        btnNouveau.addActionListener(e -> nouveauMode());
        header.add(btnNouveau, BorderLayout.EAST);

        return header;
    }

    // ---------- Colonne liste ----------

    private JPanel buildListSection() {
        JPanel section = new JPanel(new BorderLayout(0, 12));
        section.setOpaque(false);

        RoundedPanel searchWrap = new RoundedPanel();
        searchWrap.setLayout(new BorderLayout());
        searchWrap.setCardBackground(new Color(245, 245, 243));
        searchWrap.setArc(999);
        searchWrap.setBorder(new EmptyBorder(8, 14, 8, 14));
        JLabel searchIcon = new JLabel(new NavIcons(NavIcons.Type.SEARCH, Theme.TEXT_GRAY, 14));
        searchIcon.setBorder(new EmptyBorder(0, 0, 0, 8));
        searchWrap.add(searchIcon, BorderLayout.WEST);
        searchWrap.add(txtRecherche, BorderLayout.CENTER);
        Theme.pillSearchField(txtRecherche);
        section.add(searchWrap, BorderLayout.NORTH);

        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        section.add(scroll, BorderLayout.CENTER);

        return section;
    }

    private void afficherListe(List<Prof> profs) {
        listContainer.removeAll();
        selectedRow = null;

        if (profs.isEmpty()) {
            listContainer.add(buildEmptyState());
        } else {
            for (Prof p : profs) {
                listContainer.add(buildRow(p));
                listContainer.add(Box.createVerticalStrut(8));
            }
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel buildEmptyState() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(40, 10, 10, 10));
        JLabel msg = new JLabel("Aucun professeur trouvé.");
        msg.setFont(Theme.FONT_BODY);
        msg.setForeground(Theme.TEXT_GRAY);
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(msg);
        return p;
    }

    private RoundedPanel buildRow(Prof p) {
        RoundedPanel row = new RoundedPanel();
        row.setLayout(new BorderLayout(12, 0));
        row.setArc(14);
        row.setCardBackground(Color.WHITE);
        row.setCardBorderColor(Theme.BORDER);
        row.setBorder(new EmptyBorder(10, 12, 10, 12));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        row.setPreferredSize(new Dimension(10, 64));
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        RoundedPanel avatar = new RoundedPanel();
        avatar.setLayout(new GridBagLayout());
        avatar.setArc(999);
        avatar.setCardBackground(Theme.ACCENT_PROF_BG);
        avatar.setPreferredSize(new Dimension(40, 40));
        JLabel avatarLbl = new JLabel(initiales(p));
        avatarLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        avatarLbl.setForeground(Theme.ACCENT_PROF);
        avatar.add(avatarLbl);
        row.add(avatar, BorderLayout.WEST);

        JPanel texte = new JPanel();
        texte.setOpaque(false);
        texte.setLayout(new BoxLayout(texte, BoxLayout.Y_AXIS));
        texte.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nomLabel = new JLabel(p.getNom() + " " + safe(p.getPrenom()));
        nomLabel.setFont(Theme.FONT_BODY_BOLD);
        nomLabel.setForeground(Theme.TEXT_DARK);
        nomLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel metaRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 3));
        metaRow.setOpaque(false);
        metaRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        metaRow.add(Theme.chip(p.getCodeProf(), Theme.ACCENT_PROF_BG, Theme.ACCENT_PROF));
        if (!safe(p.getGrade()).isEmpty()) {
            JLabel gradeLbl = new JLabel(p.getGrade());
            gradeLbl.setFont(Theme.FONT_BODY);
            gradeLbl.setForeground(Theme.TEXT_GRAY);
            metaRow.add(gradeLbl);
        }

        texte.add(nomLabel);
        texte.add(metaRow);
        row.add(texte, BorderLayout.CENTER);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectionner(p, row);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (row != selectedRow) row.setCardBackground(new Color(249, 250, 251));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (row != selectedRow) row.setCardBackground(Color.WHITE);
            }
        });

        return row;
    }

    private void selectionner(Prof p, RoundedPanel row) {
        txtCode.setText(p.getCodeProf());
        txtNom.setText(p.getNom());
        txtPrenom.setText(safe(p.getPrenom()));
        txtGrade.setText(safe(p.getGrade()));

        if (selectedRow != null) {
            selectedRow.setCardBackground(Color.WHITE);
            selectedRow.setCardBorderColor(Theme.BORDER);
        }
        row.setCardBackground(Theme.ACCENT_PROF_BG);
        row.setCardBorderColor(Theme.ACCENT_PROF);
        selectedRow = row;

        editionMode(p);
    }

    private String initiales(Prof p) {
        String n = p.getNom() == null || p.getNom().isEmpty() ? "?" : p.getNom().substring(0, 1);
        String pr = p.getPrenom() == null || p.getPrenom().isEmpty() ? "" : p.getPrenom().substring(0, 1);
        return (n + pr).toUpperCase();
    }

    private String safe(String s) { return s == null ? "" : s; }

    // ---------- Carte de détail (formulaire) ----------

    private JPanel buildDetailCard() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(320, 0));

        RoundedPanel card = new RoundedPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setArc(16);
        card.setCardBackground(new Color(250, 250, 252));
        card.setCardBorderColor(Theme.BORDER);
        card.setBorder(new EmptyBorder(22, 20, 20, 20));

        lblDetailTitre.setFont(Theme.FONT_BODY_BOLD);
        lblDetailTitre.setForeground(Theme.TEXT_DARK);
        lblDetailTitre.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblDetailTitre);
        card.add(Box.createVerticalStrut(18));

        Theme.styledField(txtCode);
        Theme.styledField(txtNom);
        Theme.styledField(txtPrenom);
        Theme.styledField(txtGrade);

        card.add(fieldBlock("Code", txtCode));
        card.add(Box.createVerticalStrut(12));
        card.add(fieldBlock("Nom", txtNom));
        card.add(Box.createVerticalStrut(12));
        card.add(fieldBlock("Prénom", txtPrenom));
        card.add(Box.createVerticalStrut(12));
        card.add(fieldBlock("Grade", txtGrade));
        card.add(Box.createVerticalStrut(20));

        Theme.primaryButton(btnAjouter);
        Theme.primaryButton(btnModifier);
        Theme.dangerButton(btnSupprimer);
        Theme.linkButton(btnAnnuler);
        btnAjouter.setIconTextGap(8);
        btnModifier.setIconTextGap(8);
        btnSupprimer.setIconTextGap(8);

        btnAjouter.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAjouter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        btnModifier.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnModifier.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        btnSupprimer.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSupprimer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        btnAnnuler.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAnnuler.setFont(Theme.FONT_BODY);

        card.add(btnAjouter);
        card.add(Box.createVerticalStrut(8));
        card.add(btnModifier);
        card.add(Box.createVerticalStrut(8));
        card.add(btnSupprimer);
        card.add(Box.createVerticalStrut(10));
        card.add(btnAnnuler);

        wrapper.add(card, BorderLayout.NORTH);
        return wrapper;
    }

    private JPanel fieldBlock(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JLabel l = new JLabel(label.toUpperCase());
        l.setFont(Theme.FONT_SECTION_LABEL);
        l.setForeground(Theme.TEXT_GRAY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(0, 2, 5, 0));

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        p.add(l);
        p.add(field);
        return p;
    }

    // ---------- Modes Ajout / Édition ----------

    private void nouveauMode() {
        modeEdition = false;
        txtCode.setText("");
        txtNom.setText("");
        txtPrenom.setText("");
        txtGrade.setText("");
        txtCode.setEditable(true);

        if (selectedRow != null) {
            selectedRow.setCardBackground(Color.WHITE);
            selectedRow.setCardBorderColor(Theme.BORDER);
            selectedRow = null;
        }

        lblDetailTitre.setText("Nouveau professeur");
        btnAjouter.setVisible(true);
        btnModifier.setVisible(false);
        btnSupprimer.setVisible(false);
        btnAnnuler.setVisible(false);
    }

    private void editionMode(Prof p) {
        modeEdition = true;
        txtCode.setEditable(false);

        lblDetailTitre.setText("Modifier " + p.getNom());
        btnAjouter.setVisible(false);
        btnModifier.setVisible(true);
        btnSupprimer.setVisible(true);
        btnAnnuler.setVisible(true);
    }

    // ---------- Données ----------

    private void chargerTout() {
        try {
            tousLesProfs = api.getAll();
            afficherListe(tousLesProfs);
            lblCount.setText(tousLesProfs.size() + " professeur(s) enregistré(s)");
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

    private void ajouter() {
        try {
            Prof p = new Prof();
            p.setCodeProf(txtCode.getText());
            p.setNom(txtNom.getText());
            p.setPrenom(txtPrenom.getText());
            p.setGrade(txtGrade.getText());
            api.create(p);
            chargerTout();
            nouveauMode();
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
            nouveauMode();
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
            nouveauMode();
            JOptionPane.showMessageDialog(this, "Professeur supprimé.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    public void resetPanel() {
        txtRecherche.setText("");
        nouveauMode();
        chargerTout();
    }

    private void erreur(Exception ex) {
        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}