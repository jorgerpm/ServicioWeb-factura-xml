/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.MenuDTO;
import com.idebsystems.serviciosweb.servicio.MenuServicio;
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
@Path("/menu")
public class MenuREST {
    private static final Logger LOGGER = Logger.getLogger(MenuREST.class.getName());

    private final MenuServicio service = new MenuServicio();
    
    @GET
    @Path("/listarMenus")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<MenuDTO> listarMenus() throws Exception {
        try {
            //buscar en la bdd los roles
            return service.listarMenus();
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/guardarMenu")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public MenuDTO guardarMenu(MenuDTO menuDto) throws Exception {
        try {
            //guardar en la bdd el rol
            return service.guardarMenu(menuDto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @GET
    @Path("/listarMenusPorRol")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<MenuDTO> listarMenus(@QueryParam(value = "idRol") long idRol1) throws Exception {
        try {
            //buscar en la bdd los roles
            return service.listarMenusPorRol(idRol1);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
