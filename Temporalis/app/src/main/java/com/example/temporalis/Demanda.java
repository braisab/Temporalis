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

public class Demanda extends AppCompatActivity {
    Servizo demanda = Demandas.getInstance().demanda;
    public BBDD baseDatos;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);
        TextView textView = findViewById(R.id.txtTitulo);
        textView.setText(Demandas.getInstance().demanda.getTitulo());
        TextView textViewData = findViewById(R.id.txtData);
        textViewData.setText(Demandas.getInstance().demanda.getData());
        TextView textViewHora = findViewById(R.id.txtHora);
        textViewHora.setText(Demandas.getInstance().demanda.getHora());
        TextView textViewLugar = findViewById(R.id.txtLugar);
        textViewLugar.setText(Demandas.getInstance().demanda.getLugar());
        TextView textViewDuracion = findViewById(R.id.txtDuracion);
        int duracion = Demandas.getInstance().demanda.getTempoServizo();
        String sDuracion = duracion+"";
        textViewDuracion.setText(sDuracion);
        TextView textViewCreador = findViewById(R.id.txtCreador);
        int idCreador = Demandas.getInstance().demanda.getUsuarioCreador();
        String nomeCreador = baseDatos.getNomeUsuario(idCreador);
        textViewCreador.setText(nomeCreador);
        TextView textViewClientes = findViewById(R.id.txtClientes);
        ArrayList<Integer> idsClientes = baseDatos.getIdUsuariosClientes(Demandas.getInstance().demanda.getIdServizo());
        for(int idCliente : idsClientes){
            textViewClientes.append(baseDatos.getNomeUsuario(idCliente)+",");
        }
        xestionarBoton();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void xestionarBoton(){
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        Button btnDemanda = findViewById(R.id.btnServizo);
        String sIdCeador = Login.getInstance().eTextUser.getText().toString();
        int idCreador = baseDatos.getUserId(sIdCeador);
        int idUsuario = demanda.getUsuarioCreador();
        if(idUsuario== idCreador && !isDatePass()){
            btnDemanda.setText("Borrar Demanda");
            btnDemanda.setBackgroundColor(getColor(R.color.red));
            btnDemanda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogBorrar();
                }
            });
        }
        if(idUsuario == idCreador && isDatePass()){
            btnDemanda.setText("Pagar");
            btnDemanda.setTextColor(R.color.black);
            btnDemanda.setBackgroundColor(R.color.yellow);
            btnDemanda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pagar();
                }
            });
        }
        int idServizo = demanda.getIdServizo();
        boolean existeEmpSer = baseDatos.checkEmpregaServizo(idCreador, idServizo);
        if(idUsuario != idCreador && !existeEmpSer){
            btnDemanda.setText("Estou interesada/o");
            btnDemanda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogAceptar();
                }
            });
        }
        if(idUsuario != idCreador && existeEmpSer){
            btnDemanda.setText("Cancelar");
            btnDemanda.setBackgroundColor(getColor(R.color.red));
            btnDemanda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogCancelar();
                }
            });
        }
    }

    public boolean isDatePass(){
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        boolean isDatePass= false;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String sToday = dateFormat.format(Calendar.getInstance().getTime());
            Date today = dateFormat.parse(sToday);
            String data = demanda.getData();
            String hora = demanda.getHora();
            int duracionServizo = demanda.getTempoServizo();
            Date dataServizo = dateFormat.parse(data+ " "+hora);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataServizo);
            cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)+duracionServizo);
            if (today.after(cal.getTime())) {
                isDatePass = true;
            }

        }catch (ParseException e){
            Log.e("Erro", "Erro no parsing da data");
        }return isDatePass;
    }

    public void lanzarDialogBorrar(){
        Intent intentOfertas = new Intent(this,Demandas.class);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Está segura/o de querer borrar esta Demanda?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        baseDatos.borrarServizo(demanda.getIdServizo());
                        Toast.makeText(Demanda.this, "Demanda borrada", Toast.LENGTH_SHORT).show();
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
        Intent intentOfertas = new Intent(this,Demandas.class);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Quere aceptar esta demanda?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int idServizo = demanda.getIdServizo();
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
        Intent intentOfertas = new Intent(this,Demandas.class);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Quere cancelar esta demanda?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int idServizo = demanda.getIdServizo();
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
        int numUsuarios = 0;
        int cantidadePago = demanda.getTempoServizo();
        ArrayList<Integer> idsClientes = baseDatos.getIdUsuariosClientes(demanda.getIdServizo());
        if(idsClientes.size()>0) {
            for (int idCliente : idsClientes) {
                int pago = 0;
                int saldoCliente = baseDatos.getSaldoHoras(idCliente);
                pago = saldoCliente + cantidadePago;
                baseDatos.updateHoras(idCliente, pago);
                numUsuarios +=1;
            }
        }
        int idUsuarioCreador = demanda.getUsuarioCreador();
        int saldoCreador = baseDatos.getSaldoHoras(idUsuarioCreador);
        int pago = saldoCreador - (cantidadePago * numUsuarios);;
        baseDatos.updateHoras(idUsuarioCreador, pago);
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
