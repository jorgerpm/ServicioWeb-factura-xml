/*
 * Proyecto API Rest (ServiciosWebGestionCompras)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dto;

/**
 *
 * @author jorge
 */
public class ReporteDTO {
    
    private String reporteBase64;
    private String pathArchivo;
    private String respuesta;
    
//    public ReporteDTO(String respuesta){this.respuesta = respuesta;}

    public String getReporteBase64() {
        return reporteBase64;
    }

    public void setReporteBase64(String reporteBase64) {
        this.reporteBase64 = reporteBase64;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getPathArchivo() {
        return pathArchivo;
    }

    public void setPathArchivo(String pathArchivo) {
        this.pathArchivo = pathArchivo;
    }

    @Override
    public String toString() {
        return "ReporteDTO{" + "pathArchivo=" + pathArchivo + ", respuesta=" + respuesta + '}';
    }
    
    
    
}
