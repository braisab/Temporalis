package com.example.temporalis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ServizosAceptados extends AppCompatActivity {
    BBDD baseDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas);
        Button btnOfertas = findViewById(R.id.btnCrearOferta);
        btnOfertas.setVisibility(View.INVISIBLE);
        cargarListView();
    }

    public void cargarListView(){
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        ArrayList<Servizo> servizos = new ArrayList<>();
        String nomeUsuario = Login.getInstance().eTextUser.getText().toString();
        int idUsuario = baseDatos.getUserId(nomeUsuario);
        ArrayList<Integer> idsServizos = baseDatos.getIdServizosEmpSer(idUsuario);
        for(int idServizo : idsServizos){
            Servizo servizo = baseDatos.getServizoAceptado(idServizo);
            servizos.add(servizo);
        }
        ArrayAdapter<Servizo> arrayAdapter;
        ListView listView = findViewById(R.id.listViewOfertas);
        arrayAdapter = new ArrayAdapter<Servizo>(this, android.R.layout.simple_list_item_1,servizos) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView= view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);
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
            case R.id.action_bar_perfil:
                startActivity(perfilIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
