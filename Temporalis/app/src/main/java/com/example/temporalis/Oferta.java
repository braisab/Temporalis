package com.example.temporalis;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        if(idUsuario == idCreador && isDatePass()){
            btnOferta.setVisibility(View.INVISIBLE);
        }
        int idServizo = oferta.getIdServizo();
        boolean existeEmpSer = baseDatos.checkEmpregaServizo(idCreador, idServizo);
        if(idUsuario != idCreador && !existeEmpSer){
            btnOferta.setText("Interésame");
            btnOferta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogAceptar();
                }
            });
        }
        if(idUsuario != idCreador && existeEmpSer && !isDatePass()){
            btnOferta.setText("Cancelar");
            btnOferta.setBackgroundColor(getColor(R.color.red));
            btnOferta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogCancelar();
                }
            });
        }
        if(idUsuario != idCreador && existeEmpSer && isDatePass()){
            btnOferta.setText("Pagar");
            btnOferta.setTextColor(getColor(R.color.black));
            btnOferta.setBackgroundColor(getColor(R.color.yellow));
            btnOferta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pagar();
                }
            });
        }
    }

    public boolean isDatePass(){
        boolean isDatePass= false;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String sToday = dateFormat.format(Calendar.getInstance().getTime());
            Date today = dateFormat.parse(sToday);
            String data = oferta.getData();
            String hora = oferta.getHora();
            Date dataServizo = dateFormat.parse(data+ " "+hora);
                if (today.after(dataServizo)) {
                    isDatePass = true;
                }

        }catch (ParseException e){
            Log.e("Erro", "Erro no parsing da data");
        }return isDatePass;
    }


    public void lanzarDialogBorrar(){
        Intent intentOfertas = new Intent(this,Ofertas.class);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Oferta.getInstance());
        builder1.setMessage("Quere borrar esta oferta?");
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

    public void pagar(){
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        String sIdUsuarioCliente = Login.getInstance().eTextUser.getText().toString();
        int idUsuarioCreador = oferta.getUsuarioCreador();
        int cantidadePago = oferta.getTempoServizo();
        int saldoUsuarioCreador= baseDatos.getSaldoHoras(idUsuarioCreador);
        int totalSumaHorasCreador = saldoUsuarioCreador + cantidadePago;
        baseDatos.updateHoras(idUsuarioCreador, totalSumaHorasCreador);
        int idUsuarioCliente = baseDatos.getUserId(sIdUsuarioCliente);
        int saldoUsuarioCliente = baseDatos.getSaldoHoras(idUsuarioCliente);
        int totalRestaHorasCliente = saldoUsuarioCliente - cantidadePago;
        baseDatos.updateHoras(idUsuarioCliente, totalRestaHorasCliente);
        Toast.makeText(this, "Transacción realizada", Toast.LENGTH_SHORT).show();
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
}
