package com.ecole.gestion_salles_client.model;

public class Salle {
    private String codeSal;
    private String designation;

    public Salle() {
    }

    public Salle(String codeSal, String designation) {
        this.codeSal = codeSal;
        this.designation = designation;
    }

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

    @Override
    public String toString() {
        return designation;
    }
}
