/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.RespuestaDTO;
import com.idebsystems.serviciosweb.dto.UsuarioDTO;
import com.idebsystems.serviciosweb.servicio.CorreoServicio;
import com.idebsystems.serviciosweb.servicio.UsuarioServicio;
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
@Path("/usuario")
public class UsuarioREST {
    private static final Logger LOGGER = Logger.getLogger(UsuarioREST.class.getName());

    private final UsuarioServicio service = new UsuarioServicio();
    
    @POST
    @Path("/loginSistema")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public UsuarioDTO loginSistema(UsuarioDTO userDto) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", userDto);
            //buscar en la bdd el usuario
            return service.loginSistema(userDto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @GET
    @Path("/listarUsuarios")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<UsuarioDTO> listarUsuarios() throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}");
            //buscar en la bdd los roles
            return service.listarUsuarios();
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/guardarUsuario")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public UsuarioDTO guardarRol(UsuarioDTO usuarioDto) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}");
            //guardar en la bdd el rol
            return service.guardarUsuario(usuarioDto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @GET
    @Path("recuperarClave")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public RespuestaDTO recuperarClave(@QueryParam(value = "correo") String correo){
        try{
            CorreoServicio srvcorreo = new CorreoServicio();
            String resp = srvcorreo.enviarUrlNuevaClave(correo);
            return new RespuestaDTO(resp);
        }catch(Exception exc){
            return new RespuestaDTO("ERROR".concat(exc.getMessage()));
        }
    }
    
    
    @GET
    @Path("/listarUsuariosPorRol")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<UsuarioDTO> listarUsuariosPorRol(@QueryParam(value = "idRol") long idRol) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}");
            //buscar en la bdd los roles
            return service.listarUsuariosPorRol(idRol);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
