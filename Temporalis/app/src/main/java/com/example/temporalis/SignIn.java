package com.example.temporalis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SignIn extends AppCompatActivity {
    BBDD baseDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        if(baseDatos == null){
            baseDatos = new BBDD(this);
            baseDatos.getWritableDatabase();
        }
        rexistro();
    }

    public void registrarUsuario(){
        final Intent loginIntent = new Intent(this, Login.class);
        int telefono;
        if(baseDatos == null){
            baseDatos = new BBDD(this);
            baseDatos.getWritableDatabase();
        }
        Usuario usuario;
        EditText eTNome = findViewById(R.id.eTextNome);
        String nome = eTNome.getText().toString();
        if(nome.equals("")){
            eTNome.setError("Campo obrigatorio");
        }
        EditText eTPasword = findViewById(R.id.eTextpassword);
        String password = eTPasword.getText().toString();
        if (password.equals("")){
            eTPasword.setError("Campo obrigatorio");
        }
        EditText eTextRepeatPass = findViewById(R.id.eTextRepeatpassword);
        String repPass = eTextRepeatPass.getText().toString();
        if (repPass.equals("")){
            eTextRepeatPass.setError("Campo obrigatorio");
        }
        EditText eTcorreo = findViewById(R.id.eTextcorreo);
        String correo = eTcorreo.getText().toString();
        EditText eTelefono = findViewById(R.id.eTextTelefono);
        String sTelefono = eTelefono.getText().toString();
        if(correo.equals("") && sTelefono.equals("")){
            eTcorreo.setError("Campo correo ou teléfono: Un de estos dous campos é obrigatorio");
            eTelefono.setError("Campo correo ou teléfono: Un de estos dous campos é obrigatorio");
            return;
        }
        if(!password.equals(repPass)){
            eTPasword.setError("O contrasinal non coincide");
            eTextRepeatPass.setError("O contrasinal non coincide");
            return;
        }
        if(sTelefono.equals("")){
            telefono = 0;
        }else {
            telefono = Integer.parseInt(sTelefono);
        }
        EditText eTperfil = findViewById(R.id.eTextPerfil);
        String perfil = eTperfil.getText().toString();
        Spinner spinner = findViewById(R.id.spinnerLocalizacion);
        String localizacion = spinner.getSelectedItem().toString();
        int id = baseDatos.getMaxUserId()+1;
        usuario = new Usuario(id,nome,password,correo,telefono,perfil, localizacion, 10);
        String nomechkd = baseDatos.checkNomeUsuario(usuario.getNomeUsuario());
        String correoChkd = baseDatos.checkCorreoUsuario(usuario.getNomeUsuario());
        String telefonoChkd = baseDatos.checkTelefonoUsuario(usuario.getNomeUsuario());
        if(!nomechkd.equals("EXISTE")){
            Toast.makeText(this, "O nome de usuario xa existe", Toast.LENGTH_LONG).show();
            return;
        }

        if(!correoChkd.equals("EXISTE") || !telefonoChkd.equals("EXISTE")){
            Toast.makeText(this, "O usuario xa existe", Toast.LENGTH_LONG).show();
            return;
        }

        baseDatos.gardarUsuario(usuario);
        Toast.makeText(SignIn.this, "Usuario rexistrado", Toast.LENGTH_LONG).show();
        startActivity(loginIntent);
    }

    public void rexistro(){
        Button btnSign = findViewById(R.id.btnSign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }
}