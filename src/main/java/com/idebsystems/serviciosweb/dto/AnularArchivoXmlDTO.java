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
public class AnularArchivoXmlDTO {
    
    private String archivoAnularB64;
    private String usuarioAnula;

    public String getArchivoAnularB64() {
        return archivoAnularB64;
    }

    public void setArchivoAnularB64(String archivoAnularB64) {
        this.archivoAnularB64 = archivoAnularB64;
    }

    public String getUsuarioAnula() {
        return usuarioAnula;
    }

    public void setUsuarioAnula(String usuarioAnula) {
        this.usuarioAnula = usuarioAnula;
    }

    
}
