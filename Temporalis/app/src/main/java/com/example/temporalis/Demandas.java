package com.example.temporalis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Demandas extends AppCompatActivity {
    BBDD baseDatos;
    Servizo demanda;
    private static Demandas myContext;
    public Demandas() {
        myContext =  this;
    }
    public static Demandas getInstance() {
        return myContext;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demandas);
        checkMaxUsers();
        checkDate();
        checkSaldoCreadorAboutNUsers();
        cargarListView();
        crearDemandas();
    }

    public void crearDemandas(){
        final Intent crearDemandaIntent= new Intent(this, CrearDemanda.class);
        Button btnCrearDemanda = findViewById(R.id.btnCrearDemanda);
        btnCrearDemanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(crearDemandaIntent);
            }
        });
    }

    public void cargarListView(){
        final Intent demandaIntent= new Intent(this,Demanda.class);
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        final ArrayList<Servizo> demandas = baseDatos.getDemandasVisibles();
        ArrayAdapter<Servizo> arrayAdapter;
        ListView listView = findViewById(R.id.listViewDemandas);
        arrayAdapter = new ArrayAdapter<Servizo>(this, android.R.layout.simple_list_item_1,demandas){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView= view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);
                return view;
            }
        };

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                demanda = demandas.get(position);
                demandaIntent.putExtra("uniqueId", "IntentDemandas");
                demandaIntent.putExtra("intentDeDemanda", demanda);
                startActivity(demandaIntent);
            }
        });
    }

    public void checkDate(){
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        ArrayList<Servizo> demandas = baseDatos.getDemandas();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String sToday = dateFormat.format(Calendar.getInstance().getTime());
            Date today = dateFormat.parse(sToday);
            for (Servizo demanda : demandas) {
                String data = demanda.getData();
                String hora = demanda.getHora();
                Date dataServizo = dateFormat.parse(data+ " "+hora);
                if (today.after(dataServizo)) {
                    baseDatos.setServizoInvisible(demanda.getIdServizo());
                }
            }
        }catch (ParseException e){
            Log.e("Erro", "Erro no parsing da data");
        }
    }

    public void checkMaxUsers(){
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        ArrayList<Servizo> demandas = baseDatos.getDemandas();
        for(Servizo demanda : demandas) {
            int countUsers = baseDatos.getCountUsersServizo(demanda.getIdServizo());
            if(countUsers >= demanda.getNumUsuarios()){
                baseDatos.setServizoInvisible(demanda.getIdServizo());
            }else{
                baseDatos.setServizoVisible(demanda.getIdServizo());
            }
        }
    }

    public void checkSaldoCreadorAboutNUsers(){
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        ArrayList<Servizo> demandas = baseDatos.getDemandas();
        for(Servizo demanda : demandas){
            int idUsuarioCreador = demanda.getUsuarioCreador();
            int saldoCreador = baseDatos.getSaldoHoras(idUsuarioCreador);
            int duracion = demanda.getTempoServizo();
            int countUsers = baseDatos.getCountUsersServizo(demanda.getIdServizo());
            int diferenciaSaldoDuracion = saldoCreador - (duracion * countUsers);
            if(diferenciaSaldoDuracion <= 0){
                baseDatos.setServizoInvisible(demanda.getIdServizo());
            }else{
                baseDatos.setServizoVisible(demanda.getIdServizo());
            }
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
        Intent selfIntent = new Intent(this,MeusServizos.class);
        Intent otherServices = new Intent(this,ServizosAceptados.class);
        Intent perfilIntent = new Intent(this,Perfil.class);
        switch (item.getItemId()) {
            case R.id.action_bar_ofertas:
                startActivity(ofertasIntent);
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
