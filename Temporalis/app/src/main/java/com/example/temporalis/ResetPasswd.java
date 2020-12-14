package com.example.temporalis;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswd extends AppCompatActivity {

    BBDD baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_reset_psswd);
        sendMail();
    }

    public void sendMail(){
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        Button btnSendContrasinal = findViewById(R.id.btnResetPass);
        btnSendContrasinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = "temporaliscompostela@gmail.com";
                String passwd = "m8ECstTEyZ2dqvF";
                EditText eTextCorreo = findViewById(R.id.eTextResetPass);
                String correo = eTextCorreo.getText().toString();
                String nomeUsuario = baseDatos.getNomeUsuarioFromMail(correo);
                String pass = baseDatos.getContrasinalFromMail(correo);
                if(!nomeUsuario.equals("")||!pass.equals("")) {
                    new MandarCorreo.MailJob(user, passwd).execute(
                            new MandarCorreo.MailJob.Mail(user, correo, "Temporalis: Usuario e contrasinal", "\nNome de Usuario: " + nomeUsuario + "\nContrasinal: " + pass)
                    );
                }else{
                    Toast.makeText(ResetPasswd.this, "O correo-e non existe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
