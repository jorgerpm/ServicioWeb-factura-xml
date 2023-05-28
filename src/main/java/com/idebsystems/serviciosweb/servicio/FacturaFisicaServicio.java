/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.ArchivoXmlDAO;
import com.idebsystems.serviciosweb.dao.FacturaFisicaDAO;
import com.idebsystems.serviciosweb.dto.FacturaFisicaDTO;
import com.idebsystems.serviciosweb.entities.ArchivoXml;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorge
 */
public class FacturaFisicaServicio {
    
    private static final Logger LOGGER = Logger.getLogger(FacturaFisicaServicio.class.getName());
    
    private final FacturaFisicaDAO dao = new FacturaFisicaDAO();
    
    private static final ArchivoXmlDAO xmlDao = new ArchivoXmlDAO();
    
    public FacturaFisicaDTO guardarFacturaFisica(FacturaFisicaDTO dto) throws Exception {
        try{
            //verificar si el numero de factura ya no esta registrada, es numero de factura y el proveeedor.
            //la clave de acceso se arma asi, (rucproveedor + numerofactura) todo sin guiones
            String claveacceso = dto.getRucProveedor() + dto.getNumeroFactura().replace("-", "");
            ArchivoXml archivoExiste = xmlDao.getArchivoXmlPorClaveAcceso(claveacceso);
            
            if(Objects.nonNull(archivoExiste)){
                dto.setId(0L);
                dto.setRespuesta("El n\u00famero de factura ingresado ya existe para este proveedor.");
                return dto;
            }
            
//generar el json de la factura. para guardar en el comprobante
            JsonFacturaServicio jsonSrv = new JsonFacturaServicio();
            String comprobante = jsonSrv.generarJsonFactura(dto);


            ArchivoXml xml = new ArchivoXml();
            xml.setEsFisica(true);
            xml.setAmbiente("PRODUCCION");
            xml.setClaveAcceso(claveacceso);
//            xml.setCodigoJDProveedor(codigoJDProveedor);
            xml.setComprobante(comprobante);
            xml.setEstadoSistema("CARGADO");
            xml.setEstadoSri("AUTORIZADO");
//            xml.setExportado(false);
//            xml.setFechaAnula(fechaAnula);
            xml.setFechaAutorizacion(dto.getFechaFactura());
            xml.setFechaCarga(new Date());
            xml.setFechaEmision(dto.getFechaFactura());
//            xml.setId(Long.MIN_VALUE);
            xml.setIdUsuarioCarga(dto.getIdUsuarioCarga());
//            xml.setNombreArchivoPdf(nombreArchivoPdf);
//            xml.setNombreArchivoXml(nombreArchivoXml);
            xml.setNumeroAutorizacion(claveacceso);
//            xml.setRazonAnulacion(razonAnulacion);
            xml.setTipoDocumento(dto.getTipoDocumento());
//            xml.setTipoGasto(tipoGasto);
//            xml.setUrlArchivo(urlArchivo);
//            xml.setUsuarioAnula(usuarioAnula);
            
            xmlDao.guardarDatosArchivo(xml);
            
            dto.setId(xml.getId());
            dto.setRespuesta("OK");
            
            return dto;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
