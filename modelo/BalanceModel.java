package com.example.credimovil.modelo;


public class BalanceModel {

    private String docto;
    private String fecha;
    private String monto;
    private String saldo;

    public BalanceModel(String docto, String fecha, String monto,  String saldo) {
        this.docto = docto;
        this.fecha = fecha;
        this.monto = monto;
        this.saldo = saldo;
    }

    public String getDocto() {
        return docto;
    }

    public String getFecha() {
        return fecha;
    }

    public String getMonto() {
        return monto;
    }

    public String getSaldo() {
        return saldo;
    }
}
