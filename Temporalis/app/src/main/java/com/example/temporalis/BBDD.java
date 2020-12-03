package com.example.temporalis;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BBDD extends SQLiteOpenHelper {
    public SQLiteDatabase sqLite;
    public static final String DB_NAME = "bancoTempo.db";
    public static final int DB_VERSION = 1;
    private String crearUsuario ="CREATE TABLE USUARIO("+
            "idUsuario integer,"+
            "NomeUsuario text,"+
            "Contrasinal text,"+
            "Correoe text,"+
            "Telefono integer,"+
            "Localizacion text,"+
            "Perfil text,"+
            "TotalHoras integer,"+
            "PRIMARY KEY(idUsuario))";
    private String crearServizo ="CREATE TABLE SERVIZO("+
            "idServizo integer,"+
            "Titulo text,"+
            "Descricion text,"+
            "MaxUsuarios integer,"+
            "Data text,"+
            "Hora text,"+
            "Lugar text,"+
            "UsuarioCreador integer,"+
            "tipo boolean,"+
            "PRIMARY KEY(idServizo),"+
            "FOREIGN KEY(UsuarioCreador) references USUARIO(idUsuario))";

    private String crearEmpregaServizo = "CREATE TABLE EMPREGASERVIZO("+
            "idServizo integer,"+
            "idUsuarioCliente integer,"+
            "PRIMARY KEY(idServizo, idUsuarioCliente),"+
            "FOREIGN KEY(idServizo) references SERVIZO (idServizo),"+
            "FOREIGN KEY(idUsuarioCliente) references USUARIO(idUsuario))";

    public BBDD(Context context){

        super(context,DB_NAME,null,DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crearUsuario);
        db.execSQL(crearServizo);
        db.execSQL(crearEmpregaServizo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void gardarUsuario(Usuario user){
        sqLite = getWritableDatabase();
        int idUsuario = user.getIdUsuario();
        String nomeUsuario = user.getNomeUsuario();
        String contrasinal = user.getContrasinal();
        String correo = user.getCorreoe();
        int telefono = user.getTelefono();
        String localizacion = user.getLocalizacion();
        String perfil = user.getPerfil();
        int totalHoras = user.getTotalHoras();
        sqLite.execSQL("INSERT INTO USUARIO(idUsuario,NomeUsuario,Contrasinal,Correoe,Telefono,Localizacion,Perfil,TotalHoras)VALUES('"+idUsuario+"','"+nomeUsuario+"','"+contrasinal+"','"+correo+"','"+telefono+"','"+localizacion+"','"+perfil+"','"+totalHoras+"')");
    }

    public String selectNomeUsuario(String contrasinal){
        sqLite = getReadableDatabase();
        String [] sPass = {contrasinal};
        String nome ="";
        Cursor cursor = sqLite.rawQuery("SELECT NomeUsuario FROM USUARIO WHERE Contrasinal =?",sPass);
        if(cursor.moveToFirst()) {
            nome = cursor.getString(0);
        }
        return nome;
    }

    public String selectContrasinal(String nomeUsuario){
        sqLite = getReadableDatabase();
        String [] sNome = {nomeUsuario};
        String contrasinal = "";
        Cursor cursor = sqLite.rawQuery("SELECT Contrasinal FROM USUARIO WHERE NomeUsuario =?",sNome);
        if(cursor.moveToFirst()) {
             contrasinal = cursor.getString(0);
        }
        return contrasinal;
    }

    public String checkNomeUsuario(String nomeUsuario){
        sqLite = getReadableDatabase();
        String [] sPass = {nomeUsuario};
        Cursor cursor = sqLite.rawQuery("SELECT NomeUsuario FROM USUARIO WHERE nomeUsuario =?",sPass);
        if (cursor.moveToFirst() ) {
            String nome = cursor.getString(0);
            return nome;
        }else{
            return "EXISTE";
        }
    }

    public String checkCorreoUsuario(String nomeUsuario){
        sqLite = getReadableDatabase();
        String [] sPass = {nomeUsuario};
        Cursor cursor = sqLite.rawQuery("SELECT Correoe FROM USUARIO WHERE nomeUsuario =?",sPass);
        if (cursor.moveToFirst() ) {
            String correo = cursor.getString(0);
            return correo;
        }else{
            return "EXISTE";
        }
    }

    public String checkTelefonoUsuario(String nomeUsuario){
        sqLite = getReadableDatabase();
        String [] sPass = {nomeUsuario};
        Cursor cursor = sqLite.rawQuery("SELECT Telefono FROM USUARIO WHERE nomeUsuario =?",sPass);
        if (cursor.moveToFirst() ) {
            String telefono = cursor.getString(0);
            return telefono;
        }else{
            return "EXISTE";
        }
    }

    public int getMaxUserId(){
        sqLite = getReadableDatabase();
        int maxId = 0;
        Cursor cursor = sqLite.rawQuery("Select max(idUsuario) from USUARIO",null);
        if(cursor.moveToFirst()) {
            maxId = cursor.getInt(0);
        }
        return maxId;
    }

    public int getUserId(String nomeUsuario){
        sqLite = getReadableDatabase();
        String [] nome = {nomeUsuario};
        Cursor cursor = sqLite.rawQuery("Select idUsuario from USUARIO where nomeUsuario =?",nome);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        return id;
    }

    public int getMaxServizoId(){
        sqLite = getReadableDatabase();
        int maxId = 0;
        Cursor cursor = sqLite.rawQuery("Select max(idServizo) from SERVIZO",null);
        if(cursor.moveToFirst()) {
            maxId = cursor.getInt(0);
        }
        return maxId;
    }

    public void gardarServizo(Servizo servizo){
        sqLite = getWritableDatabase();
        int idServizo = servizo.getIdServizo();
        String titulo = servizo.getTitulo();
        String descricion = servizo.getDescricion();
        String hora = servizo.getHora();
        String data = servizo.getData();
        String lugar = servizo.getLugar();
        int maxUsers = servizo.getNumUsuarios();
        int idCreador = servizo.getUsuarioCreador();
        boolean tipo = servizo.isTipo();
        sqLite.execSQL("INSERT INTO SERVIZO(idServizo,Titulo,Descricion,MaxUsuarios,Data,Hora,Lugar,UsuarioCreador,tipo)VALUES('"+idServizo+"','"+titulo+"','"+descricion+"','"+maxUsers+"','"+data+"','"+hora+"','"+lugar+"','"+idCreador+"','"+tipo+"')");
    }

    public void gardarEmpregaServizo(EmpregaServizo empregaServizo){
        sqLite = getWritableDatabase();
        int idServizo = empregaServizo.getIdServizo();
        int idUsuario = empregaServizo.getIdUsuario();
        sqLite.execSQL("INSERT INTO EMPREGASERVIZO(idServizo,idUsuarioCliente)VALUES('"+idServizo+"','"+idUsuario+"')");
    }
    public ArrayList<Servizo> getOfertas(){
        sqLite = getReadableDatabase();
        ArrayList<Servizo> ofertas = new ArrayList<>();
        String [] isOferta = {"true"};
        Cursor cursor = sqLite.rawQuery("SELECT * FROM SERVIZO WHERE tipo = ?",isOferta);
        cursor.moveToNext();
        while (!cursor.isAfterLast()){
            Servizo servizo = new Servizo();
            servizo.setIdServizo(cursor.getInt(0));
            servizo.setTitulo(cursor.getString(1));
            servizo.setDescricion(cursor.getString(2));
            servizo.setNumUsuarios(cursor.getInt(3));
            servizo.setData(cursor.getString(4));
            servizo.setHora(cursor.getString(5));
            servizo.setLugar(cursor.getString(6));
            servizo.setUsuarioCreador(cursor.getInt(7));
            servizo.setTipo(true);
            ofertas.add(servizo);
            cursor.moveToNext();
        }
        return ofertas;
    }

    public ArrayList<Servizo> getDemandas(){
        sqLite = getReadableDatabase();
        ArrayList<Servizo> ofertas = new ArrayList<>();
        String [] isOferta = {"false"};
        Cursor cursor = sqLite.rawQuery("SELECT * FROM SERVIZO WHERE tipo = ?",isOferta);
        cursor.moveToNext();
        while (!cursor.isAfterLast()){
            Servizo servizo = new Servizo();
            servizo.setIdServizo(cursor.getInt(0));
            servizo.setTitulo(cursor.getString(1));
            servizo.setDescricion(cursor.getString(2));
            servizo.setNumUsuarios(cursor.getInt(3));
            servizo.setData(cursor.getString(4));
            servizo.setHora(cursor.getString(5));
            servizo.setLugar(cursor.getString(6));
            servizo.setUsuarioCreador(cursor.getInt(7));
            servizo.setTipo(false);
            ofertas.add(servizo);
            cursor.moveToNext();
        }
        return ofertas;
    }

    public void borrarServizo(int idServizo){
        sqLite = getReadableDatabase();
        String sIdServizo = idServizo+"";
        String [] id = {sIdServizo};
        sqLite.execSQL("DELETE FROM SERVIZO WHERE idServizo=?",id);
    }

    public boolean checkEmpregaServizo(int idUsuario, int idServizo){
        sqLite = getReadableDatabase();
        String sIdUsuario = idUsuario+"";
        String sIdServizo = idServizo+"";
        String[] params = {sIdServizo,sIdUsuario};
        Cursor cursor = sqLite.rawQuery("SELECT * FROM EMPREGASERVIZO WHERE idServizo = ? AND idUsuarioCliente=?",params);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }

    }

    public void cancelarEmpregaServizo(int idServizo, int idUsuario){
        sqLite = getReadableDatabase();
        String sIdUsuario = idUsuario+"";
        String sIdServizo = idServizo+"";
        String[] params = {sIdServizo,sIdUsuario};
        sqLite.execSQL("DELETE FROM EMPREGASERVIZO WHERE idServizo = ? AND idUsuarioCliente=?",params);
    }
}
