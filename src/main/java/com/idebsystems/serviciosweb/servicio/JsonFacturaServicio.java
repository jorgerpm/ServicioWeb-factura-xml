/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.google.gson.Gson;
import com.idebsystems.serviciosweb.dto.FacturaFisicaDTO;
import com.idebsystems.serviciosweb.dto.FacturaFisicaDetalleDTO;
import com.idebsystems.serviciosweb.dto.json.Detalle;
import com.idebsystems.serviciosweb.dto.json.Detalles;
import com.idebsystems.serviciosweb.dto.json.Factura;
import com.idebsystems.serviciosweb.dto.json.Impuesto;
import com.idebsystems.serviciosweb.dto.json.Impuestos;
import com.idebsystems.serviciosweb.dto.json.InfoFactura;
import com.idebsystems.serviciosweb.dto.json.InfoTributaria;
import com.idebsystems.serviciosweb.dto.json.JsonAuxiliar;
import com.idebsystems.serviciosweb.dto.json.Pago;
import com.idebsystems.serviciosweb.dto.json.Pagos;
import com.idebsystems.serviciosweb.dto.json.TotalConImpuestos;
import com.idebsystems.serviciosweb.dto.json.TotalImpuesto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorge
 */
public class JsonFacturaServicio {

    private static final Logger LOGGER = Logger.getLogger(JsonFacturaServicio.class.getName());
    
