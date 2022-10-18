/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.ArchivoXmlDTO;
import com.idebsystems.serviciosweb.dto.RespuestaDTO;
import com.idebsystems.serviciosweb.servicio.ArchivoXmlServicio;
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
@Path("/archivoXml")
public class ArchivoXmlREST {

    private static final Logger LOGGER = Logger.getLogger(ArchivoXmlREST.class.getName());

    private final ArchivoXmlServicio service = new ArchivoXmlServicio();

    @POST
    @Path("/cargarArchivoXml")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public RespuestaDTO cargarArchivoXml(ArchivoXmlDTO fileDto) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", fileDto);
            //primero guardar el pdf RIDE
            service.guardarArchivoXml(fileDto.getPdfBase64());

            String pathSaveXml = service.guardarArchivoXml(fileDto.getXmlBase64());

            String resp = service.guardarXmlToDB(pathSaveXml, fileDto.getNombreArchivoXml(), fileDto.getNombreArchivoPdf(),
                    fileDto.getUrlArchivo(), fileDto.getIdUsuarioCarga(), fileDto.getTipoDocumento());
            return new RespuestaDTO(resp);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }

    @POST
    @Path("/guardarXmlDB")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public RespuestaDTO guardarXmlDB(ArchivoXmlDTO fileDto) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", fileDto);
            String resp = service.guardarXmlToDB(fileDto.getXmlBase64(), fileDto.getNombreArchivoXml(), fileDto.getNombreArchivoPdf(),
                    fileDto.getUrlArchivo(), fileDto.getIdUsuarioCarga(), fileDto.getTipoDocumento());

            return new RespuestaDTO(resp);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }

    @GET
    @Path("/listarArchivosXml")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<ArchivoXmlDTO> listarArchivosXml() throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}");
            //buscar en la bdd los roles
            return service.listarArchivosXml();
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @GET
    @Path("/listarPorFecha")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<ArchivoXmlDTO> listarPorFecha(@QueryParam(value = "fechaInicio") String fechaInicio,
                                              @QueryParam(value = "fechaFinal") String fechaFinal,
                                              @QueryParam(value = "idUsuarioCarga") Long idUsuarioCarga,
                                              @QueryParam(value = "desde") int desde,
                                              @QueryParam(value = "hasta") int hasta) throws Exception {
        try {
            LOGGER.log(Level.INFO, "fechaInicio: {0}", fechaInicio);
            LOGGER.log(Level.INFO, "fechaFinal: {0}", fechaFinal);
            LOGGER.log(Level.INFO, "idUsuarioCarga: {0}", idUsuarioCarga);
            LOGGER.log(Level.INFO, "desde: {0}", desde);
            LOGGER.log(Level.INFO, "hasta: {0}", hasta);
            Long variable = new Date().getTime();
            LOGGER.log(Level.INFO, String.valueOf(variable));
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInit = sdf.parse(fechaInicio);
            Date dateFin = sdf.parse(fechaFinal);
            //buscar en la bdd los roles
            List<ArchivoXmlDTO> listaArchivo = service.listarPorFecha(dateInit, dateFin, idUsuarioCarga, desde, hasta);
            LOGGER.log(Level.INFO, "tamaño: {0}", listaArchivo);
            
            return listaArchivo;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
