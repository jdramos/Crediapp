package com.example.credimovil.modelo;

public class oficiales {

    String codOfi, numSuc, rolUser;

    public String getCodOfi() {
        return codOfi;
    }

    public void setCodOfi(String codOfi) {
        this.codOfi = codOfi;
    }

    public String getNumSuc() {
        return numSuc;
    }

    public void setNumSuc(String numSuc) {
        this.numSuc = numSuc;
    }

    public String getRolUser() {
        return rolUser;
    }

    public void setRolUser(String rolUser) {
        this.rolUser = rolUser;
    }

    public oficiales(String codOfi, String numSuc, String rolUser) {
        this.codOfi = codOfi;
        this.numSuc = numSuc;
        this.rolUser = rolUser;
    }
}
