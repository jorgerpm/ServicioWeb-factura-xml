/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.RolDTO;
import com.idebsystems.serviciosweb.servicio.RolServicio;
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
 * @author israe
 */
@Path("/rol")
public class RolREST {
    private static final Logger LOGGER = Logger.getLogger(RolREST.class.getName());

    private final RolServicio service = new RolServicio();
    
    @GET
    @Path("/listarRoles")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<RolDTO> listarRoles() throws Exception {
        try {
            //buscar en la bdd los roles
            return service.listarRoles();
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/guardarRol")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public RolDTO guardarRol(RolDTO rolDto) throws Exception {
        try {
            //guardar en la bdd el rol
            LOGGER.log(Level.INFO, "guarda rol: {0}", rolDto);
            return service.guardarRol(rolDto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    @GET
    @Path("/buscarRolPorId")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public RolDTO buscarRolPorId(@QueryParam(value = "idRol") Long idRol) throws Exception {
        try {
            //buscar en la bdd los roles
            return service.buscarRolPorId(idRol);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
