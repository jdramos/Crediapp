package com.example.credimovil.modelo;


public class CobrosOficialesModel {

    private String codOfi;
    private String nomOfi;
    private String cantPagos;
    private String valorTot;



    public CobrosOficialesModel(String codOfi, String nomSuc, String cantClientes, String saldoTot) {
        this.codOfi = codOfi;
        this.nomOfi = nomSuc;
        this.cantPagos = cantClientes;
        this.valorTot = saldoTot;
    }

    public String getCodOfi() {
        return codOfi;
    }

    public String getNomOfi() {
        return nomOfi;
    }

    public String getCantPagos() {
        return cantPagos;
    }

    public String getValorTot() {
        return valorTot;
    }

}
