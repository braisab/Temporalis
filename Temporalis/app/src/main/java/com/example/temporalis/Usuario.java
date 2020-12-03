package com.example.temporalis;

public class Usuario {
    private int idUsuario;
    private String nomeUsuario;
    private String contrasinal;
    private String correoe;
    private int telefono;
    private String perfil;
    private String localizacion;
    private int totalHoras;

    public Usuario(int idUsuario, String nomeUsuario, String contrasinal, String correoe, int telefono, String perfil, String localizacion, int totalHoras) {
        this.idUsuario = idUsuario;
        this.nomeUsuario = nomeUsuario;
        this.contrasinal = contrasinal;
        this.correoe = correoe;
        this.telefono = telefono;
        this.localizacion = localizacion;
        this.perfil = perfil;
        this.totalHoras = totalHoras;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getContrasinal() {
        return contrasinal;
    }

    public void setContrasinal(String contrasinal) {
        this.contrasinal = contrasinal;
    }

    public String getCorreoe() {
        return correoe;
    }

    public void setCorreoe(String correoe) {
        this.correoe = correoe;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public int getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(int totalHoras) {
        this.totalHoras = totalHoras;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nomeUsuario='" + nomeUsuario + '\'' +
                ", contrasinal='" + contrasinal + '\'' +
                ", correoe='" + correoe + '\'' +
                ", telefono=" + telefono +
                ", perfil='" + perfil + '\'' +
                ", localizacion='" + localizacion + '\'' +
                ", totalHoras=" + totalHoras +
                '}';
    }
}


