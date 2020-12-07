package com.example.temporalis;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.core.app.ShareCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Oferta extends AppCompatActivity {
    Servizo oferta;
    public BBDD baseDatos;
    final int CODIGO = 1;
    private static Oferta myContext;
    public Oferta() {
        myContext =  this;
    }
    public static Oferta getInstance() {
        return myContext;
    }
    ArrayList<Integer> idsClientes;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);
        String intentAnterior = getIntent().getExtras().get("uniqueId").toString();
        if(intentAnterior.equals("intentOfertas")){
            oferta = (Servizo)getIntent().getSerializableExtra("intentDeOferta");
        }
        if(intentAnterior.equals("intentMeusServizos")){
            oferta = (Servizo)getIntent().getSerializableExtra("intentDeMeusServizos");
        }
        String sIdCeador = Login.getInstance().eTextUser.getText().toString();
        int idUsuarioLogeado = baseDatos.getUserId(sIdCeador);
        int idUsuario = oferta.getUsuarioCreador();
        TextView textView = findViewById(R.id.txtTitulo);
        textView.setText(oferta.getTitulo());
        TextView textViewData = findViewById(R.id.txtData);
        textViewData.setText(oferta.getData());
        TextView textViewHora = findViewById(R.id.txtHora);
        textViewHora.setText(oferta.getHora());
        TextView textViewLugar = findViewById(R.id.txtLugar);
        textViewLugar.setText(oferta.getLugar());
        TextView textViewDuracion = findViewById(R.id.txtDuracion);
        int duracion = oferta.getTempoServizo();
        String sDuracion = duracion+" horas";
        textViewDuracion.setText(sDuracion);
        TextView textViewCreador = findViewById(R.id.txtCreador);
        String nomeCreador = baseDatos.getNomeUsuario(oferta.getUsuarioCreador());
        textViewCreador.setText(nomeCreador);
        TextView textViewClientes = findViewById(R.id.txtClientes);
        idsClientes = baseDatos.getIdUsuariosClientes(oferta.getIdServizo());
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
        }else{
            textViewCreador.setTextColor(getResources().getColor(R.color.purple_500));
        }
        xestionarBoton();
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
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Oferta.getInstance());
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
                                    Oferta.this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CODIGO);
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
            //Check if package exists or not. If not then code
            //in catch block will be called
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

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case CODIGO: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    chamarTelefono();
//                } else {
//                    Toast.makeText(this, "Precisas permisos para chamar",Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//        }
//    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void xestionarBoton(){
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        Button btnOferta = findViewById(R.id.btnServizo);
        String sIdCeador = Login.getInstance().eTextUser.getText().toString();
        int idUsuarioLogeado = baseDatos.getUserId(sIdCeador);
        int idUsuario = oferta.getUsuarioCreador();
        if(idUsuario== idUsuarioLogeado){
            btnOferta.setText("Borrar Oferta");
            btnOferta.setBackgroundColor(getColor(R.color.red));
            btnOferta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogBorrar();
                }
            });
        }
        if(idUsuario == idUsuarioLogeado && isDatePass()){
            btnOferta.setVisibility(View.INVISIBLE);
        }
        int idServizo = oferta.getIdServizo();
        boolean existeEmpSer = baseDatos.checkEmpregaServizo(idUsuarioLogeado, idServizo);
        if(idUsuario != idUsuarioLogeado && !existeEmpSer){
            btnOferta.setText("Interésame");
            btnOferta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogAceptar();
                }
            });
        }
        if(idUsuario != idUsuarioLogeado && existeEmpSer && !isDatePass()){
            btnOferta.setText("Cancelar");
            btnOferta.setBackgroundColor(getColor(R.color.red));
            btnOferta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogCancelar();
                }
            });
        }
        if(idUsuario != idUsuarioLogeado && existeEmpSer && isDatePass()){
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
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        boolean isDatePass= false;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String sToday = dateFormat.format(Calendar.getInstance().getTime());
            Date today = dateFormat.parse(sToday);
            String data = oferta.getData();
            String hora = oferta.getHora();
            int duracionServizo = oferta.getTempoServizo();
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
