/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dto.json;

/**
 *
 * @author jorge
 */
public class InfoFactura {

    private Pagos pagos;
    private TotalConImpuestos totalConImpuestos;

    private Double totalImpuestoReembolso;
    private String identificacionComprador;
    private String razonSocialComprador;
    private String direccionComprador;
    private Integer contribuyenteEspecial;
    private String obligadoContabilidad;
    private String fechaEmision;
    private String dirEstablecimiento;
    private Double propina;
    private Double seguroInternacional;
    private Double totalSubsidio;

    private Double gastosTransporteOtros;
    private Double valorRetIva;
    private String tipoIdentificacionComprador;
    private Double totalBaseImponibleReembolso;
    private Double totalComprobantesReembolso;
    private Double gastosAduaneros;
    private Double importeTotal;
    private Double totalDescuento;

    private Double valorRetRenta;
    private String moneda;
    private Double fleteInternacional;
    private Double totalSinImpuestos;

    public Pagos getPagos() {
        return pagos;
    }

    public void setPagos(Pagos pagos) {
        this.pagos = pagos;
    }

    public TotalConImpuestos getTotalConImpuestos() {
        return totalConImpuestos;
    }

    public void setTotalConImpuestos(TotalConImpuestos totalConImpuestos) {
        this.totalConImpuestos = totalConImpuestos;
    }
    
    public Double getTotalImpuestoReembolso() {
        return totalImpuestoReembolso;
    }

    public void setTotalImpuestoReembolso(Double totalImpuestoReembolso) {
        this.totalImpuestoReembolso = totalImpuestoReembolso;
    }

    public String getIdentificacionComprador() {
        return identificacionComprador;
    }

    public void setIdentificacionComprador(String identificacionComprador) {
        this.identificacionComprador = identificacionComprador;
    }

    public String getRazonSocialComprador() {
        return razonSocialComprador;
    }

    public void setRazonSocialComprador(String razonSocialComprador) {
        this.razonSocialComprador = razonSocialComprador;
    }

    public Integer getContribuyenteEspecial() {
        return contribuyenteEspecial;
    }

    public void setContribuyenteEspecial(Integer contribuyenteEspecial) {
        this.contribuyenteEspecial = contribuyenteEspecial;
    }

    public String getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(String obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getDirEstablecimiento() {
        return dirEstablecimiento;
    }

    public void setDirEstablecimiento(String dirEstablecimiento) {
        this.dirEstablecimiento = dirEstablecimiento;
    }

    public Double getPropina() {
        return propina;
    }

    public void setPropina(Double propina) {
        this.propina = propina;
    }

    public Double getSeguroInternacional() {
        return seguroInternacional;
    }

    public void setSeguroInternacional(Double seguroInternacional) {
        this.seguroInternacional = seguroInternacional;
    }

    public Double getTotalSubsidio() {
        return totalSubsidio;
    }

    public void setTotalSubsidio(Double totalSubsidio) {
        this.totalSubsidio = totalSubsidio;
    }

    public Double getGastosTransporteOtros() {
        return gastosTransporteOtros;
    }

    public void setGastosTransporteOtros(Double gastosTransporteOtros) {
        this.gastosTransporteOtros = gastosTransporteOtros;
    }

    public Double getValorRetIva() {
        return valorRetIva;
    }

    public void setValorRetIva(Double valorRetIva) {
        this.valorRetIva = valorRetIva;
    }

    public String getTipoIdentificacionComprador() {
        return tipoIdentificacionComprador;
    }

    public void setTipoIdentificacionComprador(String tipoIdentificacionComprador) {
        this.tipoIdentificacionComprador = tipoIdentificacionComprador;
    }

    public Double getTotalBaseImponibleReembolso() {
        return totalBaseImponibleReembolso;
    }

    public void setTotalBaseImponibleReembolso(Double totalBaseImponibleReembolso) {
        this.totalBaseImponibleReembolso = totalBaseImponibleReembolso;
    }

    public Double getTotalComprobantesReembolso() {
        return totalComprobantesReembolso;
    }

    public void setTotalComprobantesReembolso(Double totalComprobantesReembolso) {
        this.totalComprobantesReembolso = totalComprobantesReembolso;
    }

    public Double getGastosAduaneros() {
        return gastosAduaneros;
    }

    public void setGastosAduaneros(Double gastosAduaneros) {
        this.gastosAduaneros = gastosAduaneros;
    }

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public Double getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(Double totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public Double getValorRetRenta() {
        return valorRetRenta;
    }

    public void setValorRetRenta(Double valorRetRenta) {
        this.valorRetRenta = valorRetRenta;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Double getFleteInternacional() {
        return fleteInternacional;
    }

    public void setFleteInternacional(Double fleteInternacional) {
        this.fleteInternacional = fleteInternacional;
    }

    public Double getTotalSinImpuestos() {
        return totalSinImpuestos;
    }

    public void setTotalSinImpuestos(Double totalSinImpuestos) {
        this.totalSinImpuestos = totalSinImpuestos;
    }

    public String getDireccionComprador() {
        return direccionComprador;
    }

    public void setDireccionComprador(String direccionComprador) {
        this.direccionComprador = direccionComprador;
    }
    
    
}
