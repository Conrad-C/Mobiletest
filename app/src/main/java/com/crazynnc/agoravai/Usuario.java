package com.crazynnc.agoravai;

public class Usuario
{
    public double CasaLAT, CasaLNG;
    public String email, nome;


    public Usuario(String email, String nome, double casaLAT, double casaLNG) {
        this.email = email;
        this.nome = nome;
        CasaLAT = casaLAT;
        CasaLNG = casaLNG;

    }
}
