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
        final ArrayList<Servizo> ofertas = baseDatos.getOfertas();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent demandasIntent = new Intent(this,Demandas.class);
        switch (item.getItemId()) {
            case R.id.action_bar_demandas:
                startActivity(demandasIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
