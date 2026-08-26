package com.ecole.gestion_salles_client.ui;

import com.ecole.gestion_salles_client.model.Occuper;
import com.ecole.gestion_salles_client.model.Prof;
import com.ecole.gestion_salles_client.model.Salle;
import com.ecole.gestion_salles_client.service.OccuperApiService;
import com.ecole.gestion_salles_client.service.ProfApiService;
import com.ecole.gestion_salles_client.service.SalleApiService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class OccuperPanel extends JPanel {

    private final OccuperApiService api = new OccuperApiService();
    private final ProfApiService profApi = new ProfApiService();
    private final SalleApiService salleApi = new SalleApiService();

    private final JPanel listContainer = new JPanel();
    private final JComboBox<String> comboProf = new JComboBox<>();
    private final JComboBox<String> comboSalle = new JComboBox<>();
    private final JTextField txtDate = new JTextField(10);

    private String selectedProf, selectedSalle, selectedDate;

    public OccuperPanel() {
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setBackground(Color.WHITE);

        // En-tête
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titre = new JLabel("Occupations");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel sousTitre = new JLabel("Planning des salles occupées");
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
        form.add(new JLabel("Prof:")); form.add(comboProf);
        form.add(new JLabel("Salle:")); form.add(comboSalle);
        form.add(new JLabel("Date (AAAA-MM-JJ):")); form.add(txtDate);
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnSupprimer = new JButton("Supprimer");
        form.add(btnAjouter); form.add(btnSupprimer);
        add(form, BorderLayout.SOUTH);

        btnAjouter.addActionListener(e -> ajouter());
        btnSupprimer.addActionListener(e -> supprimer());

        chargerCombos();
        chargerTout();
    }

    private void chargerCombos() {
        try {
            comboProf.removeAllItems();
            List<Prof> profs = profApi.getAll();
            for (Prof p : profs) comboProf.addItem(p.getCodeProf());

            comboSalle.removeAllItems();
            List<Salle> salles = salleApi.getAll();
            for (Salle s : salles) comboSalle.addItem(s.getCodeSal());
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void chargerTout() {
        try {
            afficherListe(api.getAll());
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void afficherListe(List<Occuper> occupations) {
        listContainer.removeAll();
        for (Occuper o : occupations) {
            listContainer.add(buildRow(o));
            listContainer.add(Box.createVerticalStrut(4));
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel buildRow(Occuper o) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JLabel icone = new JLabel("📅", SwingConstants.CENTER);
        icone.setOpaque(true);
        icone.setBackground(new Color(255, 240, 224));
        icone.setPreferredSize(new Dimension(36, 36));
        row.add(icone, BorderLayout.WEST);

        String nomProf = o.getProf() != null ? o.getProf().getNom() : "?";
        String codeProf = o.getProf() != null ? o.getProf().getCodeProf() : "?";
        String designationSalle = o.getSalle() != null ? o.getSalle().getDesignation() : "?";
        String codeSalle = o.getSalle() != null ? o.getSalle().getCodeSal() : "?";

        JPanel texte = new JPanel();
        texte.setOpaque(false);
        texte.setLayout(new BoxLayout(texte, BoxLayout.Y_AXIS));
        JLabel ligne1 = new JLabel(nomProf + " → " + designationSalle);
        ligne1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel ligne2 = new JLabel(codeProf + " · " + codeSalle + " · " + o.getDateOcc());
        ligne2.setForeground(Color.GRAY);
        ligne2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        texte.add(ligne1);
        texte.add(ligne2);
        row.add(texte, BorderLayout.CENTER);

        row.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectedProf = codeProf;
                selectedSalle = codeSalle;
                selectedDate = o.getDateOcc().toString();
                comboProf.setSelectedItem(codeProf);
                comboSalle.setSelectedItem(codeSalle);
                txtDate.setText(selectedDate);
            }
        });
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return row;
    }

    private void ajouter() {
        try {
            String codeProf = (String) comboProf.getSelectedItem();
            String codeSal = (String) comboSalle.getSelectedItem();
            LocalDate date = LocalDate.parse(txtDate.getText());
            if (codeProf == null || codeSal == null) return;
            api.create(codeProf, codeSal, date);
            chargerTout();
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void supprimer() {
        try {
            if (selectedProf == null || selectedSalle == null || selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Sélectionne une ligne d'abord.");
                return;
            }
            api.delete(selectedProf, selectedSalle, LocalDate.parse(selectedDate));
            chargerTout();
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    private void erreur(Exception ex) {
        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
    }
}