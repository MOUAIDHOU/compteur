package com.example.compteur.machine;

public class Machine {
    public long nbpiecetotale;
    public long nbpiecetravaille;
    private Long id;
    private String reference;

    public Machine( String reference, String type, String marque) {

        this.reference = reference;
        this.type = type;
        this.marque = marque;
    }

    private String type;
    private String marque;

    public long getNbpiecetotale() {
        return nbpiecetotale;
    }

    public void setNbpiecetotale(long nbpiecetotale) {
        this.nbpiecetotale = nbpiecetotale;
    }

    public long getNbpiecetravaille() {
        return nbpiecetravaille;
    }

    public void setNbpiecetravaille(long nbpiecetravaille) {
        this.nbpiecetravaille = nbpiecetravaille;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Machine() {
        // Required for Firebase
    }

    public Machine(long nbpiecetotale, long nbpiecetravaille) {
        this.nbpiecetotale = nbpiecetotale;
        this.nbpiecetravaille = nbpiecetravaille;
    }
}

