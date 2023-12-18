/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. (C) 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dto;

import java.util.List;

/**
 *
 * @author jorge
 */
public class BuscadorArchivoXml {
    
    private String fechaInicio;
    private String fechaFinal;
    private Long idUsuarioCarga;
    private String claveAcceso;
    private String ruc;
    private String tipoDocumento;
    private String estadoSistema;
    private int desde;
    private int hasta;
    private boolean seleccionados;
    private boolean conDetalles;
    private long idReembolso;
    private String exportado;
    private String rucEmpresa;
    private Long idRolSesion;
    //
    private List<String> clavesSeleccionadas;

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Long getIdUsuarioCarga() {
        return idUsuarioCarga;
    }

    public void setIdUsuarioCarga(Long idUsuarioCarga) {
        this.idUsuarioCarga = idUsuarioCarga;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getEstadoSistema() {
        return estadoSistema;
    }

    public void setEstadoSistema(String estadoSistema) {
        this.estadoSistema = estadoSistema;
    }

    public int getDesde() {
        return desde;
    }

    public void setDesde(int desde) {
        this.desde = desde;
    }

    public int getHasta() {
        return hasta;
    }

    public void setHasta(int hasta) {
        this.hasta = hasta;
    }

    public boolean isSeleccionados() {
        return seleccionados;
    }

    public void setSeleccionados(boolean seleccionados) {
        this.seleccionados = seleccionados;
    }

    public boolean isConDetalles() {
        return conDetalles;
    }

    public void setConDetalles(boolean conDetalles) {
        this.conDetalles = conDetalles;
    }

    public long getIdReembolso() {
        return idReembolso;
    }

    public void setIdReembolso(long idReembolso) {
        this.idReembolso = idReembolso;
    }

    public String getExportado() {
        return exportado;
    }

    public void setExportado(String exportado) {
        this.exportado = exportado;
    }

    public String getRucEmpresa() {
        return rucEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        this.rucEmpresa = rucEmpresa;
    }

    public Long getIdRolSesion() {
        return idRolSesion;
    }

    public void setIdRolSesion(Long idRolSesion) {
        this.idRolSesion = idRolSesion;
    }

    public List<String> getClavesSeleccionadas() {
        return clavesSeleccionadas;
    }

    public void setClavesSeleccionadas(List<String> clavesSeleccionadas) {
        this.clavesSeleccionadas = clavesSeleccionadas;
    }

    @Override
    public String toString() {
        return "BuscadorArchivoXml{" + "fechaInicio=" + fechaInicio + ", fechaFinal=" + fechaFinal + ", idUsuarioCarga=" + idUsuarioCarga + ", claveAcceso=" + claveAcceso + ", ruc=" + ruc + ", tipoDocumento=" + tipoDocumento + ", estadoSistema=" + estadoSistema + ", desde=" + desde + ", hasta=" + hasta + ", seleccionados=" + seleccionados + ", conDetalles=" + conDetalles + ", idReembolso=" + idReembolso + ", exportado=" + exportado + ", rucEmpresa=" + rucEmpresa + ", idRolSesion=" + idRolSesion + '}';
    }
    
    
}
