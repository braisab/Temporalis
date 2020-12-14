package com.example.temporalis;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MandarCorreo extends AppCompatActivity {
    BBDD baseDatos;
    Button button;
    EditText  asunto, mensaje;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandar_correo);
        asunto = findViewById(R.id.caja_asunto);
        mensaje = findViewById(R.id.caja_mensaje);
        String enviarcorreo = getIntent().getExtras().get("correoUsuario").toString();
        TextView textView = findViewById(R.id.txtMailUsuario);
        textView.setText(enviarcorreo);
        button = findViewById(R.id.btn_enviar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enviarasunto = asunto.getText().toString();
                String enviarmensaje = mensaje.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { enviarcorreo });
                intent.putExtra(Intent.EXTRA_SUBJECT, enviarasunto);
                intent.putExtra(Intent.EXTRA_TEXT, enviarmensaje);
                intent.setType("message/rfc822");
                startActivity(
                        Intent
                                .createChooser(intent,
                                        "Elixe un cliente de Correo:"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent ofertasIntent = new Intent(this,Ofertas.class);
        Intent demandasIntent = new Intent(this,Demandas.class);
        Intent selfIntent = new Intent(this,MeusServizos.class);
        Intent otherServices = new Intent(this,ServizosAceptados.class);
        Intent perfilIntent = new Intent(this,Perfil.class);
        switch (item.getItemId()) {
            case R.id.action_bar_ofertas:
                startActivity(ofertasIntent);
                return true;
            case R.id.action_bar_demandas:
                startActivity(demandasIntent);
                return true;
            case R.id.action_bar_self_services:
                startActivity(selfIntent);
                return true;
            case R.id.other_services:
                startActivity(otherServices);
                return true;
            case R.id.action_bar_perfil:
                startActivity(perfilIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class MailJob extends AsyncTask<MailJob.Mail,Void,Void> {
        private  String user;
        private  String pass;

        public MailJob(String user, String pass) {
            super();
            this.user = user;
            this.pass = pass;
        }

        @Override
        protected Void doInBackground(Mail... mails) {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(user, pass);
                        }
                    });
            for (Mail mail:mails) {

                try {

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(mail.from));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(mail.to));
                    message.setSubject(mail.subject);
                    message.setText(mail.content);

                    Transport.send(message);

                } catch (MessagingException e) {
                    Log.d("MailJob", e.getMessage());
                }
            }
            return null;
        }

        public static class Mail{
            private final String subject;
            private final String content;
            private final String from;
            private final String to;

            public Mail(String from, String to, String subject, String content){
                this.subject=subject;
                this.content=content;
                this.from=from;
                this.to=to;
            }
        }
    }
}
