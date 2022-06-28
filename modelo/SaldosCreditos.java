package com.example.credimovil.modelo;

public class SaldosCreditos {

    private String codCli;
    private String nomCli;
    private String codCre;
    private String saldoTot;
    private String moraTot;
    private String vencidoTot;
    private String fechaIni;
    private String fechaVence;
    private String nomDia;
    private String concurrencia;
    private String cuota;

    public SaldosCreditos(String codCli, String nomCli, String codCre, String saldoTot, String moraTot,
                          String vencidoTot, String fechaIni, String fechaVence, String nomDia,
                          String concurrencia, String cuota) {
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

    public String getSaldoTot() {
        return saldoTot;
    }

    public String getMoraTot() {
        return moraTot;
    }

    public String getVencidoTot() {
        return vencidoTot;
    }

    public String getFechaVence() {
        return fechaVence;
    }

    public String getFechaIni() {
        return fechaIni;
    }

    public String getNomDia() {
        return nomDia;
    }

    public String getConcurrencia() {
        return concurrencia;
    }

    public String getCuota() {
        return cuota;
    }
}