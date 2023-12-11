/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dto;

/**
 *
 * @author jorge
 */
public class UsuarioDTO {
    
    private long id;
    private String nombre;
    private String usuario;
    private String clave;
    private String correo;
    private long idEstado;
    private long idRol;
    private String cedula;
    private String idEmpleado;
    private String cargo;
    //
    private String nombreRol;
    private String respuesta;
    private int alertaFD;
    private String textoAlertaFD;
    //para la aterta de las LC
    private int alertaLC;
    private String textoAlertaLC;

    public UsuarioDTO() {
    }
    
    public UsuarioDTO(String respuesta) {
        this.respuesta = respuesta;
    }
    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    

    public long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(long idEstado) {
        this.idEstado = idEstado;
    }

    public long getIdRol() {
        return idRol;
    }

    public void setIdRol(long idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public int getAlertaFD() {
        return alertaFD;
    }

    public void setAlertaFD(int alertaFD) {
        this.alertaFD = alertaFD;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTextoAlertaFD() {
        return textoAlertaFD;
    }

    public void setTextoAlertaFD(String textoAlertaFD) {
        this.textoAlertaFD = textoAlertaFD;
    }

    public int getAlertaLC() {
        return alertaLC;
    }

    public void setAlertaLC(int alertaLC) {
        this.alertaLC = alertaLC;
    }

    public String getTextoAlertaLC() {
        return textoAlertaLC;
    }

    public void setTextoAlertaLC(String textoAlertaLC) {
        this.textoAlertaLC = textoAlertaLC;
    }
    
    
    @Override
    public String toString() {
        return "UsuarioDTO{" + "id=" + id + ", nombre=" + nombre + ", usuario=" + usuario + ", clave=" + clave + ", correo=" + correo + ", idEstado=" + idEstado + '}';
    }
    
    
}
