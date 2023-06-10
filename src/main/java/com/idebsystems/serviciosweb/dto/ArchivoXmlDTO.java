/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dto;

import java.util.Date;

/**
 *
 * @author jorge
 */
public class ArchivoXmlDTO {
    
    private Long id;
    private String estadoSri;
    private String numeroAutorizacion;
    private String claveAcceso;
    private Date fechaAutorizacion;
    private Date fechaEmision;
    private String ambiente;
    private String comprobante;
    private String xmlBase64;
    private String pdfBase64;
    private Long idUsuarioCarga;
    private String ubicacionArchivo;
    
    private String urlArchivo;
    private String codigoJDProveedor;
    private String nombreArchivoXml;
    private String nombreArchivoPdf;
    private String tipoDocumento;
    private boolean exportado;
    //
    private String nombreUsuario;
    private Integer totalRegistros;
    private String razonSocial;
    //
    private String razonAnulacion;
    private String usuarioAnula;
    private Date fechaAnula;
    private String estadoSistema;
    private String tipoDocumentoTexto;
    private String tipoGasto;
    private boolean esFisica;
    private Date fechaCarga;
    private String asistentes;
    //
    private String detalle;
    private String precioUnitario;
    //para retenciones
    private String codDocSustento;
    private String numDocSustento;
    private String fechaEmisionDocSustento;
    private String codigoRetencion;
    private String baseImponible;
    private String porcentajeRetener;
    private String valorRetenido;
    private String version;
    

    public String getEstadoSri() {
        return estadoSri;
    }

    public void setEstadoSri(String estadoSri) {
        this.estadoSri = estadoSri;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public String getXmlBase64() {
        return xmlBase64;
    }

    public void setXmlBase64(String xmlBase64) {
        this.xmlBase64 = xmlBase64;
    }

    public String getPdfBase64() {
        return pdfBase64;
    }

    public void setPdfBase64(String pdfBase64) {
        this.pdfBase64 = pdfBase64;
    }

    

    public Long getIdUsuarioCarga() {
        return idUsuarioCarga;
    }

    public void setIdUsuarioCarga(Long idUsuarioCarga) {
        this.idUsuarioCarga = idUsuarioCarga;
    }

    public String getUbicacionArchivo() {
        return ubicacionArchivo;
    }

    public void setUbicacionArchivo(String ubicacionArchivo) {
        this.ubicacionArchivo = ubicacionArchivo;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    public String getCodigoJDProveedor() {
        return codigoJDProveedor;
    }

    public void setCodigoJDProveedor(String codigoJDProveedor) {
        this.codigoJDProveedor = codigoJDProveedor;
    }

    public String getNombreArchivoXml() {
        return nombreArchivoXml;
    }

    public void setNombreArchivoXml(String nombreArchivoXml) {
        this.nombreArchivoXml = nombreArchivoXml;
    }

    public String getNombreArchivoPdf() {
        return nombreArchivoPdf;
    }

    public void setNombreArchivoPdf(String nombreArchivoPdf) {
        this.nombreArchivoPdf = nombreArchivoPdf;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Integer getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(Integer totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public boolean isExportado() {
        return exportado;
    }

    public void setExportado(boolean exportado) {
        this.exportado = exportado;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazonAnulacion() {
        return razonAnulacion;
    }

    public void setRazonAnulacion(String razonAnulacion) {
        this.razonAnulacion = razonAnulacion;
    }

    public String getUsuarioAnula() {
        return usuarioAnula;
    }

    public void setUsuarioAnula(String usuarioAnula) {
        this.usuarioAnula = usuarioAnula;
    }

    public Date getFechaAnula() {
        return fechaAnula;
    }

    public void setFechaAnula(Date fechaAnula) {
        this.fechaAnula = fechaAnula;
    }

    public String getEstadoSistema() {
        return estadoSistema;
    }

    public void setEstadoSistema(String estadoSistema) {
        this.estadoSistema = estadoSistema;
    }

    public String getTipoDocumentoTexto() {
        return tipoDocumentoTexto;
    }

    public void setTipoDocumentoTexto(String tipoDocumentoTexto) {
        this.tipoDocumentoTexto = tipoDocumentoTexto;
    }

    public String getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(String tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public boolean isEsFisica() {
        return esFisica;
    }

    public void setEsFisica(boolean esFisica) {
        this.esFisica = esFisica;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(String precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(String asistentes) {
        this.asistentes = asistentes;
    }

    public String getCodDocSustento() {
        return codDocSustento;
    }

    public void setCodDocSustento(String codDocSustento) {
        this.codDocSustento = codDocSustento;
    }

    public String getNumDocSustento() {
        return numDocSustento;
    }

    public void setNumDocSustento(String numDocSustento) {
        this.numDocSustento = numDocSustento;
    }

    public String getFechaEmisionDocSustento() {
        return fechaEmisionDocSustento;
    }

    public void setFechaEmisionDocSustento(String fechaEmisionDocSustento) {
        this.fechaEmisionDocSustento = fechaEmisionDocSustento;
    }

    public String getCodigoRetencion() {
        return codigoRetencion;
    }

    public void setCodigoRetencion(String codigoRetencion) {
        this.codigoRetencion = codigoRetencion;
    }

    public String getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(String baseImponible) {
        this.baseImponible = baseImponible;
    }

    public String getPorcentajeRetener() {
        return porcentajeRetener;
    }

    public void setPorcentajeRetener(String porcentajeRetener) {
        this.porcentajeRetener = porcentajeRetener;
    }

    public String getValorRetenido() {
        return valorRetenido;
    }

    public void setValorRetenido(String valorRetenido) {
        this.valorRetenido = valorRetenido;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    @Override
    public String toString() {
        return "ArchivoXmlDTO{" + "id=" + id + ", estadoSri=" + estadoSri + ", numeroAutorizacion=" + numeroAutorizacion + ", claveAcceso=" + claveAcceso + ", fechaAutorizacion=" + fechaAutorizacion + ", fechaEmision=" + fechaEmision + ", ambiente=" + ambiente + ", comprobante=" + comprobante + ", idUsuarioCarga=" + idUsuarioCarga + ", ubicacionArchivo=" + ubicacionArchivo + ", urlArchivo=" + urlArchivo + ", codigoJDProveedor=" + codigoJDProveedor + ", nombreArchivoXml=" + nombreArchivoXml + ", nombreArchivoPdf=" + nombreArchivoPdf + ", tipoDocumento=" + tipoDocumento + ", exportado=" + exportado + ", nombreUsuario=" + nombreUsuario + ", totalRegistros=" + totalRegistros + ", razonSocial=" + razonSocial + ", razonAnulacion=" + razonAnulacion + '}';
    } 
            
}
