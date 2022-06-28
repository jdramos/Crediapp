package com.example.credimovil.modelo;


public class SaldosOficialesModel {

    private String codOfi;
    private String nomOfi;
    private String cantClientes;
    private String saldoTot;
    private String moraTot;
    private String vencidoTot;
    private String pagoHoy;
    private String ncHoy;
    private String ndHoy;
    private String ingresos;


    public SaldosOficialesModel(String codOfi, String nomSuc, String cantClientes, String saldoTot,
                                String moraTot, String vencidoTot, String pagoHoy, String ncHoy,
                                String ndHoy, String ingresos) {
        this.codOfi = codOfi;
        this.nomOfi = nomSuc;
        this.cantClientes = cantClientes;
        this.saldoTot = saldoTot;
        this.moraTot = moraTot;
        this.vencidoTot = vencidoTot;
        this.pagoHoy = pagoHoy;
        this.ncHoy = ncHoy;
        this.ndHoy = ndHoy;
        this.ingresos = ingresos;
    }

    public String getCodOfi() {
        return codOfi;
    }

    public String getNomOfi() {
        return nomOfi;
    }

    public String getCantClientes() {
        return cantClientes;
    }

    public String getSaldoTot() {
        return saldoTot;
    }

    public String getMoraTot() {
        return moraTot;
    }

    public String getVencidoTot() {
        return vencidoTot;
    }

    public String getPagoHoy() {
        return pagoHoy;
    }

    public String getNcHoy() {
        return ncHoy;
    }

    public String getNdHoy() {
        return ndHoy;
    }

    public String getIngresos() {
        return ingresos;
    }
}
