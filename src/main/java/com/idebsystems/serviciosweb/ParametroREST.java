/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.ParametroDTO;
import com.idebsystems.serviciosweb.servicio.ParametroServicio;
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
 * @author israe
 */
@Path("/parametro")
public class ParametroREST {
    private static final Logger LOGGER = Logger.getLogger(ParametroREST.class.getName());

    private final ParametroServicio service = new ParametroServicio();
    
    @GET
    @Path("/listarParametros")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<ParametroDTO> listarParametro() throws Exception {
        try {
            //buscar en la bdd los parametroes
            return service.listarParametros();
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/guardarParametro")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ParametroDTO guardarParametro(ParametroDTO parametroDto) throws Exception {
        try {
            //guardar en la bdd el parametro
            return service.guardarParametro(parametroDto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
