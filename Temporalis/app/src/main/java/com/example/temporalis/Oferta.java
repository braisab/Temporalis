package com.example.temporalis;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Oferta extends AppCompatActivity {
    Servizo oferta = Ofertas.getInstance().oferta;
    public BBDD baseDatos;
    private static Oferta myContext;
    public Oferta() {
        myContext =  this;
    }
    public static Oferta getInstance() {
        return myContext;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);
        TextView textView = findViewById(R.id.txtViewOferta);
        textView.setText(Ofertas.getInstance().oferta.toString());
        xestionarBoton();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void xestionarBoton(){
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        Button btnOferta = findViewById(R.id.btnServizo);
        String sIdCeador = Login.getInstance().eTextUser.getText().toString();
        int idCreador = baseDatos.getUserId(sIdCeador);
        int idUsuario = oferta.getUsuarioCreador();
        if(idUsuario== idCreador){
            btnOferta.setText("Borrar Oferta");
            btnOferta.setBackgroundColor(getColor(R.color.red));
            btnOferta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogBorrar();
                }
            });
        }
        int idServizo = oferta.getIdServizo();
        boolean existeEmpSer = baseDatos.checkEmpregaServizo(idCreador, idServizo);
        if(idUsuario != idCreador && !existeEmpSer){
            btnOferta.setText("Estou interesada/o");
            btnOferta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogAceptar();
                }
            });
        }
        if(idUsuario != idCreador && existeEmpSer){
            btnOferta.setText("Cancelar");
            btnOferta.setBackgroundColor(getColor(R.color.red));
            btnOferta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogCancelar();
                }
            });
        }
    }

    public void lanzarDialogBorrar(){
        Intent intentOfertas = new Intent(this,Ofertas.class);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Oferta.getInstance());
        builder1.setMessage("Está segura/o de querer borrar esta oferta?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        baseDatos.borrarServizo(oferta.getIdServizo());
                        Toast.makeText(Oferta.this, "Oferta borrada", Toast.LENGTH_SHORT).show();
                        startActivity(intentOfertas);
                    }
                });

        builder1.setNegativeButton(
                "Non",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void lanzarDialogAceptar(){
        Intent intentOfertas = new Intent(this,Ofertas.class);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Oferta.getInstance());
        builder1.setMessage("Quere aceptar esta oferta?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int idServizo = oferta.getIdServizo();
                        String sIdUsuario= Login.getInstance().eTextUser.getText().toString();
                        int idUsuario = baseDatos.getUserId(sIdUsuario);
                        EmpregaServizo empregaServizo = new EmpregaServizo(idServizo,idUsuario);
                        baseDatos.gardarEmpregaServizo(empregaServizo);
                        startActivity(intentOfertas);
                    }
                });

        builder1.setNegativeButton(
                "Non",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public void lanzarDialogCancelar(){
        Intent intentOfertas = new Intent(this,Ofertas.class);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Oferta.getInstance());
        builder1.setMessage("Quere cancelar esta oferta?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int idServizo = oferta.getIdServizo();
                        String sIdUsuario= Login.getInstance().eTextUser.getText().toString();
                        int idUsuario = baseDatos.getUserId(sIdUsuario);
                        baseDatos.cancelarEmpregaServizo(idServizo,idUsuario);
                        startActivity(intentOfertas);
                    }
                });

        builder1.setNegativeButton(
                "Non",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
