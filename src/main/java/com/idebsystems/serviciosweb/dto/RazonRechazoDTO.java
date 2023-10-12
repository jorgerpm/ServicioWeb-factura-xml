/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb.dto;

/**
 *
 * @author israe
 */
public class RazonRechazoDTO {
    
    private long id;
    private String razon;
    private long idEstado;
    private long idUsuarioModifica;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(long idEstado) {
        this.idEstado = idEstado;
    }

    public long getIdUsuarioModifica() {
        return idUsuarioModifica;
    }

    public void setIdUsuarioModifica(long idUsuarioModifica) {
        this.idUsuarioModifica = idUsuarioModifica;
    }

    @Override
    public String toString() {
        return "RazonRechazoDTO{" + "id=" + id + ", razon=" + razon + ", idEstado=" + idEstado + ", idUsuarioModifica=" + idUsuarioModifica + '}';
    }


}
