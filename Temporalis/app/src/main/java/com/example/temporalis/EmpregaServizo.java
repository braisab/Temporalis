package com.example.temporalis;

public class EmpregaServizo {
    private int idServizo;
    private int idUsuario;

    public EmpregaServizo(){
    }

    public EmpregaServizo(int idServizo, int idUsuario) {
        this.idServizo = idServizo;
        this.idUsuario = idUsuario;
    }

    public int getIdServizo() {
        return idServizo;
    }

    public void setIdServizo(int idServizo) {
        this.idServizo = idServizo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}