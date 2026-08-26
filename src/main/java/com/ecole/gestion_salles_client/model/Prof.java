package com.ecole.gestion_salles_client.model;

public class Prof {
    private String codeProf;
    private String nom;
    private String prenom;
    private String grade;

    public Prof() {
    }

    public Prof(String codeProf, String nom, String prenom, String grade) {
        this.codeProf = codeProf;
        this.nom = nom;
        this.prenom = prenom;
        this.grade = grade;
    }

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

    @Override
    public String toString() {
        return nom + " " + prenom;
    }
}
