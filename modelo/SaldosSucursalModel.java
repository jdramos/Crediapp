package com.example.credimovil.modelo;


public class SaldosSucursalModel {

    private String numSuc;
    private String nomSuc;
    private String cantClientes;
    private String saldoTot;
    private String moraTot;
    private String vencidoTot;
    private String pagoHoy;
    private String ncHoy;
    private String ndHoy;
    private String ingresos;


    public SaldosSucursalModel(String numSuc, String nomSuc, String cantClientes, String saldoTot,
                               String moraTot, String vencidoTot, String pagoHoy, String ncHoy,
                               String ndHoy, String ingresos) {
        this.numSuc = numSuc;
        this.nomSuc = nomSuc;
        this.cantClientes = cantClientes;
        this.saldoTot = saldoTot;
        this.moraTot = moraTot;
        this.vencidoTot = vencidoTot;
        this.pagoHoy = pagoHoy;
        this.ncHoy = ncHoy;
        this.ndHoy = ndHoy;
        this.ingresos = ingresos;
    }

    public String getNumSuc() {
        return numSuc;
    }

    public String getNomSuc() {
        return nomSuc;
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
