/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dto;

/**
 *
 * @author jorge
 */
public class ArchivoSriDTO {

    private Long id;
    private String comprobante;
    private String claveAcceso;
    private String numeroDocumento;
//    private String razonSocial;
    private long idUsuarioCarga;
    private String respuesta;
    private String fileBase64;
    private String rideBase64;
    private String pathArchivos;
    private String estadoSistema;

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public long getIdUsuarioCarga() {
        return idUsuarioCarga;
    }

    public void setIdUsuarioCarga(long idUsuarioCarga) {
        this.idUsuarioCarga = idUsuarioCarga;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }

//    public String getRazonSocial() {
//        return razonSocial;
//    }
//
//    public void setRazonSocial(String razonSocial) {
//        this.razonSocial = razonSocial;
//    }

    public String getPathArchivos() {
        return pathArchivos;
    }

    public void setPathArchivos(String pathArchivos) {
        this.pathArchivos = pathArchivos;
    }

    public String getEstadoSistema() {
        return estadoSistema;
    }

    public void setEstadoSistema(String estadoSistema) {
        this.estadoSistema = estadoSistema;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRideBase64() {
        return rideBase64;
    }

    public void setRideBase64(String rideBase64) {
        this.rideBase64 = rideBase64;
    }

    @Override
    public String toString() {
        return "ArchivoSriDTO{" + "comprobante=" + comprobante + ", claveAcceso=" + claveAcceso + ", numeroDocumento=" + numeroDocumento + ", idUsuarioCarga=" + idUsuarioCarga + '}';
    }

}
