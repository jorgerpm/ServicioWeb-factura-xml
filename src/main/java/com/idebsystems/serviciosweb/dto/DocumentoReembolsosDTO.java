/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dto;

import java.math.BigDecimal;

/**
 *
 * @author jorge
 */
public class DocumentoReembolsosDTO {

    private Long id;
    private String pathArchivo;
    private Long usuarioCarga;
    private UsuarioDTO usuario;
    private long fechaCargaLong;
    private String estado;
    private String usuarioAutoriza;
    private long fechaAutorizaLong;
    private String razonRechazo;
    private String archivoBase64;
    private Long idUsuarioAutoriza;
    private String tipoReembolso;

    private String motivoViaje;
    private String periodoViaje;
    private String lugarViaje;
    private BigDecimal fondoEntregado;
    private String observaciones;
    private String seleccion;
    //datos conta
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
    //
    private String respuesta;
    private Integer totalRegistros;

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

    public long getFechaCargaLong() {
        return fechaCargaLong;
    }

    public void setFechaCargaLong(long fechaCargaLong) {
        this.fechaCargaLong = fechaCargaLong;
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

    public long getFechaAutorizaLong() {
        return fechaAutorizaLong;
    }

    public void setFechaAutorizaLong(long fechaAutorizaLong) {
        this.fechaAutorizaLong = fechaAutorizaLong;
    }

    public String getRazonRechazo() {
        return razonRechazo;
    }

    public void setRazonRechazo(String razonRechazo) {
        this.razonRechazo = razonRechazo;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public String getArchivoBase64() {
        return archivoBase64;
    }

    public void setArchivoBase64(String archivoBase64) {
        this.archivoBase64 = archivoBase64;
    }

    public Long getIdUsuarioAutoriza() {
        return idUsuarioAutoriza;
    }

    public void setIdUsuarioAutoriza(Long idUsuarioAutoriza) {
        this.idUsuarioAutoriza = idUsuarioAutoriza;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Integer getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(Integer totalRegistros) {
        this.totalRegistros = totalRegistros;
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

    public String getTipoReembolso() {
        return tipoReembolso;
    }

    public void setTipoReembolso(String tipoReembolso) {
        this.tipoReembolso = tipoReembolso;
    }

    public String getJustificacionBase64() {
        return justificacionBase64;
    }

    public void setJustificacionBase64(String justificacionBase64) {
        this.justificacionBase64 = justificacionBase64;
    }

    @Override
    public String toString() {
        return "DocumentoReembolsosDTO{" + "id=" + id + ", pathArchivo=" + pathArchivo + ", usuarioCarga=" + usuarioCarga + ", fechaCargaLong=" + fechaCargaLong + ", estado=" + estado + ", usuarioAutoriza=" + usuarioAutoriza + ", fechaAutorizaLong=" + fechaAutorizaLong + ", razonRechazo=" + razonRechazo + '}';
    }

}
