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

public class Ofertas extends AppCompatActivity {
    BBDD baseDatos;
    Servizo oferta;
    private static Ofertas myContext;
    public Ofertas() {
        myContext =  this;
    }
    public static Ofertas getInstance() {
        return myContext;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas);
        checkMaxUsers();
        checkDate();
        cargarListView();
        crearOferta();
    }

    public void crearOferta(){
        final Intent crearOfertaIntent= new Intent(this, CrearOferta.class);
        Button btnCrearOferta = findViewById(R.id.btnCrearOferta);
        btnCrearOferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(crearOfertaIntent);
            }
        });
    }

    public void cargarListView(){
        final Intent ofertaIntent = new Intent(this,Oferta.class);
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        final ArrayList<Servizo> ofertas = baseDatos.getOfertasVisibles();
        ArrayAdapter<Servizo> arrayAdapter;
        ListView listView = findViewById(R.id.listViewOfertas);
        arrayAdapter = new ArrayAdapter<Servizo>(this, android.R.layout.simple_list_item_1,ofertas){

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
                oferta = ofertas.get(position);
                startActivity(ofertaIntent);
            }
        });
    }

    public void  checkMaxUsers(){
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        ArrayList<Servizo> ofertas = baseDatos.getOfertas();
        for(Servizo oferta : ofertas) {
            int countUsers = baseDatos.getCountUsersServizo(oferta.getIdServizo());
            if(countUsers >= oferta.getNumUsuarios()){
                baseDatos.setServizoInvisible(oferta.getIdServizo());
            }
        }
    }

    public void checkDate(){
        baseDatos = new BBDD(this);
        baseDatos.getReadableDatabase();
        ArrayList<Servizo> ofertas = baseDatos.getOfertas();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String sToday = dateFormat.format(Calendar.getInstance().getTime());
            Date today = dateFormat.parse(sToday);
            for (Servizo oferta : ofertas) {
                String data = oferta.getData();
                String hora = oferta.getHora();
                Date dataServizo = dateFormat.parse(data+ " "+hora);
                if (today.after(dataServizo)) {
                        baseDatos.setServizoInvisible(oferta.getIdServizo());
                }
            }
        }catch (ParseException e){
            Log.e("Erro", "Erro no parsing da data");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent demandasIntent = new Intent(this,Demandas.class);
        Intent selfIntent = new Intent(this,MeusServizos.class);
        Intent otherServices = new Intent(this,ServizosAceptados.class);
        Intent perfilIntent = new Intent(this,Perfil.class);
        switch (item.getItemId()) {
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
