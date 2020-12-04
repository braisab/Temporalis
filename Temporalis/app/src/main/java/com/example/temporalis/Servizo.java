package com.example.temporalis;

public class Servizo {
    private int idServizo;
    private String titulo;
    private String descricion;
    private String hora;
    private String data;
    private String lugar;
    private int numUsuarios;
    private int usuarioCreador;
    private boolean tipo;
    private boolean visible;
    private int tempoServizo;

    public Servizo() {
    }

    public Servizo(int idServizo, String titulo, String descricion, int numUsuarios, String data, String hora, String lugar,int usuarioCreador, boolean tipo, boolean visible, int tempoServizo) {
        this.idServizo = idServizo;
        this.titulo = titulo;
        this.descricion = descricion;
        this.hora = hora;
        this.data = data;
        this.lugar = lugar;
        this.numUsuarios = numUsuarios;
        this.usuarioCreador = usuarioCreador;
        this.tipo = tipo;
        this.visible = visible;
        this.tempoServizo = tempoServizo;
    }

    public int getIdServizo() {
        return idServizo;
    }

    public void setIdServizo(int idServizo) {
        this.idServizo = idServizo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricion() {
        return descricion;
    }

    public void setDescricion(String descricion) {
        this.descricion = descricion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getNumUsuarios() {
        return numUsuarios;
    }

    public void setNumUsuarios(int numUsuarios) {
        this.numUsuarios = numUsuarios;
    }

    public int getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(int usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getTempoServizo() {
        return tempoServizo;
    }

    public void setTempoServizo(int tempoServizo) {
        this.tempoServizo = tempoServizo;
    }

    @Override
    public String toString() {
        return  titulo +"\n"+
                "Data:" + data + "\n" +
                "Hora:" + hora + "\n" +
                "Localizacion:" +lugar+"\n__________________";
    }
}

