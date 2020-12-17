
package com.example.temporalis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;

public class MeusServizos extends AppCompatActivity {
    public BBDD baseDatos;
    public Servizo demanda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas);
        Button btnOfertas = findViewById(R.id.btnCrearOferta);
        btnOfertas.setVisibility(View.INVISIBLE);
        TextView txtOfertas = findViewById(R.id.txtOfertas);
        TextView txtDemandas = findViewById(R.id.txtDemandas);
        txtDemandas.setVisibility(View.VISIBLE);
        txtOfertas.setVisibility(View.VISIBLE);
        txtOfertas.setText("AS MIÑAS OFERTAS:");
        txtDemandas.setText("AS MIÑAS DEMANDAS:");
        cargarListViewOfertas();
        cargarListViewDemandas();
    }

    public void cargarListViewOfertas(){
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        String nomeUsuario = Login.getInstance().eTextUser.getText().toString();
        int idUsuario = baseDatos.getUserId(nomeUsuario);
        final ArrayList<Servizo> servizos = baseDatos.getMinhasOfertas(idUsuario);
        final ArrayList<Servizo> ofertasNonPagadas = new ArrayList<>();
        for (Servizo oferta : servizos){
            int idOferta = oferta.getIdServizo();
            int relacion = baseDatos.countEmpregaServizo(idOferta);
            int ofertasPagadas = baseDatos.countEmpregaServizoPagados(idOferta);
            if(relacion != ofertasPagadas || relacion == 0){
                ofertasNonPagadas.add(oferta);
            }
        }
        ArrayAdapter<Servizo> arrayAdapter;
        ListView listView = findViewById(R.id.listViewOfertas);
        arrayAdapter = new ArrayAdapter<Servizo>(this, android.R.layout.simple_list_item_1,ofertasNonPagadas) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView= view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);
        Intent ofertaIntent = new Intent(this, Oferta.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Servizo oferta;
                oferta = ofertasNonPagadas.get(position);
                ofertaIntent.putExtra("uniqueId", "intentMeusServizos");
                ofertaIntent.putExtra("intentDeMeusServizos", oferta);
                startActivity(ofertaIntent);
            }
        });
    }

    public void cargarListViewDemandas(){
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        String nomeUsuario = Login.getInstance().eTextUser.getText().toString();
        int idUsuario = baseDatos.getUserId(nomeUsuario);
        final ArrayList<Servizo> servizos = baseDatos.getMinhasDemandas(idUsuario);
        final ArrayList<Servizo> demandasNonPagadas = new ArrayList<>();
        for (Servizo demanda : servizos){
            int idDemanda = demanda.getIdServizo();
            int relacion = baseDatos.countEmpregaServizo(idDemanda);
            int demandasPagadas = baseDatos.countEmpregaServizoPagados(idDemanda);
            if(relacion != demandasPagadas || relacion == 0){
                demandasNonPagadas.add(demanda);
            }
        }
        ArrayAdapter<Servizo> arrayAdapter;
        ListView listViewDemandas = findViewById(R.id.listViewDemandas);
        arrayAdapter = new ArrayAdapter<Servizo>(this, android.R.layout.simple_list_item_1,demandasNonPagadas) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView= view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);
                return view;
            }
        };
        listViewDemandas.setAdapter(arrayAdapter);
        Intent demandaIntent = new Intent(this, Demanda.class);
        listViewDemandas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                demanda = demandasNonPagadas.get(position);
                demandaIntent.putExtra("uniqueId", "intentDemandasMeusServizos");
                demandaIntent.putExtra("intentDeDemandaMS", demanda);
                startActivity(demandaIntent);
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
        Intent otherServices = new Intent(this,ServizosAceptados.class);
        Intent perfilIntent = new Intent(this,Perfil.class);
        switch (item.getItemId()) {
            case R.id.action_bar_ofertas:
                startActivity(ofertasIntent);
                return true;
            case R.id.action_bar_demandas:
                startActivity(demandasIntent);
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

