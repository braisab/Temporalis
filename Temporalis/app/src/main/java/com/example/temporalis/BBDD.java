package com.example.temporalis;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
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
            "visible boolean,"+
            "Tempo integer,"+
            "PRIMARY KEY(idServizo),"+
            "FOREIGN KEY(UsuarioCreador) references USUARIO(idUsuario))";

    private String crearEmpregaServizo = "CREATE TABLE EMPREGASERVIZO("+
            "idServizo integer,"+
            "idUsuarioCliente integer,"+
            "Pagado boolean,"+
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

    public String getNomeUsuario(int idUsuario){
        sqLite = getReadableDatabase();
        String sIdUsuario = idUsuario+"";
        String [] param = {sIdUsuario};
        String nome ="";
        Cursor cursor = sqLite.rawQuery("SELECT NomeUsuario FROM USUARIO WHERE idUsuario =?",param);
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

    public String getNomeUsuarioFromMail(String correo){
        sqLite = getReadableDatabase();
        String [] param = {correo};
        String nome ="";
        Cursor cursor = sqLite.rawQuery("SELECT NomeUsuario FROM USUARIO WHERE Correoe =?",param);
        if(cursor.moveToFirst()) {
            nome = cursor.getString(0);
        }
        return nome;
    }

    public String getContrasinalFromMail(String correo){
        sqLite = getReadableDatabase();
        String [] param = {correo};
        String nome ="";
        Cursor cursor = sqLite.rawQuery("SELECT Contrasinal FROM USUARIO WHERE Correoe =?",param);
        if(cursor.moveToFirst()) {
            nome = cursor.getString(0);
        }
        return nome;
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

    public int getCountUsersServizo(int idServizo){
        sqLite = getReadableDatabase();
        String sIdServizo = idServizo + "";
        String [] param = {sIdServizo};
        int countUsers = 0;
        Cursor cursor = sqLite.rawQuery("SELECT COUNT(idUsuarioCliente) FROM EMPREGASERVIZO WHERE idServizo = ?", param);
        if(cursor.moveToFirst()){
            countUsers = cursor.getInt(0);
        }
        return countUsers;
    }

    public int getUserId(String nomeUsuario){
        sqLite = getReadableDatabase();
        String [] nome = {nomeUsuario};
        Cursor cursor = sqLite.rawQuery("Select idUsuario from USUARIO where nomeUsuario =?",nome);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        return id;
    }

    public Usuario getUsuario(String nomeUsuario){
        sqLite = getReadableDatabase();
        String[] nome = {nomeUsuario};
        Cursor cursor = sqLite.rawQuery("SELECT * FROM USUARIO WHERE nomeUsuario = ?", nome);
        cursor.moveToFirst();
        int idUsuario = cursor.getInt(0);
        String nomeU = cursor.getString(1);
        String contrasinal = cursor.getString(2);
        String correo = cursor.getString(3);
        int telefono = cursor.getInt(4);
        String lugar = cursor.getString(5);
        String perfil = cursor.getString(6);
        int horas = cursor.getInt(7);
        Usuario usuario = new Usuario(idUsuario,nomeU,contrasinal,correo,telefono,perfil,lugar,horas);
        return usuario;
    }

    public void modificarPerfil(String idUsuario, String nome, String correo, String telefono, String zona, String perfil){
        sqLite = getWritableDatabase();
        String [] params = {nome,correo,telefono,zona,perfil, idUsuario};
        sqLite.execSQL("UPDATE USUARIO SET NomeUsuario = ?, Correoe = ?, Telefono = ?, Localizacion = ?, Perfil = ? WHERE IdUSUARIO = ?",params);
    }

    public int getSaldoHoras(int idUsuario){
        sqLite = getReadableDatabase();
        int totalHoras = 0;
        String sIdUsuario = idUsuario + "";
        String[] param = {sIdUsuario};
        Cursor cursor = sqLite.rawQuery("SELECT TotalHoras FROM USUARIO WHERE idUsuario =?",param);
        if(cursor.moveToFirst()){
            totalHoras = cursor.getInt(0);
        }
        return totalHoras;
    }

    public String getCorreoUsuario(int idUsuario){
        sqLite = getReadableDatabase();
        String correo = "";
        String sIdUsuario = idUsuario + "";
        String[] param = {sIdUsuario};
        Cursor cursor = sqLite.rawQuery("SELECT Correoe FROM USUARIO WHERE idUsuario =?",param);
        if(cursor.moveToFirst()){
            correo = cursor.getString(0);
        }
        return correo;
    }

    public ArrayList<Integer> getIdUsuariosClientes(int idServizo){
        sqLite = getReadableDatabase();
        ArrayList<Integer> ids = new ArrayList<>();
        String sIdServizo = idServizo + "";
        String [] param = {sIdServizo};
        Cursor cursor = sqLite.rawQuery("SELECT idUsuarioCliente FROM EMPREGASERVIZO WHERE idServizo = ?", param);
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                int idUsuarioCliente = cursor.getInt(0);
                ids.add(idUsuarioCliente);
                cursor.moveToNext();
            }
        }
        return ids;
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
        boolean visible = servizo.isVisible();
        int tempo = servizo.getTempoServizo();
        sqLite.execSQL("INSERT INTO SERVIZO(idServizo,Titulo,Descricion,MaxUsuarios,Data,Hora,Lugar,UsuarioCreador,tipo,visible,Tempo)VALUES('"+idServizo+"','"+titulo+"','"+descricion+"','"+maxUsers+"','"+data+"','"+hora+"','"+lugar+"','"+idCreador+"','"+tipo+"','"+visible+"','"+tempo+"')");
    }

    public void gardarEmpregaServizo(EmpregaServizo empregaServizo){
        sqLite = getWritableDatabase();
        int idServizo = empregaServizo.getIdServizo();
        int idUsuario = empregaServizo.getIdUsuario();
        boolean pagado = empregaServizo.isPagado();
        sqLite.execSQL("INSERT INTO EMPREGASERVIZO(idServizo,idUsuarioCliente,Pagado)VALUES('"+idServizo+"','"+idUsuario+"','"+pagado+"')");
    }

    public ArrayList<Servizo> getOfertasVisibles(){
        sqLite = getReadableDatabase();
        ArrayList<Servizo> ofertas = new ArrayList<>();
        String [] params = {"true", "true"};
        Cursor cursor = sqLite.rawQuery("SELECT * FROM SERVIZO WHERE tipo = ? AND visible = ?",params);
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
            Boolean.parseBoolean(cursor.getString(9));
            servizo.setTempoServizo(cursor.getInt(10));
            ofertas.add(servizo);
            cursor.moveToNext();
        }
        return ofertas;
    }

    public ArrayList<Servizo> getDemandasVisibles(){
        sqLite = getReadableDatabase();
        ArrayList<Servizo> demandas = new ArrayList<>();
        String [] params = {"false", "true"};
        Cursor cursor = sqLite.rawQuery("SELECT * FROM SERVIZO WHERE tipo = ? AND visible = ?",params);
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
            Boolean.parseBoolean(cursor.getString(9));
            servizo.setTempoServizo(cursor.getInt(10));
            demandas.add(servizo);
            cursor.moveToNext();
        }
        return demandas;
    }

    public ArrayList<Servizo> getOfertas(){
        sqLite = getReadableDatabase();
        ArrayList<Servizo> ofertas = new ArrayList<>();
        String [] params = {"true"};
        Cursor cursor = sqLite.rawQuery("SELECT * FROM SERVIZO WHERE tipo = ?",params);
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
            Boolean.parseBoolean(cursor.getString(9));
            servizo.setTempoServizo(cursor.getInt(10));
            ofertas.add(servizo);
            cursor.moveToNext();
        }
        return ofertas;
    }

    public ArrayList<Servizo> getDemandas(){
        sqLite = getReadableDatabase();
        ArrayList<Servizo> demandas = new ArrayList<>();
        String [] params = {"false"};
        Cursor cursor = sqLite.rawQuery("SELECT * FROM SERVIZO WHERE tipo = ?",params);
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
            Boolean.parseBoolean(cursor.getString(9));
            servizo.setTempoServizo(cursor.getInt(10));
            demandas.add(servizo);
            cursor.moveToNext();
        }
        return demandas;
    }

    public Servizo getOferta(int idServizo){
        sqLite = getReadableDatabase();
        String sId = idServizo +"";
        String [] params = {sId};
        Cursor cursor = sqLite.rawQuery("SELECT * FROM SERVIZO WHERE idServizo = ?",params);
        cursor.moveToNext();
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
        Boolean.parseBoolean(cursor.getString(9));
        servizo.setTempoServizo(cursor.getInt(10));
        return servizo;
    }

    public ArrayList<Servizo> getDemandasCreadas(int idUsuario){
        sqLite = getReadableDatabase();
        ArrayList<Servizo> demandas = new ArrayList<>();
        String sIdUsuario = idUsuario + "";
        String [] params = {sIdUsuario, "false"};
        Cursor cursor = sqLite.rawQuery("SELECT * FROM SERVIZO WHERE UsuarioCreador = ? AND tipo = ?",params);
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
            Boolean.parseBoolean(cursor.getString(9));
            servizo.setTempoServizo(cursor.getInt(10));
            demandas.add(servizo);
            cursor.moveToNext();
        }
        return demandas;
    }

    public void setServizoInvisible(int idServizo){
        sqLite = getWritableDatabase();
        String sIdServizo = idServizo + "";
        String [] param =  {sIdServizo};
        sqLite.execSQL("UPDATE SERVIZO SET visible = 'false' WHERE idServizo =?",param);
    }

    public void setServizoVisible(int idServizo){
        sqLite = getWritableDatabase();
        String sIdServizo = idServizo + "";
        String [] param =  {sIdServizo};
        sqLite.execSQL("UPDATE SERVIZO SET visible = 'true' WHERE idServizo =?",param);
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

    public void updateHoras(int idUsuario, int cantidadeHoras){
        sqLite = getWritableDatabase();
        String sIdUsuario = idUsuario + "";
        String sHoras = cantidadeHoras + "";
        String [] params =  {sHoras , sIdUsuario};
        sqLite.execSQL("UPDATE USUARIO SET TotalHoras = ? WHERE idUsuario =?",params);
    }

    public ArrayList<Servizo> getMinhasOfertas(int idUsuario){
        sqLite = getReadableDatabase();
        ArrayList<Servizo> servizos = new ArrayList<>();
        String sIdUsuario = idUsuario + "";
        String [] params = {sIdUsuario, "true"};
        Cursor cursor = sqLite.rawQuery("SELECT * FROM SERVIZO WHERE UsuarioCreador = ? AND tipo = ?",params);
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
            servizo.setTipo(Boolean.parseBoolean(cursor.getString(8)));
            servizo.setVisible(Boolean.parseBoolean(cursor.getString(9)));
            servizo.setTempoServizo(cursor.getInt(10));
            servizos.add(servizo);
            cursor.moveToNext();
        }
        return servizos;
    }

    public ArrayList<Servizo> getMinhasDemandas(int idUsuario){
        sqLite = getReadableDatabase();
        ArrayList<Servizo> servizos = new ArrayList<>();
        String sIdUsuario = idUsuario + "";
        String [] params = {sIdUsuario, "false"};
        Cursor cursor = sqLite.rawQuery("SELECT * FROM SERVIZO WHERE UsuarioCreador = ? AND tipo = ?",params);
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
            servizo.setTipo(Boolean.parseBoolean(cursor.getString(8)));
            servizo.setVisible(Boolean.parseBoolean(cursor.getString(9)));
            servizo.setTempoServizo(cursor.getInt(10));
            servizos.add(servizo);
            cursor.moveToNext();
        }
        return servizos;
    }

    public void setIsPagado(int idServizo, int idCliente){
        sqLite = getWritableDatabase();
        String sIdServizo = idServizo + "";
        String sIdCliente =idCliente+"";
        String [] params =  {sIdServizo, sIdCliente};
        sqLite.execSQL("UPDATE EMPREGASERVIZO SET Pagado = 'true' WHERE idServizo =? AND idUsuarioCliente = ?",params);
    }

    public ArrayList<Integer> getIdServizosEmpSer(int idUsuario){
        sqLite = getReadableDatabase();
        ArrayList<Integer> ids = new ArrayList<>();
        String sIdUsuario = idUsuario +"";
        String[] param = {sIdUsuario};
        Cursor cursor = sqLite.rawQuery("SELECT idServizo FROM EMPREGASERVIZO WHERE idUsuarioCliente = ?", param);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                int id = cursor.getInt(0);
                ids.add(id);
                cursor.moveToNext();
            }
        }
        return ids;
    }

    public ArrayList<Integer> getIdServizosEmpSerNonPagados(int idUsuario){
        sqLite = getReadableDatabase();
        ArrayList<Integer> ids = new ArrayList<>();
        String sIdUsuario = idUsuario +"";
        String[] param = {sIdUsuario, "false"};
        Cursor cursor = sqLite.rawQuery("SELECT idServizo FROM EMPREGASERVIZO WHERE idUsuarioCliente = ? AND Pagado = ?", param);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                int id = cursor.getInt(0);
                ids.add(id);
                cursor.moveToNext();
            }
        }
        return ids;
    }

    public Servizo getOfertaAceptada(int idServizo){
        sqLite = getReadableDatabase();
        String sIdServizo = idServizo + "";
        String [] params = {sIdServizo};
        Servizo servizo = new Servizo();
        Cursor cursor = sqLite.rawQuery("SELECT * FROM SERVIZO WHERE idServizo = ?", params);
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                servizo.setIdServizo(cursor.getInt(0));
                servizo.setTitulo(cursor.getString(1));
                servizo.setDescricion(cursor.getString(2));
                servizo.setNumUsuarios(cursor.getInt(3));
                servizo.setData(cursor.getString(4));
                servizo.setHora(cursor.getString(5));
                servizo.setLugar(cursor.getString(6));
                servizo.setUsuarioCreador(cursor.getInt(7));
                servizo.setTipo(Boolean.parseBoolean(cursor.getString(8)));
                servizo.setVisible(Boolean.parseBoolean(cursor.getString(9)));
                servizo.setTempoServizo(cursor.getInt(10));
                cursor.moveToNext();
            }
        }
        return servizo;
    }

    public int countEmpregaServizo (int idServizo){
        sqLite = getReadableDatabase();
        int relacions = 0;
        String sIdServizo = idServizo + "";
        String [] params = {sIdServizo};
        Cursor cursor = sqLite.rawQuery("SELECT COUNT(idServizo) FROM EMPREGASERVIZO WHERE idServizo = ?", params);
        if(cursor.moveToFirst()){
           relacions = cursor.getInt(0);
        }
        return relacions;
    }

    public int countEmpregaServizoPagados (int idServizo){
        sqLite = getReadableDatabase();
        int relacions = 0;
        String sIdServizo = idServizo + "";
        String [] params = {sIdServizo, "true"};
        Cursor cursor = sqLite.rawQuery("SELECT COUNT(idServizo) FROM EMPREGASERVIZO WHERE idServizo = ? AND Pagado = ?", params);
        if(cursor.moveToFirst()){
            relacions = cursor.getInt(0);
        }
        return relacions;
    }

    public void updateServizo(String titulo, String descricion, String data, String hora,  String maxUsers, String lugar, String duracion, String idServizo){
        sqLite = getWritableDatabase();
        String [] params = {titulo, descricion, data, hora, maxUsers, lugar, duracion, idServizo};
        sqLite.execSQL("UPDATE SERVIZO SET Titulo = ?, Descricion=?, Data=?, Hora=?, MaxUsuarios=?, Lugar = ?, Tempo = ? WHERE idServizo = ?", params);
    }
}
