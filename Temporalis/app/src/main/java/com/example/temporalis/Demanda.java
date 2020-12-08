package com.example.temporalis;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import androidx.core.app.ActivityCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Demanda extends AppCompatActivity {
    Servizo demanda;
    public BBDD baseDatos;
    ArrayList<Integer> idsClientes;
    final int CODIGO = 1;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);
        String intentAnterior = getIntent().getExtras().get("uniqueId").toString();
        if(intentAnterior.equals("IntentDemandas")){
            demanda = (Servizo)getIntent().getSerializableExtra("intentDeDemanda");
        }
        if(intentAnterior.equals("intentDemandasMeusServizos")){
            demanda = (Servizo)getIntent().getSerializableExtra("intentDeDemandaMS");
        }
        if(intentAnterior.equals("intentDeOutrasDemandas")){
            demanda = (Servizo)getIntent().getSerializableExtra("intentOutrasDemandas");
        }
        String sIdCeador = Login.getInstance().eTextUser.getText().toString();
        int idUsuarioLogeado = baseDatos.getUserId(sIdCeador);
        int idUsuario = demanda.getUsuarioCreador();
        int idServizo = demanda.getIdServizo();
        boolean existeEmpSer = baseDatos.checkEmpregaServizo(idUsuarioLogeado, idServizo);
        TextView textView = findViewById(R.id.txtTitulo);
        textView.setText(demanda.getTitulo());
        TextView textViewData = findViewById(R.id.txtData);
        textViewData.setText(demanda.getData());
        TextView textViewHora = findViewById(R.id.txtHora);
        textViewHora.setText(demanda.getHora());
        TextView textViewLugar = findViewById(R.id.txtLugar);
        textViewLugar.setText(demanda.getLugar());
        TextView textViewDuracion = findViewById(R.id.txtDuracion);
        int duracion = demanda.getTempoServizo();
        String sDuracion = duracion+"";
        textViewDuracion.setText(sDuracion);
        TextView textViewCreador = findViewById(R.id.txtCreador);
        int idCreador = demanda.getUsuarioCreador();
        String nomeCreador = baseDatos.getNomeUsuario(idCreador);
        textViewCreador.setText(nomeCreador);
        TextView textViewClientes = findViewById(R.id.txtClientes);
       idsClientes = baseDatos.getIdUsuariosClientes(demanda.getIdServizo());
        for(int idCliente : idsClientes){
            textViewClientes.append(baseDatos.getNomeUsuario(idCliente)+",");
        }
        if(idUsuarioLogeado == idUsuario) {
            textViewClientes.setTextColor(getResources().getColor(R.color.purple_500));
            textViewClientes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogoClientes();
                }
            });
        }
        if(idUsuarioLogeado != idUsuario && existeEmpSer){
            textViewCreador.setTextColor(getResources().getColor(R.color.purple_500));
            textViewCreador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogContactos(nomeCreador);
                }
            });
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
            btnDemanda.setTextColor(getResources().getColor(R.color.black));
            btnDemanda.setBackgroundColor(getResources().getColor(R.color.yellow));
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

    public void lanzarDialogoClientes() {
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        ArrayList<String> nomes = new ArrayList<>();
        for(int idCliente : idsClientes){
            nomes.add(baseDatos.getNomeUsuario(idCliente));
        }
        String [] array = new String[nomes.size()];
        array = nomes.toArray(array);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] finalArray = array;
        builder.setTitle("Clientes")
                .setItems(array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String nome = finalArray[which];
                        lanzarDialogContactos(nome);
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    public void lanzarDialogContactos(String nomeUsuario){
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        Intent correoIntent = new Intent(this,MandarCorreo.class);
        Usuario usuario = baseDatos.getUsuario(nomeUsuario);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Como quere contactar?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Chamar teléfono",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            int permiso = checkSelfPermission(Manifest.permission.CALL_PHONE);
                            if (permiso == PackageManager.PERMISSION_GRANTED) {
                                chamarTelefono(usuario.getTelefono());
                            } else {
                                Demanda.this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CODIGO);
                            }
                        } else {
                            chamarTelefono(usuario.getTelefono());
                        }
                    }
                });

        builder1.setNegativeButton(
                "Enviar Mensaxe",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onClickWhatsApp();
                    }
                });
        builder1.setNeutralButton(
                "Enviar Correo",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        correoIntent.putExtra("correoUsuario", usuario.getCorreoe());
                        startActivity(correoIntent);
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void onClickWhatsApp() {

        PackageManager pm = getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "YOUR TEXT HERE";

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    private void chamarTelefono(int telefono) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telefono));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
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
