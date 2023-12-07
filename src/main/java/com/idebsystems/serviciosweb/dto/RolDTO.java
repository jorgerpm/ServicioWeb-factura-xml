/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb.dto;

import java.util.Date;
import javax.persistence.Temporal;

/**
 *
 * @author israe
 */
public class RolDTO {

    private long id;
    private String nombre;
    private boolean principal;
    private long idEstado;
    private boolean autorizador;
    //
    private boolean bFactura;
    private boolean bRetencion;
    private boolean bNotaCredito;
    private boolean bNotaDebito;
    private boolean bGuiaRemision;
    //
    private boolean datosContable;
    private boolean cargaliquidacion;
    private long idUsuarioModifica;
    private Date fechaModifica;
    private UsuarioDTO usuario;
    private String listaIdEmpresas;

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

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(long idEstado) {
        this.idEstado = idEstado;
    }

    public boolean isAutorizador() {
        return autorizador;
    }

    public void setAutorizador(boolean autorizador) {
        this.autorizador = autorizador;
    }

    public boolean isbFactura() {
        return bFactura;
    }

    public void setbFactura(boolean bFactura) {
        this.bFactura = bFactura;
    }

    public boolean isbRetencion() {
        return bRetencion;
    }

    public void setbRetencion(boolean bRetencion) {
        this.bRetencion = bRetencion;
    }

    public boolean isbNotaCredito() {
        return bNotaCredito;
    }

    public void setbNotaCredito(boolean bNotaCredito) {
        this.bNotaCredito = bNotaCredito;
    }

    public boolean isbNotaDebito() {
        return bNotaDebito;
    }

    public void setbNotaDebito(boolean bNotaDebito) {
        this.bNotaDebito = bNotaDebito;
    }

    public boolean isbGuiaRemision() {
        return bGuiaRemision;
    }

    public void setbGuiaRemision(boolean bGuiaRemision) {
        this.bGuiaRemision = bGuiaRemision;
    }

    public boolean isDatosContable() {
        return datosContable;
    }

    public void setDatosContable(boolean datosContable) {
        this.datosContable = datosContable;
    }

    public long getIdUsuarioModifica() {
        return idUsuarioModifica;
    }

    public void setIdUsuarioModifica(long idUsuarioModifica) {
        this.idUsuarioModifica = idUsuarioModifica;
    }

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public String getListaIdEmpresas() {
        return listaIdEmpresas;
    }

    public void setListaIdEmpresas(String listaIdEmpresas) {
        this.listaIdEmpresas = listaIdEmpresas;
    }

    public boolean isCargaliquidacion() {
        return cargaliquidacion;
    }

    public void setCargaliquidacion(boolean cargaliquidacion) {
        this.cargaliquidacion = cargaliquidacion;
    }

    @Override
    public String toString() {
        return "RolDTO{" + "id=" + id + ", nombre=" + nombre + ", principal=" + principal + ", idEstado=" + idEstado + ", autorizador=" + autorizador + ", bFactura=" + bFactura + ", bRetencion=" + bRetencion + ", bNotaCredito=" + bNotaCredito + ", bNotaDebito=" + bNotaDebito + ", bGuiaRemision=" + bGuiaRemision + ", datosContable=" + datosContable + ", idUsuarioModifica=" + idUsuarioModifica + ", listaIdEmpresas=" + listaIdEmpresas + '}';
    }

    
}
