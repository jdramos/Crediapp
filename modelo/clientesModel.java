package com.example.credimovil.modelo;

public class clientesModel {

    String codCli;
    String nomCli;
    String saldoActual;
    String fechaActualizacion;
    String disponibilidad;
    String valorGarantia;

    public clientesModel(String codCli, String nomCli, String saldoActual, String fechaActualizacion,
                         String disponibilidad, String valorGarantia) {
        this.codCli = codCli;
        this.nomCli = nomCli;
        this.saldoActual = saldoActual;
        this.fechaActualizacion = fechaActualizacion;
        this.disponibilidad = disponibilidad;
        this.valorGarantia = valorGarantia;
    }


    public String getNomCli() {
        return nomCli;
    }

    public String getCodCli() {
        return codCli;
    }

    public String toString() {
        return nomCli;
    }

    public void setNomCli(String nomCli) {
        this.nomCli = nomCli;
    }

    public String getSaldoActual() {
        return saldoActual;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public String getValorGarantia() {
        return valorGarantia;
    }
}
