package com.example.credimovil.modelo;


public class SaldosModel {

    private String codCli;
    private String nomCli;
    private String telefono;
    private String codCre;
    private String saldoTot;
    private String moraTot;
    private String vencidoTot;
    private String fechaIni;
    private String fechaVence;
    private String nomDia;
    private String concurrencia;
    private String cuota;
    private String pagoHoy;
    private String ncHoy;
    private String ndHoy;
    private String porcentajeSaldo;


    public SaldosModel(String codCli, String nomCli, String telefono, String codCre, String saldoTot,
                       String moraTot, String vencidoTot, String fechaIni, String fechaVence,
                       String nomDia, String concurrencia, String cuota, String pagoHoy, String ncHoy,
                       String ndHoy, String porcentajeSaldo) {
        this.codCli = codCli;
        this.nomCli = nomCli;
        this.codCre = codCre;
        this.saldoTot = saldoTot;
        this.moraTot = moraTot;
        this.vencidoTot = vencidoTot;
        this.fechaIni = fechaIni;
        this.fechaVence = fechaVence;
        this.nomDia = nomDia;
        this.concurrencia = concurrencia;
        this.cuota = cuota;
        this.telefono = telefono;
        this.pagoHoy = pagoHoy;
        this.ncHoy = ncHoy;
        this.ndHoy = ndHoy;
        this.porcentajeSaldo = porcentajeSaldo;


    }

    public String getFechaIni() {
        return fechaIni;
    }

    public String getCodCli() {
        return codCli;
    }

    public void setCodCli(String codCli) {
        this.codCli = codCli;
    }

    public String getCuota() {
        return cuota;
    }

    public void setCuota(String cuota) {
        this.cuota = cuota;
    }

    public String getNomCli() {
        return nomCli;
    }

    public void setNomCli(String nomCli) {
        this.nomCli = nomCli;
    }

    public String getCodCre() {
        return codCre;
    }

    public void setCodCre(String codCre) {
        this.codCre = codCre;
    }

    public String getSaldoTot() {
        return saldoTot;
    }

    public void setSaldoTot(String saldoTot) {
        this.saldoTot = saldoTot;
    }

    public String getMoraTot() {
        return moraTot;
    }

    public void setMoraTot(String moraTot) {
        this.moraTot = moraTot;
    }

    public String getVencidoTot() {
        return vencidoTot;
    }

    public void setVencidoTot(String vencidoTot) {
        this.vencidoTot = vencidoTot;
    }

    public String getFechaVence() {
        return fechaVence;
    }

    public void setFechaVence(String fechaVence) {
        this.fechaVence = fechaVence;
    }

    public String getNomDia() {
        return nomDia;
    }

    public void setNomDia(String nomDia) {
        this.nomDia = nomDia;
    }

    public String getConcurrencia() {
        return concurrencia;
    }

    public void setConcurrencia(String concurrencia) {
        this.concurrencia = concurrencia;
    }


    public String getTelefono() {
        return telefono;
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

    public String getPorcentajeSaldo() {
        return porcentajeSaldo;
    }
}
