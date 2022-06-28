package com.example.credimovil.modelo;

public class frecuencias {
    int dias;
    String frecuencia;


    public frecuencias(){}

    public frecuencias(int dias, String frecuencia) {
        setDias(dias);
        setFrecuencia(frecuencia);
    }



    public int getDias() {

        return dias;
    }

    public void setDias(int dias) {

        this.dias = dias;
    }

    public String toString() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }
}
