package com.example.credimovil.modelo;

public class sucursalesModel {
    String numSuc;
    String nomSuc;

    public sucursalesModel(String numSuc, String nomSuc) {
        this.numSuc = numSuc;
        this.nomSuc = nomSuc;
    }

    public String getNumSuc() {
        return numSuc;
    }

    public String getNomSuc() {
        return nomSuc;
    }

    public String toString() {
        return nomSuc;
    }
}
