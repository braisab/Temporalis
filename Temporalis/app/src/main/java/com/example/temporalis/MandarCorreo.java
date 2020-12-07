package com.example.temporalis;

import android.content.Intent;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
}
