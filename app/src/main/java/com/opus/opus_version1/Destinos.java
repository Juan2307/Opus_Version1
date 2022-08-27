package com.opus.opus_version1;

public class Destinos {
    //Atributos
    private String documento;
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private String telefono;
    private String email;
    private String profesionesDeInterés;

    //Constructor Vacio
    public Destinos() {
        super();
    }

    public Destinos(String documento, String nombre, String apellido, String fechaNacimiento, String telefono, String email, String profesionesDeInterés) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.profesionesDeInterés = profesionesDeInterés;
    }

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

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    public String getProfesionesDeInterés() {
        return profesionesDeInterés;
    }

    public void setProfesionesDeInterés(String profesionesDeInterés) {
        this.profesionesDeInterés = profesionesDeInterés;
    }
}
