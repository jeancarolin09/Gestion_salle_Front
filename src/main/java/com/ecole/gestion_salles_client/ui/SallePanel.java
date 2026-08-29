package com.ecole.gestion_salles_client.ui;

import com.ecole.gestion_salles_client.model.Salle;
import com.ecole.gestion_salles_client.service.SalleApiService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class SallePanel extends JPanel {

    private final SalleApiService api = new SalleApiService();

    private final JPanel listContainer = new JPanel();
    private final JTextField txtRecherche = new JTextField();
    private final JLabel lblCount = new JLabel();

    private final JTextField txtCode = new JTextField();
    private final JTextField txtDesignation = new JTextField();

    private final JLabel lblDetailTitre = new JLabel();
    private final JButton btnAjouter = new JButton("Ajouter", new NavIcons(NavIcons.Type.PLUS, Color.WHITE, 13));
    private final JButton btnModifier = new JButton("Enregistrer", new NavIcons(NavIcons.Type.EDIT, Color.WHITE, 13));
    private final JButton btnSupprimer = new JButton("Supprimer", new NavIcons(NavIcons.Type.TRASH, new Color(220, 38, 38), 13));
    private final JButton btnAnnuler = new JButton("Annuler la modification");

    private RoundedPanel selectedRow = null;
    private List<Salle> toutesLesSalles = List.of();

    public SallePanel() {
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
        JLabel titre = new JLabel("Salles");
        titre.setFont(Theme.FONT_TITLE);
        titre.setForeground(Theme.TEXT_DARK);
        lblCount.setFont(Theme.FONT_SUBTITLE);
        lblCount.setForeground(Theme.TEXT_GRAY);
        titles.add(titre);
        titles.add(lblCount);
        header.add(titles, BorderLayout.WEST);

        JButton btnNouveau = new JButton("Nouvelle salle", new NavIcons(NavIcons.Type.PLUS, Color.WHITE, 13));
        btnNouveau.setIconTextGap(8);
        Theme.primaryButton(btnNouveau, Theme.ACCENT_SALLE, new Color(13, 122, 73), new Color(10, 97, 58));
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

    private void afficherListe(List<Salle> salles) {
        listContainer.removeAll();
        selectedRow = null;

        if (salles.isEmpty()) {
            listContainer.add(buildEmptyState());
        } else {
            for (Salle s : salles) {
                listContainer.add(buildRow(s));
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
        JLabel msg = new JLabel("Aucune salle trouvée.");
        msg.setFont(Theme.FONT_BODY);
        msg.setForeground(Theme.TEXT_GRAY);
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(msg);
        return p;
    }

    private RoundedPanel buildRow(Salle s) {
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

        RoundedPanel icone = new RoundedPanel();
        icone.setLayout(new GridBagLayout());
        icone.setArc(999);
        icone.setCardBackground(Theme.ACCENT_SALLE_BG);
        icone.setPreferredSize(new Dimension(40, 40));
        icone.add(new JLabel(new NavIcons(NavIcons.Type.SALLE, Theme.ACCENT_SALLE, 17)));
        row.add(icone, BorderLayout.WEST);

        JPanel texte = new JPanel();
        texte.setOpaque(false);
        texte.setLayout(new BoxLayout(texte, BoxLayout.Y_AXIS));
        texte.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nomLabel = new JLabel(s.getDesignation());
        nomLabel.setFont(Theme.FONT_BODY_BOLD);
        nomLabel.setForeground(Theme.TEXT_DARK);
        nomLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel metaRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 3));
        metaRow.setOpaque(false);
        metaRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        metaRow.add(Theme.chip(s.getCodeSal(), Theme.ACCENT_SALLE_BG, Theme.ACCENT_SALLE));

        texte.add(nomLabel);
        texte.add(metaRow);
        row.add(texte, BorderLayout.CENTER);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectionner(s, row);
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

    private void selectionner(Salle s, RoundedPanel row) {
        txtCode.setText(s.getCodeSal());
        txtDesignation.setText(s.getDesignation());

        if (selectedRow != null) {
            selectedRow.setCardBackground(Color.WHITE);
            selectedRow.setCardBorderColor(Theme.BORDER);
        }
        row.setCardBackground(Theme.ACCENT_SALLE_BG);
        row.setCardBorderColor(Theme.ACCENT_SALLE);
        selectedRow = row;

        editionMode(s);
    }

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
        Theme.styledField(txtDesignation);

        card.add(fieldBlock("Code", txtCode));
        card.add(Box.createVerticalStrut(12));
        card.add(fieldBlock("Désignation", txtDesignation));
        card.add(Box.createVerticalStrut(20));

        Theme.primaryButton(btnAjouter, Theme.ACCENT_SALLE, new Color(13, 122, 73), new Color(10, 97, 58));
        Theme.primaryButton(btnModifier, Theme.ACCENT_SALLE, new Color(13, 122, 73), new Color(10, 97, 58));
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
        txtCode.setText("");
        txtDesignation.setText("");
        txtCode.setEditable(true);

        if (selectedRow != null) {
            selectedRow.setCardBackground(Color.WHITE);
            selectedRow.setCardBorderColor(Theme.BORDER);
            selectedRow = null;
        }

        lblDetailTitre.setText("Nouvelle salle");
        btnAjouter.setVisible(true);
        btnModifier.setVisible(false);
        btnSupprimer.setVisible(false);
        btnAnnuler.setVisible(false);
    }

    private void editionMode(Salle s) {
        txtCode.setEditable(false);

        lblDetailTitre.setText("Modifier " + s.getDesignation());
        btnAjouter.setVisible(false);
        btnModifier.setVisible(true);
        btnSupprimer.setVisible(true);
        btnAnnuler.setVisible(true);
    }

    // ---------- Données ----------

    private void chargerTout() {
        try {
            toutesLesSalles = api.getAll();
            afficherListe(toutesLesSalles);
            lblCount.setText(toutesLesSalles.size() + " salle(s) enregistrée(s)");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void filtrer() {
        String q = txtRecherche.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            afficherListe(toutesLesSalles);
            return;
        }
        List<Salle> filtres = toutesLesSalles.stream()
                .filter(s ->
                        (s.getDesignation() != null && s.getDesignation().toLowerCase().contains(q)) ||
                                (s.getCodeSal() != null && s.getCodeSal().toLowerCase().contains(q)))
                .collect(Collectors.toList());
        afficherListe(filtres);
    }

    private void ajouter() {
        try {
            Salle s = new Salle();
            s.setCodeSal(txtCode.getText());
            s.setDesignation(txtDesignation.getText());
            api.create(s);
            chargerTout();
            nouveauMode();
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
            nouveauMode();
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
            nouveauMode();
            JOptionPane.showMessageDialog(this, "Salle supprimée.",
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