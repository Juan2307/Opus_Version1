package com.opus.opus_version1;

public class Destinos {
    //Variables
    private String documento;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;

    //Constructor Vacio
    public Destinos() {
        super();
    }

    //Constructor Con Los Parametros
    public Destinos(String documento, String nombre, String apellido, String telefono, String email) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
    }

    //Getter and Setter
    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
