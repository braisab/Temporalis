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
    String intentAnterior;
    Usuario usuario;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);
        intentAnterior = getIntent().getExtras().get("uniqueId").toString();
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
        TextView txtDescricion = findViewById(R.id.txtwDescripcion);
        txtDescricion.setText(demanda.getDescricion());
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
        Button btnEditar = findViewById(R.id.btnEditarServizo);
        String sIdCeador = Login.getInstance().eTextUser.getText().toString();
        int idCreador = baseDatos.getUserId(sIdCeador);
        int idUsuario = demanda.getUsuarioCreador();
        Intent crearDemandaIntent = new Intent(this,CrearDemanda.class);
        if(idUsuario== idCreador && !isDateArrives()){
            btnDemanda.setText("Borrar Demanda");
            btnDemanda.setBackgroundColor(getColor(R.color.red));
            btnDemanda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogBorrar();
                }
            });
            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    crearDemandaIntent.putExtra("uniqueId", "intentDemanda");
                    crearDemandaIntent.putExtra("demanda", demanda);
                    startActivity(crearDemandaIntent);
                }
            });
        }

        if(isDateArrives()){
            btnDemanda.setVisibility(View.INVISIBLE);
            btnEditar.setVisibility(View.INVISIBLE);
        }

        if(idUsuario == idCreador && isDatePass()){
            btnEditar.setVisibility(View.INVISIBLE);
            btnDemanda.setVisibility(View.VISIBLE);
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
            btnEditar.setVisibility(View.INVISIBLE);
            btnDemanda.setText("Interésame");
            btnDemanda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogAceptar();
                }
            });
        }

        int relacion = baseDatos.countEmpregaServizo(demanda.getIdServizo());
        if(idUsuario == idCreador && isDatePass() && relacion == 0){
            btnDemanda.setVisibility(View.VISIBLE);
            btnDemanda.setBackgroundColor(getResources().getColor(R.color.red));
            btnDemanda.setTextColor(getResources().getColor(R.color.white));
            btnDemanda.setText("Borrar Demanda");
            btnDemanda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarDialogBorrar();
                }
            });
        }

        if(idUsuario != idCreador && existeEmpSer && !isDateArrives()){
            btnEditar.setVisibility(View.INVISIBLE);
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
        usuario = baseDatos.getUsuario(nomeUsuario);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CODIGO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    int telefono = usuario.getTelefono();
                    chamarTelefono(telefono);
                } else {
                    Toast.makeText(this, "Precisas permisos para chamar",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public boolean isDateArrives(){
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        boolean isDatePass= false;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String sToday = dateFormat.format(Calendar.getInstance().getTime());
            Date today = dateFormat.parse(sToday);
            String data = demanda.getData();
            String hora = demanda.getHora();
            Date dataServizo = dateFormat.parse(data+ " "+hora);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataServizo);
            cal.set(Calendar.HOUR, cal.get(Calendar.HOUR));
            if (today.after(cal.getTime())) {
                isDatePass = true;
            }
        }catch (ParseException e){
            Log.e("Erro", "Erro no parsing da data");
        }return isDatePass;
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
                    String user = "temporaliscompostela@gmail.com";
                    String passwd = "m8ECstTEyZ2dqvF";
                    public void onClick(DialogInterface dialog, int id) {
                        baseDatos.borrarServizo(demanda.getIdServizo());
                        Toast.makeText(Demanda.this, "Demanda borrada", Toast.LENGTH_SHORT).show();
                        startActivity(intentOfertas);
                        String nomeCreador = baseDatos.getNomeUsuario(demanda.getUsuarioCreador());
                        ArrayList<Integer> idsClientes = baseDatos.getIdUsuariosClientes(demanda.getIdServizo());
                        if (idsClientes.size() > 0) {
                            for (int idCliente : idsClientes) {
                                String correoUsuario = baseDatos.getCorreoUsuario(idCliente);
                                new MandarCorreo.MailJob(user, passwd).execute(
                                        new MandarCorreo.MailJob.Mail(user, correoUsuario, "Temporalis: Demanda Borrada", "O usuario " + nomeCreador + " borrou a demanda " + demanda.getTitulo() + " con data " + demanda.getData() + " " + demanda.getHora())
                                );
                            }
                        }
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
                        String user = "temporaliscompostela@gmail.com";
                        String passwd = "m8ECstTEyZ2dqvF";
                        int idServizo = demanda.getIdServizo();
                        String sIdUsuario= Login.getInstance().eTextUser.getText().toString();
                        int idUsuario = baseDatos.getUserId(sIdUsuario);
                        EmpregaServizo empregaServizo = new EmpregaServizo(idServizo,idUsuario, false);
                        baseDatos.gardarEmpregaServizo(empregaServizo);
                        startActivity(intentOfertas);
                        int idUsuarioCreador = demanda.getUsuarioCreador();
                        String correoUsuario = baseDatos.getCorreoUsuario(idUsuarioCreador);
                        new MandarCorreo.MailJob(user, passwd).execute(
                                new MandarCorreo.MailJob.Mail(user, correoUsuario, "Temporalis: Usuario interesado en demanda", "O usuario " + sIdUsuario + " interesalle a demanda:\n " + demanda.getTitulo() + " con data " + demanda.getData() + " " + demanda.getHora())
                        );
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
                        String user = "temporaliscompostela@gmail.com";
                        String passwd = "m8ECstTEyZ2dqvF";
                        int idServizo = demanda.getIdServizo();
                        String sIdUsuario= Login.getInstance().eTextUser.getText().toString();
                        int idUsuario = baseDatos.getUserId(sIdUsuario);
                        baseDatos.cancelarEmpregaServizo(idServizo,idUsuario);
                        startActivity(intentOfertas);
                        int idUsuarioCreador = demanda.getUsuarioCreador();
                        String correoUsuario = baseDatos.getCorreoUsuario(idUsuarioCreador);
                        new MandarCorreo.MailJob(user, passwd).execute(
                                new MandarCorreo.MailJob.Mail(user, correoUsuario, "Temporalis: Usuario cancelou demanda", "O usuario " + sIdUsuario + " cancelou a demanda:\n " + demanda.getTitulo() + " con data " + demanda.getData() + " " + demanda.getHora())
                        );
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
        Intent intentMeusServizos = new Intent(this,MeusServizos.class);
        Intent outrosServizos = new Intent(this,ServizosAceptados.class);
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
                baseDatos.setIsPagado(demanda.getIdServizo(), idCliente);
            }
        }
        int idUsuarioCreador = demanda.getUsuarioCreador();
        int saldoCreador = baseDatos.getSaldoHoras(idUsuarioCreador);
        int pago = saldoCreador - (cantidadePago * numUsuarios);;
        baseDatos.updateHoras(idUsuarioCreador, pago);
        Toast.makeText(this, "Transacción realizada", Toast.LENGTH_SHORT).show();
        if(intentAnterior.equals("intentDemandasMeusServizos")){
            startActivity(intentMeusServizos);
        }
        if(intentAnterior.equals("intentDeOutrasDemandas")){
            startActivity(outrosServizos);
        }
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
