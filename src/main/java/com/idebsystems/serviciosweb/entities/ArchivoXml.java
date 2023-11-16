/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jorge
 */
@Entity
@Table(name = "archivoxml")
public class ArchivoXml implements Serializable {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipoDocumento;
    private String estadoSri;
    @Column(name = "autorizacion")
    private String numeroAutorizacion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    private String ambiente;
    private String comprobante;
    private Long idUsuarioCarga;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCarga;
    private String urlArchivo;
    private String codigoJDProveedor;
    private String nombreArchivoXml;
    private String nombreArchivoPdf;
    private String claveAcceso;
    private boolean exportado;
    private String razonAnulacion;
    private String usuarioAnula;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnula;
    private String estadoSistema;
    private String tipoGasto;
    private boolean esFisica;
    private String asistentes;
    private String version;
    private Long idReembolso;
    private String rucEmisor;
    
    @JoinColumn(name = "idReembolso", referencedColumnName = "id", 
            insertable = false, updatable = false, nullable = true)
    @ManyToOne
    private DocumentoReembolsos reembolso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

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

    public Long getIdUsuarioCarga() {
        return idUsuarioCarga;
    }

    public void setIdUsuarioCarga(Long idUsuarioCarga) {
        this.idUsuarioCarga = idUsuarioCarga;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
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

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public boolean isExportado() {
        return exportado;
    }

    public void setExportado(boolean exportado) {
        this.exportado = exportado;
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

    public String getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(String asistentes) {
        this.asistentes = asistentes;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRucEmisor() {
        return rucEmisor;
    }

    public void setRucEmisor(String rucEmisor) {
        this.rucEmisor = rucEmisor;
    }

    public Long getIdReembolso() {
        return idReembolso;
    }

    public void setIdReembolso(Long idReembolso) {
        this.idReembolso = idReembolso;
    }

    public DocumentoReembolsos getReembolso() {
        return reembolso;
    }

    public void setReembolso(DocumentoReembolsos reembolso) {
        this.reembolso = reembolso;
    }

    

}
