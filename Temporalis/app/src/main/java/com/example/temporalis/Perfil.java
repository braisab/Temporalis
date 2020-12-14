package com.example.temporalis;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Perfil extends AppCompatActivity {
    BBDD baseDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        String nomeUsuario = Login.getInstance().eTextUser.getText().toString();
        Usuario usuario = baseDatos.getUsuario(nomeUsuario);
        TextView textViewSaldo = findViewById(R.id.textViewSaldo);
        int totalHoras = usuario.getTotalHoras();
        String horas = totalHoras+"";
        textViewSaldo.setText(horas);
        EditText editTextNome = findViewById(R.id.eTextNomePerfil);
        editTextNome.setText(nomeUsuario);
        EditText editTextCorreo = findViewById(R.id.eTextCorreoPerfil);
        editTextCorreo.setText(usuario.getCorreoe());
        EditText editTextTelefono = findViewById(R.id.eTextTelefonoPerfil);
        int telefono = usuario.getTelefono();
        String sTelefono = telefono+"";
        editTextTelefono.setText(sTelefono);
        EditText editTextZona = findViewById(R.id.eTextZonaPerfil);
        editTextZona.setText(usuario.getLocalizacion());
        EditText editTextPerfil = findViewById(R.id.eTextPerfil);
        editTextPerfil.setText(usuario.getPerfil());
        xestionarBoton();
    }

    public void xestionarBoton() {
        EditText editTextnome = findViewById(R.id.eTextNomePerfil);
        EditText editTextCorreo = findViewById(R.id.eTextCorreoPerfil);
        EditText editTextTelefono = findViewById(R.id.eTextTelefonoPerfil);
        EditText editTextZona = findViewById(R.id.eTextZonaPerfil);
        Button btnCancelarEdicion = findViewById(R.id.btnCancelarEdicion);
        Button btnPerfil = findViewById(R.id.btnEditarPerfi);
        EditText editTextPerfil = findViewById(R.id.eTextPerfil);
        if (!editTextnome.isEnabled()) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editTextnome.setEnabled(true);
                    editTextCorreo.setEnabled(true);
                    editTextTelefono.setEnabled(true);
                    editTextZona.setEnabled(true);
                    editTextPerfil.setEnabled(true);
                    btnCancelarEdicion.setVisibility(View.VISIBLE);
                    btnPerfil.setText("Gardar");
                    gardarPerfil();
                }
            });
        }
    }


    public void gardarPerfil(){
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        Intent recargar = new Intent(this,Perfil.class);
        String nomeUsuario = Login.getInstance().eTextUser.getText().toString();
        int idUsuario = baseDatos.getUserId(nomeUsuario);
        String sIdUsuario = idUsuario+ "";
        EditText editTextnome = findViewById(R.id.eTextNomePerfil);
        EditText editTextCorreo = findViewById(R.id.eTextCorreoPerfil);
        EditText editTextTelefono = findViewById(R.id.eTextTelefonoPerfil);
        EditText editTextZona = findViewById(R.id.eTextZonaPerfil);
        EditText editTextPerfil = findViewById(R.id.eTextPerfil);
        Button btnCancelarEdicion = findViewById(R.id.btnCancelarEdicion);
        Button btnPerfil = findViewById(R.id.btnEditarPerfi);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancelarEdicion.setVisibility(View.INVISIBLE);
                btnPerfil.setText("Editar Perfil");
                editTextnome.setEnabled(false);
                editTextCorreo.setEnabled(false);
                editTextTelefono.setEnabled(false);
                editTextZona.setEnabled(false);
                editTextPerfil.setEnabled(false);
                String nome = editTextnome.getText().toString();
                String correo = editTextCorreo.getText().toString();
                String telefono = editTextTelefono.getText().toString();
                String zona = editTextZona.getText().toString();
                String perfil = editTextPerfil.getText().toString();
                baseDatos.modificarPerfil(sIdUsuario,nome,correo,telefono,zona,perfil);
                Toast.makeText(Perfil.this, "Perfil gardado", Toast.LENGTH_SHORT).show();
                startActivity(recargar);
            }
        });
        btnCancelarEdicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancelarEdicion.setVisibility(View.INVISIBLE);
                btnPerfil.setText("Editar Perfil");
                editTextnome.setEnabled(false);
                editTextCorreo.setEnabled(false);
                editTextTelefono.setEnabled(false);
                editTextZona.setEnabled(false);
                editTextPerfil.setEnabled(false);
                startActivity(recargar);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
