package com.example.credimovil.modelo;

public class SolicitudesModel {

    private String idSolicitud;
    private String codCli;
    private String nomCli;
    private String fecha;
    private String monto;
    private String telefono;
    private String aprobado;
    private String aplicado;
    private double plazo;
    private double tasa;
    private String idFrecuencia;
    private String frecuencia;
    private String tipCred;
    private String CodCred;
    private String nomSuc;
    private String solicitadopor;
    private String aprobadoSup;
    private String aprobadoGteSuc;
    private String aprobadoGteGral;
    private String aplicadopor;
    private String fechasolicitud;
    private String fechaaprobadoSup;
    private String fechaaprobadoGteSuc;
    private String fechaaprobadoGteGral;
    private String fechaaplicado;
    private double saldoActual;
    private String comentarios;
    private String consultaCrediticia;
    private String capacidadPago;
    private String coberturaGtia;
    private String recordInterno;
    private String fechaActualizacion;

    public SolicitudesModel(String codCli, String nomCli, String monto, String aprobado,
                            String aplicado) {
        this.codCli = codCli;
        this.nomCli = nomCli;
        this.monto = monto;
        this.aprobado = aprobado;
        this.aplicado = aplicado;
    }

    public SolicitudesModel(String idSolicitud, String codCli, String nomCli,
                            String fecha, String monto, String telefono,
                            String aprobado, String aplicado, double plazo,
                            double tasa, String idFrecuencia, String  frecuencia, String tipCred,
                            String codCred, String nomSuc, String solicitadopor,
                            String aprobadoSup, String aprobadoGteSuc, String aprobadoGteGral,
                            String aplicadopor, String fechasolicitud,
                            String fechaaprobadoSup, String fechaaprobadoGteSuc,
                            String fechaaprobadoGteGral, String fechaaplicado,
                            double saldoActual, String comentarios, String consultaCrediticia,
                            String capacidadPago, String coberturaGtia, String recordInterno,
                            String fechaActualizacion) {
        this.idSolicitud = idSolicitud;
        this.codCli = codCli;
        this.nomCli = nomCli;
        this.fecha = fecha;
        this.monto = monto;
        this.telefono = telefono;
        this.aprobado = aprobado;
        this.aplicado = aplicado;
        this.plazo = plazo;
        this.tasa = tasa;
        this.idFrecuencia = idFrecuencia;
        this.frecuencia = frecuencia;
        this.tipCred = tipCred;
        this.CodCred = codCred;
        this.nomSuc = nomSuc;
        this.solicitadopor = solicitadopor;
        this.aprobadoSup = aprobadoSup;
        this.aplicadopor = aplicadopor;
        this.fechasolicitud = fechasolicitud;
        this.fechaaprobadoSup = fechaaprobadoSup;
        this.fechaaplicado = fechaaplicado;
        this.aprobadoGteSuc = aprobadoGteSuc;
        this.aprobadoGteGral = aprobadoGteGral;
        this.fechaaprobadoGteSuc = fechaaprobadoGteSuc;
        this.fechaaprobadoGteGral = fechaaprobadoGteGral;
        this.saldoActual = saldoActual;
        this.comentarios = comentarios;
        this.consultaCrediticia = consultaCrediticia;
        this.capacidadPago = capacidadPago;
        this.coberturaGtia = coberturaGtia;
        this.recordInterno = recordInterno;
        this.fechaActualizacion = fechaActualizacion;
    }



    public String getCodCli() {
        return codCli;
    }

    public String getNomCli() {
        return nomCli;
    }

    public String getFecha() {
        return fecha;
    }

    public String getMonto() {
        return monto;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public String getAprobado() {

        return aprobado;
    }

    public String getAplicado() {

        return aplicado;
    }

    public double getPlazo() {
        return plazo;
    }

    public double getTasa() {
        return tasa;
    }

    public String getIdFrecuencia() {
        return idFrecuencia;
    }

    public String getFrecuencia() {

        return frecuencia;
    }

    public String getTipCred() {
        return tipCred;
    }

    public String getCodCred() {
        return CodCred;
    }

    public String getNomSuc() {
        return nomSuc;
    }

    public String getSolicitadopor() {
        return solicitadopor;
    }

    public String getAprobadoSup() {
        return aprobadoSup;
    }

    public String getAplicadopor() {
        return aplicadopor;
    }

    public String getFechasolicitud() {
        return fechasolicitud;
    }

    public String getFechaaprobadoSup() {
        return fechaaprobadoSup;
    }

    public String getFechaaplicado() {
        return fechaaplicado;
    }

    public String getAprobadoGteSuc() {
        return aprobadoGteSuc;
    }

    public String getAprobadoGteGral() {
        return aprobadoGteGral;
    }

    public String getFechaaprobadoGteSuc() {
        return fechaaprobadoGteSuc;
    }

    public String getFechaaprobadoGteGral() {
        return fechaaprobadoGteGral;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public String getComentarios() {
        return comentarios;
    }

    public String getConsultaCrediticia() {
        return consultaCrediticia;
    }

    public String getCapacidadPago() {
        return capacidadPago;
    }

    public String getCoberturaGtia() {
        return coberturaGtia;
    }

    public String getRecordInterno() {
        return recordInterno;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }
}
