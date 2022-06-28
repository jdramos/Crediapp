package com.example.credimovil.modelo;


public class CobrosSucursalModel {

    private String numSuc;
    private String nomSuc;
    private String cantCobros;
    private String cobroTotal;


    public CobrosSucursalModel(String numSuc, String nomSuc, String cantCobros, String cobroTotal) {
        this.numSuc = numSuc;
        this.nomSuc = nomSuc;
        this.cantCobros = cantCobros;
        this.cobroTotal = cobroTotal;

    }

    public String getNumSuc() {
        return numSuc;
    }

    public String getNomSuc() {
        return nomSuc;
    }

    public String getCantCobros() {
        return cantCobros;
    }

    public String getCobroTotal() {
        return cobroTotal;
    }

}
