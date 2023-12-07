/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.DocumentoReembolsosDTO;
import com.idebsystems.serviciosweb.servicio.DocumentoReembolsosServicio;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@Path("/documentoreembolsos")
public class DocumentoReembolsosREST {
    
    private static final Logger LOGGER = Logger.getLogger(DocumentoReembolsosREST.class.getName());

    private final DocumentoReembolsosServicio service = new DocumentoReembolsosServicio();
    
    @GET
    @Path("/listarDocumentos")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<DocumentoReembolsosDTO> listarDocumentos(
            @QueryParam(value = "fechaInicio") String fechaInicio,
            @QueryParam(value = "fechaFinal") String fechaFinal,
            @QueryParam(value = "idUsuarioCarga") Long idUsuarioCarga,
            @QueryParam(value = "estadoSistema") String estadoSistema,
            @QueryParam(value = "desde") int desde,
            @QueryParam(value = "hasta") int hasta,
            @QueryParam(value = "numeroRC") String numeroRC,
            @QueryParam(value = "tipoReembolso") String tipoReembolso,
            @QueryParam(value = "numeroReembolso") String numeroReembolso,
            @QueryParam(value = "numeroLC") String numeroLC
    ) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", tipoReembolso);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInit = sdf.parse(fechaInicio);
            Date dateFin = sdf.parse(fechaFinal);
            //buscar en la bdd los DocumentoReembolsos
            return service.listarDocumentos(dateInit, dateFin, idUsuarioCarga, estadoSistema, desde, hasta, numeroRC, 
                    tipoReembolso, numeroReembolso, numeroLC);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/guardarDocumentoReembolsos")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public DocumentoReembolsosDTO guardarDocumentoReembolsos(DocumentoReembolsosDTO dto) throws Exception {
        try {
//            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", dto);
            //guardar en la bdd el DocumentoReembolsos
            return service.guardarDocumentoReembolsos(dto);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @GET
    @Path("/getDocumentosUsuario")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<DocumentoReembolsosDTO> getDocumentosPorIdUsuario(@QueryParam(value = "idUsuario") long idUsuario) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", idUsuario);
            //buscar en la bdd los DocumentoReembolsos
            return service.getDocumentosPorIdUsuario(idUsuario);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    @POST
    @Path("/aprobarDocumentoReembolsos")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public DocumentoReembolsosDTO aprobarDocumentoReembolsos(DocumentoReembolsosDTO dto) throws Exception {
        try {
//            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", dto.getClaveFirma());
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", dto.isTerceraFirma());
            //guardar en la bdd el DocumentoReembolsos
            return service.aprobarDocumentoReembolsos(dto, dto.getClaveFirma(), dto.isTerceraFirma());
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    @POST
    @Path("/enviarCorreoJustificacion")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public DocumentoReembolsosDTO enviarCorreoJustificacion(DocumentoReembolsosDTO dto) throws Exception {
        try {
//            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", dto);
            //guardar en la bdd el DocumentoReembolsos
            String resp = service.enviarCorreoJustificacion(dto);
            
            dto.setRespuesta(resp);
            return dto;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            dto.setRespuesta(exc.getMessage());
            return dto;
        }
    }
    
    @POST
    @Path("/cargarJustificacion")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public DocumentoReembolsosDTO cargarJustificacion(DocumentoReembolsosDTO dto) throws Exception {
        try {
//            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", dto);
            //guardar en la bdd el DocumentoReembolsos
            String resp = service.cargarJustificacion(dto);
            
            dto.setRespuesta(resp);
            return dto;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            dto.setRespuesta(exc.getMessage());
            return dto;
        }
    }
    
}
