/*
 * Proyecto API Rest (ServiciosWebGestionCompras)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.DocumentoReembolsosDTO;
import com.idebsystems.serviciosweb.dto.ReporteDTO;
import com.idebsystems.serviciosweb.servicio.ReporteServicio;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jorge
 */
@Path("/reportes")
public class ReporteREST {
    
    private static final Logger LOGGER = Logger.getLogger(ReporteREST.class.getName());

    private final ReporteServicio service = new ReporteServicio();
    
    @GET
    @Path("/generarReportePdf")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ReporteDTO generarReportePdf(
            @QueryParam(value = "reporte") String reporte,
            @QueryParam(value = "id") Long id
    ) throws Exception {
        try {
            LOGGER.log(Level.INFO, "reporte: {0}", reporte);
            LOGGER.log(Level.INFO, "id: {0}", id);
            //buscar en la bdd los roles
            return service.generarReportePdf(reporte, id);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @GET
    @Path("/generarReporteXls")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ReporteDTO generarReporteXls(
            @QueryParam(value = "reporte") String reporte,
            @QueryParam(value = "fechaIni") String fechaIni,
            @QueryParam(value = "fechaFin") String fechaFin
    ) throws Exception {
        try {
            LOGGER.log(Level.INFO, "reporte: {0}", reporte);
            LOGGER.log(Level.INFO, "fechaIni: {0}", fechaIni);
            LOGGER.log(Level.INFO, "fechaFin: {0}", fechaFin);
            //buscar en la bdd los roles
            return service.generarReporteXls(reporte, fechaIni, fechaFin);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    @GET
    @Path("/reporteFirma")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ReporteDTO generarReporteFirma(
            @QueryParam(value = "reporte") String reporte,
            @QueryParam(value = "ids") String ids,
            @QueryParam(value = "idUsuario") Long idUsuario,
            @QueryParam(value = "tiposGasto") String tiposGasto,
            @QueryParam(value = "tipoReembolso") String tipoReembolso,
            @QueryParam(value = "idAprobador") long idAprobador,
            @QueryParam(value = "listaAsistentes") String listaAsistentes,
            
            @QueryParam(value = "motivoViaje") String motivoViaje,
            @QueryParam(value = "periodoViaje") String periodoViaje,
            @QueryParam(value = "lugarViaje") String lugarViaje,
            @QueryParam(value = "fondoEntregado") double fondoEntregado,
            @QueryParam(value = "observaciones") String observaciones,
            @QueryParam(value = "seleccion") String seleccion,
            @QueryParam(value = "claveFirma") String claveFirma,
            @QueryParam(value = "numeroRC") String numeroRC
    ) throws Exception {
        try {
            LOGGER.log(Level.INFO, "reporte: {0}", reporte);
            LOGGER.log(Level.INFO, "ids: {0}", ids);
            LOGGER.log(Level.INFO, "tiposGasto: {0}", tiposGasto);
            LOGGER.log(Level.INFO, "tipoReembolso: {0}", tipoReembolso);
            LOGGER.log(Level.INFO, "idUsuario: {0}", idUsuario);
            LOGGER.log(Level.INFO, "idAprobador: {0}", idAprobador);
            LOGGER.log(Level.INFO, "claveFirma: {0}", claveFirma);
            LOGGER.log(Level.INFO, "numeroRC: {0}", numeroRC);
            
            DocumentoReembolsosDTO reembDto = new DocumentoReembolsosDTO();
            reembDto.setMotivoViaje(motivoViaje);
            reembDto.setPeriodoViaje(periodoViaje);
            reembDto.setLugarViaje(lugarViaje);
            reembDto.setFondoEntregado(new BigDecimal(fondoEntregado));
            reembDto.setObservaciones(observaciones);
            reembDto.setSeleccion(seleccion);
            reembDto.setNumeroRC(numeroRC);
                    
            //buscar en la bdd los roles
            return service.generarReporteFirma(reporte, ids, tiposGasto, tipoReembolso, idUsuario, idAprobador, listaAsistentes, reembDto, claveFirma);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
