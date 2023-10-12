/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.RazonRechazoDTO;
import com.idebsystems.serviciosweb.servicio.RazonRechazoServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jorge
 */
@Path("/razonRechazo")
public class RazonRechazoREST {
    
    private static final Logger LOGGER = Logger.getLogger(RazonRechazoREST.class.getName());

    private final RazonRechazoServicio service = new RazonRechazoServicio();
    
    @GET
    @Path("/listarRazonesRechazo")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<RazonRechazoDTO> listarRazonesRechazo() throws Exception {
        try {
            //buscar en la bdd los parametroes
            return service.listarRazonesRechazo();
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/guardarRazonRechazo")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public RazonRechazoDTO guardarRazonRechazo(RazonRechazoDTO razonDto) throws Exception {
        try {
            //guardar en la bdd el parametro
            return service.guardarRazonRechazo(razonDto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
}
