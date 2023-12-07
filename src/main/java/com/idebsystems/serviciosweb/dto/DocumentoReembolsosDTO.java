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
    private Long idAprobador;

    private String motivoViaje;
    private String periodoViaje;
    private String lugarViaje;
    private BigDecimal fondoEntregado;
    private String observaciones;
    private String seleccion;
    
    private String justificacionBase64;
    private String tipoJustificacionBase64;
    //
    private String respuesta;
    private Integer totalRegistros;
    private String claveFirma;
    private boolean terceraFirma;
    private Integer tresFirmas;
    private String numeroRC;
    private String aprobador;
    private String usuarioProcesa;
    private Long fechaProcesaLong;
    private String tipoReembolsoNombre;
    private String numeroReembolso;
    //
    private LiquidacionCompraDTO liquidacionCompra;

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

    public String getTipoJustificacionBase64() {
        return tipoJustificacionBase64;
    }

    public void setTipoJustificacionBase64(String tipoJustificacionBase64) {
        this.tipoJustificacionBase64 = tipoJustificacionBase64;
    }

    public String getClaveFirma() {
        return claveFirma;
    }

    public void setClaveFirma(String claveFirma) {
        this.claveFirma = claveFirma;
    }

    public boolean isTerceraFirma() {
        return terceraFirma;
    }

    public void setTerceraFirma(boolean terceraFirma) {
        this.terceraFirma = terceraFirma;
    }

    public Integer getTresFirmas() {
        return tresFirmas;
    }

    public void setTresFirmas(Integer tresFirmas) {
        this.tresFirmas = tresFirmas;
    }

    public Long getIdAprobador() {
        return idAprobador;
    }

    public void setIdAprobador(Long idAprobador) {
        this.idAprobador = idAprobador;
    }

    public String getNumeroRC() {
        return numeroRC;
    }

    public void setNumeroRC(String numeroRC) {
        this.numeroRC = numeroRC;
    }

    public String getAprobador() {
        return aprobador;
    }

    public void setAprobador(String aprobador) {
        this.aprobador = aprobador;
    }

    public String getUsuarioProcesa() {
        return usuarioProcesa;
    }

    public void setUsuarioProcesa(String usuarioProcesa) {
        this.usuarioProcesa = usuarioProcesa;
    }

    public Long getFechaProcesaLong() {
        return fechaProcesaLong;
    }

    public void setFechaProcesaLong(Long fechaProcesaLong) {
        this.fechaProcesaLong = fechaProcesaLong;
    }

    public String getTipoReembolsoNombre() {
        return tipoReembolsoNombre;
    }

    public void setTipoReembolsoNombre(String tipoReembolsoNombre) {
        this.tipoReembolsoNombre = tipoReembolsoNombre;
    }

    public String getNumeroReembolso() {
        return numeroReembolso;
    }

    public void setNumeroReembolso(String numeroReembolso) {
        this.numeroReembolso = numeroReembolso;
    }

    public LiquidacionCompraDTO getLiquidacionCompra() {
        return liquidacionCompra;
    }

    public void setLiquidacionCompra(LiquidacionCompraDTO liquidacionCompra) {
        this.liquidacionCompra = liquidacionCompra;
    }

    @Override
    public String toString() {
        return "DocumentoReembolsosDTO{" + "id=" + id + ", pathArchivo=" + pathArchivo + ", usuarioCarga=" + usuarioCarga + ", fechaCargaLong=" + fechaCargaLong + ", estado=" + estado + ", usuarioAutoriza=" + usuarioAutoriza + ", fechaAutorizaLong=" + fechaAutorizaLong + ", razonRechazo=" + razonRechazo +  ", claveFirma=" + claveFirma + '}';
    }

}
