/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jorge
 */
@Entity
public class DocumentoReembolsos implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pathArchivo;
    private Long usuarioCarga;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCarga;
    private String estado;
    private String usuarioAutoriza;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutoriza;
    private String razonRechazo;
    private String idsXml;
    private String tipoReembolso;
    private Long idAprobador;

    private String motivoViaje;
    private String periodoViaje;
    private String lugarViaje;
    private BigDecimal fondoEntregado;
    private String observaciones;
    private String seleccion;
    
    private String justificacionBase64;
    private String tipoJustificacionBase64;
    private Integer tresFirmas;
    private String numeroRC;
    private String usuarioProcesa;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaProcesa;
    private String numeroReembolso;
    
    @OneToOne(mappedBy = "documentoReembolsos")
    private LiquidacionCompra liquidacionCompra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathArchivo() {
        return pathArchivo;
    }

    public void setPathArchivo(String pathArchivo) {
        this.pathArchivo = pathArchivo;
    }

    public Long getUsuarioCarga() {
        return usuarioCarga;
    }

    public void setUsuarioCarga(Long usuarioCarga) {
        this.usuarioCarga = usuarioCarga;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuarioAutoriza() {
        return usuarioAutoriza;
    }

    public void setUsuarioAutoriza(String usuarioAutoriza) {
        this.usuarioAutoriza = usuarioAutoriza;
    }

    public Date getFechaAutoriza() {
        return fechaAutoriza;
    }

    public void setFechaAutoriza(Date fechaAutoriza) {
        this.fechaAutoriza = fechaAutoriza;
    }

    public String getRazonRechazo() {
        return razonRechazo;
    }

    public void setRazonRechazo(String razonRechazo) {
        this.razonRechazo = razonRechazo;
    }

    public String getIdsXml() {
        return idsXml;
    }

    public void setIdsXml(String idsXml) {
        this.idsXml = idsXml;
    }

    public String getTipoReembolso() {
        return tipoReembolso;
    }

    public void setTipoReembolso(String tipoReembolso) {
        this.tipoReembolso = tipoReembolso;
    }

    public Long getIdAprobador() {
        return idAprobador;
    }

    public void setIdAprobador(Long idAprobador) {
        this.idAprobador = idAprobador;
    }

    public String getMotivoViaje() {
        return motivoViaje;
    }

    public void setMotivoViaje(String motivoViaje) {
        this.motivoViaje = motivoViaje;
    }

    public String getPeriodoViaje() {
        return periodoViaje;
    }

    public void setPeriodoViaje(String periodoViaje) {
        this.periodoViaje = periodoViaje;
    }

    public String getLugarViaje() {
        return lugarViaje;
    }

    public void setLugarViaje(String lugarViaje) {
        this.lugarViaje = lugarViaje;
    }

    public BigDecimal getFondoEntregado() {
        return fondoEntregado;
    }

    public void setFondoEntregado(BigDecimal fondoEntregado) {
        this.fondoEntregado = fondoEntregado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(String seleccion) {
        this.seleccion = seleccion;
    }

    public String getJustificacionBase64() {
        return justificacionBase64;
    }

    public void setJustificacionBase64(String justificacionBase64) {
        this.justificacionBase64 = justificacionBase64;
    }

    public String getTipoJustificacionBase64() {
        return tipoJustificacionBase64;
    }

    public void setTipoJustificacionBase64(String tipoJustificacionBase64) {
        this.tipoJustificacionBase64 = tipoJustificacionBase64;
    }

    public Integer getTresFirmas() {
        return tresFirmas;
    }

    public void setTresFirmas(Integer tresFirmas) {
        this.tresFirmas = tresFirmas;
    }

    public String getNumeroRC() {
        return numeroRC;
    }

    public void setNumeroRC(String numeroRC) {
        this.numeroRC = numeroRC;
    }

    public String getUsuarioProcesa() {
        return usuarioProcesa;
    }

    public void setUsuarioProcesa(String usuarioProcesa) {
        this.usuarioProcesa = usuarioProcesa;
    }

    public Date getFechaProcesa() {
        return fechaProcesa;
    }

    public void setFechaProcesa(Date fechaProcesa) {
        this.fechaProcesa = fechaProcesa;
    }

    public String getNumeroReembolso() {
        return numeroReembolso;
    }

    public void setNumeroReembolso(String numeroReembolso) {
        this.numeroReembolso = numeroReembolso;
    }

    public LiquidacionCompra getLiquidacionCompra() {
        return liquidacionCompra;
    }

    public void setLiquidacionCompra(LiquidacionCompra liquidacionCompra) {
        this.liquidacionCompra = liquidacionCompra;
    }

    
}
