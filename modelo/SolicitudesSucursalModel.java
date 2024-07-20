package com.example.credimovil.modelo;


public class SolicitudesSucursalModel {

    private String numSuc;
    private String nomSuc;
    private String cantSolicitudes;
    private String solicitudTotal;


    public SolicitudesSucursalModel(String numSuc, String nomSuc, String cantSolicitudes, String solicitudTotal) {
        this.numSuc = numSuc;
        this.nomSuc = nomSuc;
        this.cantSolicitudes = cantSolicitudes;
        this.solicitudTotal = solicitudTotal;

    }

    public String getNumSuc() {
        return numSuc;
    }

    public String getNomSuc() {
        return nomSuc;
    }

    public String getCantSolicitudes() {
        return cantSolicitudes;
    }

    public String getSolicitudTotal() {
        return solicitudTotal;
    }

}
