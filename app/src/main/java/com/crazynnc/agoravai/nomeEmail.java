package com.crazynnc.agoravai;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class nomeEmail {
    private String emailU;
    private String nomeU;

    public nomeEmail(String nomeU,String emailU){
        this.nomeU = nomeU;
        this.emailU = emailU;

    }
    public String getEmailU(){
        return emailU;
    }

    public String getNomeU(){
        return nomeU;
    }


}
