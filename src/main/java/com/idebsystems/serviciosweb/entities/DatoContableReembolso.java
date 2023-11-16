/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jorge
 */
@Entity
public class DatoContableReembolso implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long idReembolso;
    //datos para contabilidad
    private String justificativos;
    private String batchIngresoLiquidacion;
    private String batchDocumentoInterno;
    private String p3;
    private String p4;
    private String p5;
    private String phne;
    private String cruce1;
    private String cruce2;
    private String tipoDocumento;
    private String numeroDocumento;
    private String numeroRetencion;
    private long idUsuarioModifica;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModifica;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJustificativos() {
        return justificativos;
    }

    public void setJustificativos(String justificativos) {
        this.justificativos = justificativos;
    }

    public String getBatchIngresoLiquidacion() {
        return batchIngresoLiquidacion;
    }

    public void setBatchIngresoLiquidacion(String batchIngresoLiquidacion) {
        this.batchIngresoLiquidacion = batchIngresoLiquidacion;
    }

    public String getBatchDocumentoInterno() {
        return batchDocumentoInterno;
    }

    public void setBatchDocumentoInterno(String batchDocumentoInterno) {
        this.batchDocumentoInterno = batchDocumentoInterno;
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public String getP4() {
        return p4;
    }

    public void setP4(String p4) {
        this.p4 = p4;
    }

    public String getP5() {
        return p5;
    }

    public void setP5(String p5) {
        this.p5 = p5;
    }

    public String getPhne() {
        return phne;
    }

    public void setPhne(String phne) {
        this.phne = phne;
    }

    public String getCruce1() {
        return cruce1;
    }

    public void setCruce1(String cruce1) {
        this.cruce1 = cruce1;
    }

    public String getCruce2() {
        return cruce2;
    }

    public void setCruce2(String cruce2) {
        this.cruce2 = cruce2;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNumeroRetencion() {
        return numeroRetencion;
    }

    public void setNumeroRetencion(String numeroRetencion) {
        this.numeroRetencion = numeroRetencion;
    }

    public long getIdReembolso() {
        return idReembolso;
    }

    public void setIdReembolso(long idReembolso) {
        this.idReembolso = idReembolso;
    }    

    public long getIdUsuarioModifica() {
        return idUsuarioModifica;
    }

    public void setIdUsuarioModifica(long idUsuarioModifica) {
        this.idUsuarioModifica = idUsuarioModifica;
    }

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }
    
}
