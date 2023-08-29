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
    
    //datos para contabilidad
    private String justificativos;
    private String batchIngresoLiquidacion;
    private String batchDocumentoInterno;
    private String p3;
    private String p4;
    private String p5;
    private String phne;
    private String cruce1;
    private String cruce2;
    private String tipoDocumento;
    private String numeroDocumento;
    private String numeroRetencion;
    
    private String justificacionBase64;
    private String tipoJustificacionBase64;
    private Integer tresFirmas;

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

    public String getJustificativos() {
        return justificativos;
    }

    public void setJustificativos(String justificativos) {
        this.justificativos = justificativos;
    }

    public String getBatchIngresoLiquidacion() {
        return batchIngresoLiquidacion;
    }

    public void setBatchIngresoLiquidacion(String batchIngresoLiquidacion) {
        this.batchIngresoLiquidacion = batchIngresoLiquidacion;
    }

    public String getBatchDocumentoInterno() {
        return batchDocumentoInterno;
    }

    public void setBatchDocumentoInterno(String batchDocumentoInterno) {
        this.batchDocumentoInterno = batchDocumentoInterno;
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public String getP4() {
        return p4;
    }

    public void setP4(String p4) {
        this.p4 = p4;
    }

    public String getP5() {
        return p5;
    }

    public void setP5(String p5) {
        this.p5 = p5;
    }

    public String getPhne() {
        return phne;
    }

    public void setPhne(String phne) {
        this.phne = phne;
    }

    public String getCruce1() {
        return cruce1;
    }

    public void setCruce1(String cruce1) {
        this.cruce1 = cruce1;
    }

    public String getCruce2() {
        return cruce2;
    }

    public void setCruce2(String cruce2) {
        this.cruce2 = cruce2;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNumeroRetencion() {
        return numeroRetencion;
    }

    public void setNumeroRetencion(String numeroRetencion) {
        this.numeroRetencion = numeroRetencion;
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

}
