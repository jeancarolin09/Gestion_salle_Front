package com.ecole.gestion_salles_client.ui;

import com.ecole.gestion_salles_client.model.Occuper;
import com.ecole.gestion_salles_client.model.Prof;
import com.ecole.gestion_salles_client.model.Salle;
import com.ecole.gestion_salles_client.service.OccuperApiService;
import com.ecole.gestion_salles_client.service.ProfApiService;
import com.ecole.gestion_salles_client.service.SalleApiService;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class OccuperPanel extends JPanel {

    private final OccuperApiService api = new OccuperApiService();
    private final ProfApiService profApi = new ProfApiService();
    private final SalleApiService salleApi = new SalleApiService();

    private final JPanel gridPanel = new JPanel();
    private final JLabel lblWeekRange = new JLabel();
    private static final int SALLE_COL_WIDTH = 140;

    private LocalDate weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    private List<Salle> salles = List.of();
    private List<Occuper> occupations = List.of();

    private static final String[] PALETTE = {
            "#93C5FD", "#86EFAC", "#FDE68A", "#FCA5A5", "#C4B5FD", "#F9A8D4", "#67E8F9", "#FDBA74"
    };

    public OccuperPanel() {
        setLayout(new BorderLayout(0, 14));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setBackground(Color.WHITE);

        add(buildHeader(), BorderLayout.NORTH);

        gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));
        gridPanel.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    private JPanel buildHeader() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JLabel titre = new JLabel("Occupations");
        titre.setFont(Theme.FONT_TITLE);
        titre.setForeground(Theme.TEXT_DARK);
        JLabel sousTitre = new JLabel("Planning hebdomadaire des salles");
        sousTitre.setFont(Theme.FONT_SUBTITLE);
        sousTitre.setForeground(Theme.TEXT_GRAY);
        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);
        titles.add(titre);
        titles.add(sousTitre);
        wrapper.add(titles, BorderLayout.WEST);

        JButton btnNouvelle = new JButton("+ Nouvelle réservation");
        Theme.primaryButton(btnNouvelle);
        btnNouvelle.addActionListener(e -> ouvrirDialogAjout(null, null));
        wrapper.add(btnNouvelle, BorderLayout.EAST);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        nav.setOpaque(false);
        JButton btnPrev = new JButton("◀");
        JButton btnNext = new JButton("▶");
        JButton btnToday = new JButton("Aujourd'hui");
        Theme.secondaryButton(btnPrev);
        Theme.secondaryButton(btnNext);
        Theme.secondaryButton(btnToday);
        lblWeekRange.setFont(Theme.FONT_BODY_BOLD);
        lblWeekRange.setForeground(Theme.TEXT_DARK);

        btnPrev.addActionListener(e -> { weekStart = weekStart.minusWeeks(1); rebuildGrid(); });
        btnNext.addActionListener(e -> { weekStart = weekStart.plusWeeks(1); rebuildGrid(); });
        btnToday.addActionListener(e -> {
            weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            rebuildGrid();
        });

        nav.add(btnPrev);
        nav.add(lblWeekRange);
        nav.add(btnNext);
        nav.add(Box.createHorizontalStrut(12));
        nav.add(btnToday);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(wrapper, BorderLayout.NORTH);
        top.add(nav, BorderLayout.SOUTH);
        return top;
    }

    public void refresh() {
        try {
            salles = salleApi.getAll();
            occupations = api.getAll();
            rebuildGrid();
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void rebuildGrid() {
        gridPanel.removeAll();

        DateTimeFormatter fmtRange = DateTimeFormatter.ofPattern("dd MMM", Locale.FRENCH);
        lblWeekRange.setText("Semaine du " + weekStart.format(fmtRange) + " au "
                + weekStart.plusDays(6).format(fmtRange));

        gridPanel.add(buildHeaderRow());
        gridPanel.add(Box.createVerticalStrut(4));

        for (Salle s : salles) {
            gridPanel.add(buildSalleRow(s));
            gridPanel.add(Box.createVerticalStrut(4));
        }

        if (salles.isEmpty()) {
            JLabel vide = new JLabel("Aucune salle enregistrée.");
            vide.setForeground(Theme.TEXT_GRAY);
            gridPanel.add(vide);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel buildHeaderRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel corner = new JLabel("Salle");
        corner.setFont(Theme.FONT_LABEL);
        corner.setForeground(Theme.TEXT_GRAY);
        corner.setPreferredSize(new Dimension(SALLE_COL_WIDTH, 30));
        row.add(corner, BorderLayout.WEST);

        JPanel days = new JPanel(new GridLayout(1, 7, 4, 0));
        days.setOpaque(false);
        DateTimeFormatter fmtDay = DateTimeFormatter.ofPattern("dd/MM");
        for (int i = 0; i < 7; i++) {
            LocalDate d = weekStart.plusDays(i);
            String nomJour = capitalize(d.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRENCH));
            boolean aujourdhui = d.equals(LocalDate.now());
            JLabel lbl = new JLabel("<html><center>" + nomJour + "<br>" + d.format(fmtDay) + "</center></html>",
                    SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(aujourdhui ? new Color(67, 56, 202) : Theme.TEXT_DARK);
            days.add(lbl);
        }
        row.add(days, BorderLayout.CENTER);
        return row;
    }

    private JPanel buildSalleRow(Salle salle) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JLabel lblSalle = new JLabel("<html>" + salle.getDesignation()
                + "<br><span style='color:#94A3B8;font-size:10px'>" + salle.getCodeSal() + "</span></html>");
        lblSalle.setFont(Theme.FONT_BODY_BOLD);
        lblSalle.setPreferredSize(new Dimension(SALLE_COL_WIDTH, 60));
        row.add(lblSalle, BorderLayout.WEST);

        JPanel days = new JPanel(new GridLayout(1, 7, 4, 0));
        days.setOpaque(false);
        for (int i = 0; i < 7; i++) {
            LocalDate d = weekStart.plusDays(i);
            days.add(buildCell(salle, d));
        }
        row.add(days, BorderLayout.CENTER);
        return row;
    }

    private JPanel buildCell(Salle salle, LocalDate date) {
        List<Occuper> match = occupations.stream()
                .filter(o -> o.getSalle() != null && salle.getCodeSal().equals(o.getSalle().getCodeSal()))
                .filter(o -> date.equals(o.getDateOcc()))
                .collect(Collectors.toList());

        JPanel cell = new JPanel(new BorderLayout());
        cell.setPreferredSize(new Dimension(0, 60));
        cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (match.isEmpty()) {
            cell.setBackground(new Color(249, 250, 251));
            cell.setBorder(BorderFactory.createLineBorder(new Color(237, 238, 240), 1));
            cell.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    ouvrirDialogAjout(salle.getCodeSal(), date);
                }
            });
        } else if (match.size() == 1) {
            Occuper o = match.get(0);
            cell.setBackground(couleurPourProf(o.getProf().getCodeProf()));
            cell.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            String nomProf = o.getProf().getNom();
            JLabel lbl = new JLabel("<html><b>" + nomProf + "</b></html>");
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            cell.add(lbl, BorderLayout.CENTER);
            cell.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    afficherDetails(match);
                }
            });
        } else {
            cell.setBackground(new Color(252, 165, 165));
            cell.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            JLabel lbl = new JLabel("<html><b>⚠ Conflit</b><br>(" + match.size() + " profs)</html>");
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lbl.setForeground(new Color(127, 29, 29));
            cell.add(lbl, BorderLayout.CENTER);
            cell.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    afficherDetails(match);
                }
            });
        }
        return cell;
    }

    private Color couleurPourProf(String codeProf) {
        int idx = Math.floorMod(codeProf.hashCode(), PALETTE.length);
        return Color.decode(PALETTE[idx]);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    // ---------- Dialogue de détails ----------

    private void afficherDetails(List<Occuper> occupationsCell) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Détails de l'occupation",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(380, 120 + occupationsCell.size() * 70);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        content.setBackground(Color.WHITE);

        Occuper first = occupationsCell.get(0);
        JLabel salleLabel = new JLabel(first.getSalle().getDesignation() + " — " + first.getDateOcc());
        salleLabel.setFont(Theme.FONT_BODY_BOLD);
        content.add(salleLabel);
        content.add(Box.createVerticalStrut(10));

        for (Occuper o : occupationsCell) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(new Color(249, 250, 251));
            row.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

            JLabel infoLabel = new JLabel("<html><b>" + o.getProf().getNom() + " " + safe(o.getProf().getPrenom())
                    + "</b><br><span style='color:#94A3B8'>" + o.getProf().getCodeProf() + " · "
                    + safe(o.getProf().getGrade()) + "</span></html>");
            row.add(infoLabel, BorderLayout.CENTER);

            JButton btnSupprimer = new JButton("Supprimer");
            Theme.dangerButton(btnSupprimer);
            btnSupprimer.addActionListener(e -> {
                try {
                    int confirm = JOptionPane.showConfirmDialog(dialog,
                            "Supprimer cette occupation ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) return;
                    api.delete(o.getProf().getCodeProf(), o.getSalle().getCodeSal(), o.getDateOcc());
                    dialog.dispose();
                    refresh();
                    JOptionPane.showMessageDialog(this, "Occupation supprimée.",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    erreur(ex);
                }
            });
            row.add(btnSupprimer, BorderLayout.EAST);

            content.add(row);
            content.add(Box.createVerticalStrut(6));
        }

        dialog.add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // ---------- Dialogue d'ajout ----------

    private void ouvrirDialogAjout(String codeSalPreselect, LocalDate datePreselect) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Nouvelle réservation",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(360, 260);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setBackground(Color.WHITE);

        JComboBox<String> comboProf = new JComboBox<>();
        JComboBox<String> comboSalle = new JComboBox<>();
        try {
            for (Prof p : profApi.getAll()) comboProf.addItem(p.getCodeProf());
            for (Salle s : salleApi.getAll()) comboSalle.addItem(s.getCodeSal());
        } catch (Exception ex) {
            erreur(ex);
        }
        if (codeSalPreselect != null) comboSalle.setSelectedItem(codeSalPreselect);

        DatePickerSettings dateSettings = new DatePickerSettings();
        DatePicker datePicker = new DatePicker(dateSettings);
        dateSettings.setVetoPolicy(new DateVetoPolicy() {
            @Override
            public boolean isDateAllowed(LocalDate date) {
                return !date.isBefore(LocalDate.now());
            }
        });
        datePicker.setDate(datePreselect != null ? datePreselect : LocalDate.now());

        content.add(labeled("Professeur", comboProf));
        content.add(Box.createVerticalStrut(10));
        content.add(labeled("Salle", comboSalle));
        content.add(Box.createVerticalStrut(10));
        content.add(labeled("Date", datePicker));
        content.add(Box.createVerticalStrut(20));

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        boutons.setOpaque(false);
        JButton btnAnnuler = new JButton("Annuler");
        JButton btnAjouter = new JButton("Ajouter");
        Theme.secondaryButton(btnAnnuler);
        Theme.primaryButton(btnAjouter);
        btnAnnuler.addActionListener(e -> dialog.dispose());
        btnAjouter.addActionListener(e -> {
            try {
                String codeProf = (String) comboProf.getSelectedItem();
                String codeSal = (String) comboSalle.getSelectedItem();
                LocalDate date = datePicker.getDate();
                if (codeProf == null || codeSal == null || date == null) {
                    JOptionPane.showMessageDialog(dialog, "Remplis tous les champs.");
                    return;
                }
                api.create(codeProf, codeSal, date);
                dialog.dispose();
                refresh();
                JOptionPane.showMessageDialog(this, "Occupation ajoutée avec succès.",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                erreur(ex);
            }
        });
        boutons.add(btnAnnuler);
        boutons.add(btnAjouter);
        content.add(boutons);

        dialog.add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private JPanel labeled(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(Theme.FONT_LABEL);
        l.setForeground(Theme.TEXT_GRAY);
        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private String safe(String s) { return s == null ? "" : s; }

    private void erreur(Exception ex) {
        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}