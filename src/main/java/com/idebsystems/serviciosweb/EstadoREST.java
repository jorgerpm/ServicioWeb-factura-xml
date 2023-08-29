/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.EstadoDTO;
import com.idebsystems.serviciosweb.servicio.EstadoServicio;
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
@Path("/estado")
public class EstadoREST {
    private static final Logger LOGGER = Logger.getLogger(EstadoREST.class.getName());

    private final EstadoServicio service = new EstadoServicio();
    
    @GET
    @Path("/listarEstados")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<EstadoDTO> listarEstado() throws Exception {
        try {
            //buscar en la bdd los roles
            return service.listarEstado();
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/guardarEstado")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public EstadoDTO guardarEstado(EstadoDTO estadoDto) throws Exception {
        try {
            //guardar en la bdd el rol
            return service.guardarEstado(estadoDto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
