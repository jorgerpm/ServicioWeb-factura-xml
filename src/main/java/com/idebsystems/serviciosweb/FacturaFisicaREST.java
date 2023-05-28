/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.FacturaFisicaDTO;
import com.idebsystems.serviciosweb.servicio.FacturaFisicaServicio;
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
@Path("/facturafisica")
public class FacturaFisicaREST {
    
    private static final Logger LOGGER = Logger.getLogger(FacturaFisicaREST.class.getName());

    private final FacturaFisicaServicio service = new FacturaFisicaServicio();
    
    @POST
    @Path("/guardarFacturaFisica")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public FacturaFisicaDTO guardarFacturaFisica(FacturaFisicaDTO dto) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", dto);
            //guardar en la bdd el FirmaDigital
            return service.guardarFacturaFisica(dto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
}