    public String generarJsonFactura(FacturaFisicaDTO dto) throws Exception {
        try {
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            Factura factura = new Factura();
            
            InfoTributaria infoTributaria = new InfoTributaria();
            infoTributaria.setAmbiente(2);
            infoTributaria.setClaveAcceso(dto.getRucProveedor() + dto.getNumeroFactura().replace("-", ""));
            infoTributaria.setCodDoc(dto.getTipoDocumento());
//            infoTributaria.setContribuyenteRimpe(contribuyenteRimpe);
            infoTributaria.setDirMatriz(dto.getDireccionProveedor());
            infoTributaria.setEstab(dto.getNumeroFactura().split("-")[0]);
            infoTributaria.setPtoEmi(dto.getNumeroFactura().split("-")[1]);
            infoTributaria.setRazonSocial(dto.getProveedor());
            infoTributaria.setRuc(dto.getRucProveedor());
            infoTributaria.setSecuencial(dto.getNumeroFactura().split("-")[2]);
            infoTributaria.setTipoEmision(1);
            
            factura.setInfoTributaria(infoTributaria);
            
            InfoFactura infoFactura = new InfoFactura();
//            infoFactura.setContribuyenteEspecial(Integer.BYTES);
//            infoFactura.setDirEstablecimiento(dirEstablecimiento);
            infoFactura.setFechaEmision(sdf.format(dto.getFechaFactura()));
//            infoFactura.setFleteInternacional(Double.MIN_NORMAL);
//            infoFactura.setGastosAduaneros(Double.MAX_VALUE);
//            infoFactura.setGastosTransporteOtros(Double.POSITIVE_INFINITY);
            infoFactura.setIdentificacionComprador(dto.getIdentificacionCliente());
            infoFactura.setImporteTotal(Double.parseDouble(dto.getTotal()));
            infoFactura.setMoneda("DOLAR");
            infoFactura.setObligadoContabilidad("SI");
            
            List<Pago> pagos = new ArrayList<>();
            Pago pago = new Pago();
            pago.setFormaPago(dto.getFormaPago());
            pago.setPlazo(1);
            pago.setTotal(Double.parseDouble(dto.getTotal()));
            pago.setUnidadTiempo("");
            
            pagos.add(pago);
            
            Pagos objpago = new Pagos();
            objpago.setPago(pagos);
            
            infoFactura.setPagos(objpago);
            
//            infoFactura.setPropina(Double.NaN);
            infoFactura.setRazonSocialComprador(dto.getCliente());
//            infoFactura.setSeguroInternacional(Double.NEGATIVE_INFINITY);
//            infoFactura.setSeguroInternacional(Double.NEGATIVE_INFINITY);
            infoFactura.setTipoIdentificacionComprador(dto.getTipoIdentCliente());
//            infoFactura.setTotalBaseImponibleReembolso(Double.POSITIVE_INFINITY);
//            infoFactura.setTotalComprobantesReembolso(Double.MIN_NORMAL);
            
            List<TotalImpuesto> listsTotalConImpuestos = new ArrayList<>();
            //verfificar si tiene con iva y si tiene sin iva
            if(Objects.nonNull(dto.getSubtotalSinIva()) && !dto.getSubtotalSinIva().isEmpty() && Double.parseDouble(dto.getSubtotalSinIva()) > 0 ){
                //es iva 0%
                TotalImpuesto totalImpuesto = new TotalImpuesto();
                totalImpuesto.setBaseImponible(Double.parseDouble(dto.getSubtotalSinIva()));
                totalImpuesto.setCodigo(2);
                totalImpuesto.setCodigoPorcentaje("0");
                totalImpuesto.setTarifa("0");
                totalImpuesto.setValor(0D);
//                totalImpuesto.setValorDevolucionIva(Double.MAX_VALUE);
                listsTotalConImpuestos.add(totalImpuesto);
            }
            if(Objects.nonNull(dto.getSubtotal()) && !dto.getSubtotal().isEmpty() && Double.parseDouble(dto.getSubtotal()) > 0 ){
                //es iva mayor a 0%
                TotalImpuesto totalImpuesto = new TotalImpuesto();
                totalImpuesto.setBaseImponible(Double.parseDouble(dto.getSubtotal()));
                totalImpuesto.setCodigo(2);
                totalImpuesto.setCodigoPorcentaje(dto.getTipoIva());
                totalImpuesto.setTarifa(dto.getTarifa());
                totalImpuesto.setValor(Double.parseDouble(dto.getIva()));
//                totalImpuesto.setValorDevolucionIva(Double.MAX_VALUE);
                listsTotalConImpuestos.add(totalImpuesto);
            }
            
            TotalConImpuestos totalConImpuestos = new TotalConImpuestos();
            totalConImpuestos.setTotalImpuesto(listsTotalConImpuestos);
            infoFactura.setTotalConImpuestos(totalConImpuestos);
            
            infoFactura.setTotalDescuento(Double.parseDouble(dto.getDescuento()));
//            infoFactura.setTotalImpuestoReembolso(Double.MIN_NORMAL);
            infoFactura.setTotalSinImpuestos(Double.parseDouble(dto.getSubtotal()) + Double.parseDouble(dto.getSubtotalSinIva()));
//            infoFactura.setTotalSubsidio(Double.NaN);
//            infoFactura.setValorRetIva(Double.NaN);
//            infoFactura.setValorRetRenta(Double.NaN);
            
            factura.setInfoFactura(infoFactura);
            
            List<Detalle> listaDetalles = new ArrayList<>();
            
            for(FacturaFisicaDetalleDTO obj : dto.getListaDetalles()){
                Detalle detalle = new Detalle();
                detalle.setCantidad(obj.getCantidad());
//                detalle.setCodigoAuxiliar(codigoAuxiliar);
                detalle.setCodigoPrincipal(obj.getDescripcion().substring(0,3));
                detalle.setDescripcion(obj.getDescripcion());
                detalle.setDescuento(Double.parseDouble(obj.getDescuento()));

                List<Impuesto> listaImpuestos = new ArrayList<>();
                Impuesto impuesto = new Impuesto();
                impuesto.setBaseImponible(Double.parseDouble(obj.getValorTotal()));
                impuesto.setCodigo("2");
                impuesto.setCodigoPorcentaje("2");
                impuesto.setTarifa(12);
                impuesto.setValor(Double.parseDouble(obj.getValorTotal()) * 12/100);
                
                listaImpuestos.add(impuesto);

                Impuestos impuestos = new Impuestos();
                impuestos.setImpuesto(listaImpuestos);
                detalle.setImpuestos(impuestos);

                detalle.setPrecioTotalSinImpuesto(Double.parseDouble(obj.getValorTotal()));
                detalle.setPrecioUnitario(Double.parseDouble(obj.getValorUnitario()));
                
                listaDetalles.add(detalle);
            }
            
            Detalles detalles = new Detalles();
            detalles.setDetalle(listaDetalles);
            factura.setDetalles(detalles);
            
            JsonAuxiliar aux = new JsonAuxiliar();
            aux.setFactura(factura);
            
            Gson gson = new Gson();
            
            String componente = gson.toJson(aux);
            
            return componente;

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
}
