/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.EmpresaDTO;
import com.idebsystems.serviciosweb.servicio.EmpresaServicio;
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
@Path("/empresa")
public class EmpresaREST {
    
    private static final Logger LOGGER = Logger.getLogger(EmpresaREST.class.getName());

    private final EmpresaServicio service = new EmpresaServicio();
    
    @GET
    @Path("/listarEmpresas")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<EmpresaDTO> listarEmpresas() throws Exception {
        try {
            //buscar en la bdd los parametroes
            return service.listarEmpresas();
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/guardarEmpresa")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public EmpresaDTO guardarEmpresa(EmpresaDTO dto) throws Exception {
        try {
            //guardar en la bdd el parametro
            return service.guardarEmpresa(dto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
