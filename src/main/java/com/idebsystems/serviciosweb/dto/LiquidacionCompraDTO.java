/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. (C) 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dto;

/**
 *
 * @author jorge
 */
public class LiquidacionCompraDTO {
 
    private Long id;
    private Long idReembolso;
    private String numero;
    private String pathArchivo;
    private String estado;
    private Long idUsuarioModifica;
    //
    private String archivoBase64;
    private String claveFirma;
    private String respuesta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdReembolso() {
        return idReembolso;
    }

    public void setIdReembolso(Long idReembolso) {
        this.idReembolso = idReembolso;
    }

    public String getPathArchivo() {
        return pathArchivo;
    }

    public void setPathArchivo(String pathArchivo) {
        this.pathArchivo = pathArchivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getIdUsuarioModifica() {
        return idUsuarioModifica;
    }

    public void setIdUsuarioModifica(Long idUsuarioModifica) {
        this.idUsuarioModifica = idUsuarioModifica;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getArchivoBase64() {
        return archivoBase64;
    }

    public void setArchivoBase64(String archivoBase64) {
        this.archivoBase64 = archivoBase64;
    }

    public String getClaveFirma() {
        return claveFirma;
    }

    public void setClaveFirma(String claveFirma) {
        this.claveFirma = claveFirma;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "LiquidacionCompraDTO{" + "id=" + id + ", idReembolso=" + idReembolso + ", numero=" + numero + ", pathArchivo=" + pathArchivo + ", estado=" + estado + ", idUsuarioModifica=" + idUsuarioModifica + ", claveFirma=" + claveFirma + ", respuesta=" + respuesta + '}';
    }
    
    
}
