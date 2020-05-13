package com.crazynnc.agoravai;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender extends AsyncTask<Void,Void,Void> {


    private Session session;
    private String email;
    private String nome;

    public EmailSender(String email, String nome){
    this.email = email;
    this.nome = nome;

    }

    @Override
    protected Void doInBackground(Void... params){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        final String meuEmail = "jvococonrad@gmail.com";
        final String senhaEmail = "dojoDo@fogo1";

        session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.EMAIL, Config.SENHA);
            }
        });

        try { MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Config.EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Obrigado!");
            message.setText("Parabens, o seu e-mail foi confirmado "+ nome+"!" );
            Transport.send(message);

        } catch(MessagingException ex) {
            ex.printStackTrace();
        }
        return null;

    }


}



