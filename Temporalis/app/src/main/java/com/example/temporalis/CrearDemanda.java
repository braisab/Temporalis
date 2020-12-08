package com.example.temporalis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CrearDemanda extends AppCompatActivity {
    public BBDD baseDatos;
    Servizo demanda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_demandas);
        xestionarBotons();
    }

    public void xestionarBotons(){
        final Intent volverIntent= new Intent(this,Demandas.class);
        baseDatos = new BBDD(this);
        baseDatos.getWritableDatabase();
        Button btnCrear = findViewById(R.id.btnCrearDem);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idServizo = baseDatos.getMaxServizoId()+1;
                String sIdCeador = Login.getInstance().eTextUser.getText().toString();
                int idCreador = baseDatos.getUserId(sIdCeador);
                EditText eTTitulo = findViewById(R.id.eTextDemTitulo);
                String titulo = eTTitulo.getText().toString();
                EditText eTDescricion = findViewById(R.id.eTextDemDescricion);
                String descricion = eTDescricion.getText().toString();
                EditText eTData = findViewById(R.id.eTextDemData);
                String data = eTData.getText().toString();
                EditText eTHora = findViewById(R.id.eTextDemHora);
                String hora = eTHora.getText().toString();
                EditText etextLugar = findViewById(R.id.eDemLugar);
                String lugar = etextLugar.getText().toString();
                EditText eTMaxUsers = findViewById(R.id.eDemMaxUsers);
                String sMaxPersoas = eTMaxUsers.getText().toString();
                boolean tipo = false;
                boolean visible = true;
                EditText eTextDuracion = findViewById(R.id.eTextDemTempo);
                String sDuracion = eTextDuracion.getText().toString();
                if(titulo.equals("")||descricion.equals("")||data.equals("")||hora.equals("")||lugar.equals("")||sMaxPersoas.equals("")||sDuracion.equals("")){
                    Toast.makeText(CrearDemanda.this, "Todos os campos son obrigatorios", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checkDate(data,hora)){
                    eTData.setError("A data ou a hora da demanda non é válida");
                    eTHora.setError("A data ou a hora da demanda non é válida");
                    return;
                }
                int tempo =Integer.parseInt(sDuracion);
                int maxPersoas;
                if(sMaxPersoas.equals("")) {
                    maxPersoas = 25000;
                }else {
                    maxPersoas = Integer.parseInt(sMaxPersoas);
                }
                demanda = new Servizo(idServizo, titulo,descricion,maxPersoas,data,hora,lugar,idCreador,tipo,visible,tempo);
                baseDatos.gardarServizo(demanda);
                Toast.makeText(CrearDemanda.this, "Demanda creada", Toast.LENGTH_SHORT).show();
                startActivity(volverIntent);
            }
        });
    }

    public boolean checkDate(String data, String hora){
        boolean isDatePass= false;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Calendar cal = Calendar.getInstance();
            String sToday = dateFormat.format(Calendar.getInstance().getTime());
            Date today = dateFormat.parse(sToday);
            dateFormat.setLenient(false);
            Date dataServizo = dateFormat.parse(data+ " "+hora);
            cal.setTime(today);
            cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+1);
            if (dataServizo.before(cal.getTime())) {
                isDatePass = true;
            }
        }catch (ParseException e){
            Log.e("Erro", "Erro no parsing da data");
            isDatePass = true;
        }return isDatePass;
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
