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
public class FirmaDigitalDTO {
    
    private Long id;
    private Long idUsuario;
    private UsuarioDTO usuario;
    private String archivo;
    private String clave;
    private Long fechaCaducaLong;
    private Integer tipoFirma;
    private Integer idEstado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Long getFechaCaducaLong() {
        return fechaCaducaLong;
    }

    public void setFechaCaducaLong(Long fechaCaducaLong) {
        this.fechaCaducaLong = fechaCaducaLong;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public Integer getTipoFirma() {
        return tipoFirma;
    }

    public void setTipoFirma(Integer tipoFirma) {
        this.tipoFirma = tipoFirma;
    }

    @Override
    public String toString() {
        return "FirmaDigitalDTO{" + "id=" + id + ", idUsuario=" + idUsuario + ", archivo=" + archivo + ", clave=" + clave + ", fechaCaducaLong=" + fechaCaducaLong + ", idEstado=" + idEstado + '}';
    }

    
}
