package com.example.temporalis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    public BBDD baseDatos;
    private static Login myContext;
    public Login() {
        myContext =  this;
    }
    public static Login getInstance() {
        return myContext;
    }
    public EditText eTextUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if(baseDatos == null){
            baseDatos = new BBDD(this);
            baseDatos.getWritableDatabase();
        }
        appEntry();
        userSignIn();
    }

    public void appEntry(){
        final Intent entry_intent = new Intent(this,BancoTempo.class);
        Button btnEntry = findViewById(R.id.btn_entry);
        final EditText eTextPassword = findViewById(R.id.eText_password);
        final TextView error = findViewById(R.id.txtView_eror);
        eTextUser = findViewById(R.id.eText_User);
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();

        btnEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = eTextUser.getText().toString();
                final String pass = eTextPassword.getText().toString();
                if(user.equals("") || pass.equals("")){
                    error.setText("Usuario ou contrasinal non válidos");
                    eTextUser.setText("");
                    eTextPassword.setText("");
                    return;
                }
                String nomeUsuario = baseDatos.selectNomeUsuario(pass);
                String contrasinal = baseDatos.selectContrasinal(user);
                if(!nomeUsuario.equals(user) && !contrasinal.equals(pass)){
                    error.setText("Usuario ou contrasinal non válidos");
                    eTextUser.setText("");
                    eTextPassword.setText("");
                    return;
                }
                startActivity(entry_intent);
            }
        });
    }

    public void userSignIn(){
        final Intent signIntent = new Intent(this,SignIn.class);
        TextView txtViewSign = findViewById(R.id.txtView_register);
        txtViewSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(signIntent);
            }
        });
    }
    public void onStart() {
        super.onStart();
        if (baseDatos == null) {
            baseDatos = new BBDD(this);
            baseDatos.sqLite = baseDatos.getWritableDatabase();
        }
        EditText eTextUser = findViewById(R.id.eText_User);
        eTextUser.setText("");
        EditText eTextpass = findViewById(R.id.eText_password);
        eTextpass.setText("");
    }
}