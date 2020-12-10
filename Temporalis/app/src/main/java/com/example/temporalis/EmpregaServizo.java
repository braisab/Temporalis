package com.example.temporalis;

public class EmpregaServizo {
    private int idServizo;
    private int idUsuario;
    private boolean pagado;

    public EmpregaServizo(){
    }

    public EmpregaServizo(int idServizo, int idUsuario, boolean pagado) {
        this.idServizo = idServizo;
        this.idUsuario = idUsuario;
        this.pagado = pagado;
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

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }
}