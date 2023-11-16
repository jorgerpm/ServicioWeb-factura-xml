/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dto;

import java.util.Date;
import java.util.List;

/**
 *
 * @author jorge
 */
public class FacturaFisicaDTO {

    private Long id;

    private String numeroFactura;
    private Date fechaFactura;
    private Long idUsuarioCarga;

    private String rucProveedor;
    private String proveedor;
    private String direccionProveedor;

    private String tipoIdentCliente;
    private String identificacionCliente;
    private String cliente;
    private String direccionCliente;

    private String formaPago;

    //los totales
    private String descuento;
    private String subtotalSinIva;
    private String subtotal;
    private String porcentajeIva;
    private String iva;
    private String total;
    private String tipoIva;
    private String tarifa;
    
    private String tipoDocumento;

    //los detalles
    private List<FacturaFisicaDetalleDTO> listaDetalles;
    private String respuesta;
    private String pathArchivo;
    private String nombreArchivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public Long getIdUsuarioCarga() {
        return idUsuarioCarga;
    }

    public void setIdUsuarioCarga(Long idUsuarioCarga) {
        this.idUsuarioCarga = idUsuarioCarga;
    }

    public String getRucProveedor() {
        return rucProveedor;
    }

    public void setRucProveedor(String rucProveedor) {
        this.rucProveedor = rucProveedor;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getDireccionProveedor() {
        return direccionProveedor;
    }

    public void setDireccionProveedor(String direccionProveedor) {
        this.direccionProveedor = direccionProveedor;
    }

    public String getTipoIdentCliente() {
        return tipoIdentCliente;
    }

    public void setTipoIdentCliente(String tipoIdentCliente) {
        this.tipoIdentCliente = tipoIdentCliente;
    }

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getSubtotalSinIva() {
        return subtotalSinIva;
    }

    public void setSubtotalSinIva(String subtotalSinIva) {
        this.subtotalSinIva = subtotalSinIva;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(String porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<FacturaFisicaDetalleDTO> getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(List<FacturaFisicaDetalleDTO> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }

    public String getTipoIva() {
        return tipoIva;
    }

    public void setTipoIva(String tipoIva) {
        this.tipoIva = tipoIva;
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
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

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    @Override
    public String toString() {
        return "FacturaFisicaDTO{" + "id=" + id + ", numeroFactura=" + numeroFactura + ", fechaFactura=" + fechaFactura + ", idUsuarioCarga=" + idUsuarioCarga + ", rucProveedor=" + rucProveedor + ", proveedor=" + proveedor + ", direccionProveedor=" + direccionProveedor + ", tipoIdentCliente=" + tipoIdentCliente + ", identificacionCliente=" + identificacionCliente + ", cliente=" + cliente + ", direccionCliente=" + direccionCliente + ", formaPago=" + formaPago + ", descuento=" + descuento + ", subtotalSinIva=" + subtotalSinIva + ", subtotal=" + subtotal + ", porcentajeIva=" + porcentajeIva + ", iva=" + iva + ", total=" + total + '}';
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    
    
}
