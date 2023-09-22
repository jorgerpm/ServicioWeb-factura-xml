/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb;

import com.idebsystems.serviciosweb.dto.AnularArchivoXmlDTO;
import com.idebsystems.serviciosweb.dto.ArchivoSriDTO;
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
//            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", fileDto);
            //primero guardar el pdf RIDE
            service.guardarArchivoXml(fileDto.getPdfBase64());

            String pathSaveXml = service.guardarArchivoXml(fileDto.getXmlBase64());

            String resp = service.guardarXmlToDB(pathSaveXml, fileDto.getNombreArchivoXml(), fileDto.getNombreArchivoPdf(),
                    /*fileDto.getUrlArchivo(),*/ fileDto.getIdUsuarioCarga(), fileDto.getTipoDocumento(), true, null, null, null, true);
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
//            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", fileDto);
            String resp = service.guardarXmlToDB(fileDto.getXmlBase64(), fileDto.getNombreArchivoXml(), fileDto.getNombreArchivoPdf(),
                    /*fileDto.getUrlArchivo(),*/ fileDto.getIdUsuarioCarga(), fileDto.getTipoDocumento(), true, null, null, null, Boolean.TRUE);
            
//            LOGGER.log(Level.INFO, "el path es:::: {0}", service.getPathCarpetas());
            
            if(resp.contains("OK")){
                RespuestaDTO dto = new RespuestaDTO("");
                dto.setRespuesta("OK");
                dto.setDto(service.getPathCarpetas());//resp.split("~")[1]);
                return dto;
            }

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
            
            @QueryParam(value = "claveacceso") String claveAcceso,
            @QueryParam(value = "ruc") String ruc,
            @QueryParam(value = "tipodoc") String tipoDocumento,
            @QueryParam(value = "estadoSistema") String estadoSistema,
            
            @QueryParam(value = "desde") int desde,
            @QueryParam(value = "hasta") int hasta,
            @QueryParam(value = "seleccionados") boolean seleccionados,
            @QueryParam(value = "conDetalles") boolean conDetalles) throws Exception {
        try {
//            LOGGER.log(Level.INFO, "fechaInicio: {0}", fechaInicio);
//            LOGGER.log(Level.INFO, "fechaFinal: {0}", fechaFinal);
//            LOGGER.log(Level.INFO, "idUsuarioCarga: {0}", idUsuarioCarga);
//            
//            LOGGER.log(Level.INFO, "claveAcceso: {0}", claveAcceso);
//            LOGGER.log(Level.INFO, "ruc: {0}", ruc);
//            LOGGER.log(Level.INFO, "tipoDocumento: {0}", tipoDocumento);
//            LOGGER.log(Level.INFO, "estadoSistema: {0}", estadoSistema);
//            
//            LOGGER.log(Level.INFO, "desde: {0}", desde);
//            LOGGER.log(Level.INFO, "hasta: {0}", hasta);
//            LOGGER.log(Level.INFO, "seleccionados: {0}", seleccionados);
//            LOGGER.log(Level.INFO, "conDetalles: {0}", conDetalles);
            
            
            Long variable = new Date().getTime();
//            LOGGER.log(Level.INFO, String.valueOf(variable));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInit = sdf.parse(fechaInicio);
            Date dateFin = sdf.parse(fechaFinal);
            //buscar en la bdd los roles
            List<ArchivoXmlDTO> listaArchivo = service.listarPorFecha(dateInit, dateFin, idUsuarioCarga, 
                    claveAcceso, ruc, tipoDocumento, estadoSistema, desde, hasta, seleccionados, conDetalles);
//            LOGGER.log(Level.INFO, "tamaño: {0}", listaArchivo);

            return listaArchivo;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }

    
    @POST
    @Path("/cargarArchivoSri")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<ArchivoSriDTO> cargarArchivoSri(List<ArchivoSriDTO> lista) throws Exception {
        try {
            LOGGER.log(Level.INFO, "inicia carga archivosri: {0}");

            List<ArchivoSriDTO> respuesta = service.cargarArchivoSri(lista, lista.get(0).getIdUsuarioCarga());

            LOGGER.log(Level.INFO, "finaliza carga archivosri: {0}");
            
            return respuesta;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    @POST
    @Path("/anularArchivosXml")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public RespuestaDTO anularArchivosXml(List<ArchivoXmlDTO> lista) throws Exception {
        try {
//            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", lista);
//            lista.forEach(l -> LOGGER.log(Level.INFO, "uno: {0}", l));

            service.anularArchivosXml(lista);

            return new RespuestaDTO("OK");
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    @POST
    @Path("/anularXmlPorArchivo")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public RespuestaDTO anularXmlPorArchivo(AnularArchivoXmlDTO lista) throws Exception {
        try {
            LOGGER.log(Level.INFO, "entroooooooooooo: {0}", lista);

            String resp = service.anularXmlPorArchivo(lista);
            
            LOGGER.log(Level.INFO, "ya resp: {0}", resp);

            return new RespuestaDTO(resp);
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
