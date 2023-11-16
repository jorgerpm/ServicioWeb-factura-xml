/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.TipoReembolsoDTO;
import com.idebsystems.serviciosweb.servicio.TipoReembolsoServicio;
import java.util.List;
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
@Path("/tipoReembolso")
public class TipoReembolsoREST {
    
    private static final Logger LOGGER = Logger.getLogger(TipoReembolsoREST.class.getName());
    
    private final TipoReembolsoServicio service = new TipoReembolsoServicio();
    
    @GET
    @Path("/listarTipoReembolso")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<TipoReembolsoDTO> listarTipoReembolso(@QueryParam(value = "esPrincipal") String esPrincipal) throws Exception {
        try {
            //buscar en la bdd los parametroes
            LOGGER.log(Level.INFO, "esPrincipal: {0}", esPrincipal);
            return service.listarTipoReembolso(esPrincipal);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/guardarTipoReembolso")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public TipoReembolsoDTO guardarTipoReembolso(TipoReembolsoDTO dto) throws Exception {
        try {
            //guardar en la bdd el parametro
            return service.guardarTipoReembolso(dto);
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
