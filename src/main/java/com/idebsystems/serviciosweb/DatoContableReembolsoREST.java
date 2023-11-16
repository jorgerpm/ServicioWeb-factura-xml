/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. (C) 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.DatoContableReembolsoDTO;
import com.idebsystems.serviciosweb.servicio.DatoContableReembolsoServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jorge
 */
@Path("/datocontablereembolso")
public class DatoContableReembolsoREST {
    
    private static final Logger LOGGER = Logger.getLogger(DatoContableReembolsoREST.class.getName());

    private final DatoContableReembolsoServicio service = new DatoContableReembolsoServicio();
    
    @POST
    @Path("/guardarDatosContabilidad")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public DatoContableReembolsoDTO guardarDatosContabilidad(DatoContableReembolsoDTO dto) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", dto);
            //guardar en la bdd el DocumentoReembolsos
            return service.guardarDatosContabilidad(dto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @GET
    @Path("/buscarDatoContableReembolso")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public DatoContableReembolsoDTO buscarDatoContableReembolso(@QueryParam(value = "idReembolso") long idReembolso) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", idReembolso);
            
            return service.getDatoPorIdReembolso(idReembolso);
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
}
