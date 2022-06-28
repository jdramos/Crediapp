package com.example.credimovil.modelo;

public class CobrosModel {

    private String idPago;
    private String codCli;
    private String nomCli;
    private String codCre;
    private String monto;
    private String telefono;

    public CobrosModel(String idPago, String codCli, String nomCli, String codCre, String monto, String telefono) {
        this.idPago = idPago;
        this.codCli = codCli;
        this.nomCli = nomCli;
        this.codCre = codCre;
        this.monto = monto;
        this.telefono = telefono;
    }

    public String getCodCli() {
        return codCli;
    }

    public String getNomCli() {
        return nomCli;
    }

    public String getCodCre() {
        return codCre;
    }

    public String getMonto() {
        return monto;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getIdPago() {
        return idPago;
    }
}
