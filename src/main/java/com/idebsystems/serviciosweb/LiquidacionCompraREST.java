/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. (C) 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.LiquidacionCompraDTO;
import com.idebsystems.serviciosweb.servicio.LiquidacionCompraServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jorge
 */
@Path("/liquidacioncompra")
public class LiquidacionCompraREST {
    
    private static final Logger LOGGER = Logger.getLogger(LiquidacionCompraREST.class.getName());

    private final LiquidacionCompraServicio service = new LiquidacionCompraServicio();
    
    @POST
    @Path("/cargarLiquidacion")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public LiquidacionCompraDTO cargarLiquidacion(LiquidacionCompraDTO dto) throws Exception {
        try {
//            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", dto.getClaveFirma());
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", dto);
            //guardar en la bdd el DocumentoReembolsos
            return service.guardarLiquidacion(dto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    @POST
    @Path("/firmarLiquidacion")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public LiquidacionCompraDTO firmarLiquidacion(LiquidacionCompraDTO dto) throws Exception {
        try {
//            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", dto.getClaveFirma());
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", dto);
            //guardar en la bdd el DocumentoReembolsos
            return service.firmarLiquidacion(dto, dto.getClaveFirma());
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
