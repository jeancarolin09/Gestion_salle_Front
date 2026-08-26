package com.ecole.gestion_salles_client.model;

import java.time.LocalDate;

public class Occuper {

    private ProfRef prof;
    private SalleRef salle;
    private LocalDate dateOcc;

    public Occuper() {
    }

    public ProfRef getProf() {
        return prof;
    }

    public void setProf(ProfRef prof) {
        this.prof = prof;
    }

    public SalleRef getSalle() {
        return salle;
    }

    public void setSalle(SalleRef salle) {
        this.salle = salle;
    }

    public LocalDate getDateOcc() {
        return dateOcc;
    }

    public void setDateOcc(LocalDate dateOcc) {
        this.dateOcc = dateOcc;
    }

    // Sous-classes légères juste pour récupérer les infos
    public static class ProfRef {
        private String codeProf;
        private String nom;
        private String prenom;
        private String grade;

        public String getCodeProf() {
            return codeProf;
        }

        public void setCodeProf(String codeProf) {
            this.codeProf = codeProf;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }
    }

    public static class SalleRef {
        private String codeSal;
        private String designation;

        public String getCodeSal() {
            return codeSal;
        }

        public void setCodeSal(String codeSal) {
            this.codeSal = codeSal;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }
    }
}