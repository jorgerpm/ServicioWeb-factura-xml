/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.FirmaDigitalDTO;
import com.idebsystems.serviciosweb.servicio.FirmaDigitalServicio;
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
@Path("/firmadigital")
public class FirmaDigitalREST {
    
    private static final Logger LOGGER = Logger.getLogger(FirmaDigitalREST.class.getName());

    private final FirmaDigitalServicio service = new FirmaDigitalServicio();
    
    @GET
    @Path("/listarFirmas")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<FirmaDigitalDTO> listarFirmas(
        @QueryParam(value = "idUsuario") long idUsuario,
        @QueryParam(value = "idRol") long idRol
    ) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", idUsuario);
            //buscar en la bdd los FirmaDigital
            return service.listarFirmas(idUsuario, idRol);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/guardarFirmaDigital")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public FirmaDigitalDTO guardarFirmaDigital(FirmaDigitalDTO dto) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", dto);
            //guardar en la bdd el FirmaDigital
            return service.guardarFirmaDigital(dto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @GET
    @Path("/getFirmaUsuario")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public FirmaDigitalDTO getFirmaActivaPorIdUsuario(@QueryParam(value = "idUsuario") long idUsuario) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", idUsuario);
            //buscar en la bdd los FirmaDigital
            return service.getFirmaActivaPorIdUsuario(idUsuario, false);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @GET
    @Path("/solicitarClaveFirma")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String solicitarClaveFirma(@QueryParam(value = "idUsuario") long idUsuario) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", idUsuario);
            
            boolean f= service.solicitarClaveFirma(idUsuario);
            LOGGER.log(Level.INFO, "respuesta : {0}", f);
            return Boolean.toString(f);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            return "false";
        }
    }
}
