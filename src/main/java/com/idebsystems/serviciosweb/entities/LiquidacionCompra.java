/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. (C) 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jorge
 */
@Entity
public class LiquidacionCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idReembolso;
    private String numero;
    private String pathArchivo;
    private String estado;
    private Long idUsuarioModifica;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModifica;
    
    @JoinColumn(name="idReembolso", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private DocumentoReembolsos documentoReembolsos;

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

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }    

    public DocumentoReembolsos getDocumentoReembolsos() {
        return documentoReembolsos;
    }

    public void setDocumentoReembolsos(DocumentoReembolsos documentoReembolsos) {
        this.documentoReembolsos = documentoReembolsos;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
}
