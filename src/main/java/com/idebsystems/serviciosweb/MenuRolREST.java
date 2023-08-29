/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.MenuRolDTO;
import com.idebsystems.serviciosweb.servicio.MenuRolServicio;
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
@Path("/menuRol")
public class MenuRolREST {
    private static final Logger LOGGER = Logger.getLogger(MenuRolREST.class.getName());

    private final MenuRolServicio service = new MenuRolServicio();
    
    @GET
    @Path("/listarMenuRoles")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<MenuRolDTO> listarMenuRoles() throws Exception {
        try {
            //buscar en la bdd los roles
            return service.listarMenuRoles();
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/guardarMenuRol")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public MenuRolDTO guardarMenuRol(MenuRolDTO menuRolDto) throws Exception {
        try {
            //guardar en la bdd el rol
            return service.guardarMenuRol(menuRolDto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @GET
    @Path("/listarMenuRolPorRol")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<MenuRolDTO> listarMenuRolesPorRol(@QueryParam(value = "idRol") long idRol) throws Exception {
        try {
            //buscar en la bdd los roles
            return service.listarMenuRolPorRol(idRol);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/eliminarMenuRolPorRol")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String eliminarMenuRolPorRol(long idRol) throws Exception {
        try {
            //buscar en la bdd los menuRoles
            service.eliminarMenuRolPorRol(idRol);
            return "eliminado";
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/guardarPermisos")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String guardarPermisos(List<MenuRolDTO> menuRolDtoLista) throws Exception {
        try {
            //buscar en la bdd los menuRoles
            service.guardarPermisos(menuRolDtoLista);
            return "insertado";
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
