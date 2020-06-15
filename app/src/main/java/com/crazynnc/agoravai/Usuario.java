package com.crazynnc.agoravai;

public class Usuario
{
    public double CasaLAT, CasaLNG;
    public String email;

    public Usuario(String email, double casaLAT, double casaLNG) {
        this.email = email;
        CasaLAT = casaLAT;
        CasaLNG = casaLNG;

    }
}
