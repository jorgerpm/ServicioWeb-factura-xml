/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author israe
 */
@Entity
@Table(name = "rol")
public class Rol implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String nombre;
    private boolean principal;
    private long idEstado;
    private boolean autorizador;
    //
    @Column(name = "b_factura")
    private boolean bFactura;
    @Column(name = "b_retencion")
    private boolean bRetencion;
    @Column(name = "b_nota_credito")
    private boolean bNotaCredito;
    @Column(name = "b_nota_debito")
    private boolean bNotaDebito;
    @Column(name = "b_guia_remision")
    private boolean bGuiaRemision;
    private boolean datosContable;
    private boolean cargaliquidacion;
    private long idUsuarioModifica;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModifica;

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

    public boolean isCargaliquidacion() {
        return cargaliquidacion;
    }

    public void setCargaliquidacion(boolean cargaliquidacion) {
        this.cargaliquidacion = cargaliquidacion;
    }

}
