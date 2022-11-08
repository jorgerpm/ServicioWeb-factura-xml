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
public class RespuestaDTO {
    private String respuesta;
    private Object dto;

    public RespuestaDTO(String respuesta){this.respuesta = respuesta;}
    
    public RespuestaDTO(String respuesta, Object dto){
        this.respuesta = respuesta;
        this.dto = dto;
    }
    
    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Object getDto() {
        return dto;
    }

    public void setDto(Object dto) {
        this.dto = dto;
    }
    
    
}
