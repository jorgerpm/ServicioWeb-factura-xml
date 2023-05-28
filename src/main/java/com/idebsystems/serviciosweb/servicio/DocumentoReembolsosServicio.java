/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.DocumentoReembolsosDAO;
import com.idebsystems.serviciosweb.dao.UsuarioDAO;
import com.idebsystems.serviciosweb.dto.DocumentoReembolsosDTO;
import com.idebsystems.serviciosweb.dto.FirmaDigitalDTO;
import com.idebsystems.serviciosweb.entities.DocumentoReembolsos;
import com.idebsystems.serviciosweb.entities.Usuario;
import com.idebsystems.serviciosweb.mappers.DocumentoReembolsosMapper;
import com.idebsystems.serviciosweb.mappers.UsuarioMapper;
import com.idebsystems.serviciosweb.util.FechaUtil;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorge
 */
public class DocumentoReembolsosServicio {

    private static final Logger LOGGER = Logger.getLogger(DocumentoReembolsosServicio.class.getName());

    private final DocumentoReembolsosDAO dao = new DocumentoReembolsosDAO();

    public List<DocumentoReembolsosDTO> listarDocumentos(
            Date fechaInicio,
            Date fechaFinal,
            Long idUsuarioCarga,
            String estadoSistema,
            int desde,
            int hasta
    ) throws Exception {
        try {
            UsuarioDAO usdao = new UsuarioDAO();
            List<Usuario> uss = usdao.listarUsuarios();
            Usuario userCrg = uss.stream().filter(us -> Objects.equals(us.getId(), idUsuarioCarga)).findAny().orElse(new Usuario());
            
            Long idAprobador = null;
            if(userCrg.getIdRol() == 3){//si es un rol APROBADOR
                idAprobador = idUsuarioCarga;
            }
            
            List<DocumentoReembolsosDTO> lista = new ArrayList<>();

            List<Object> respuesta = dao.listarDocumentos(FechaUtil.fechaInicial(fechaInicio),
                    FechaUtil.fechaFinal(fechaFinal), idUsuarioCarga, estadoSistema, desde, hasta, idAprobador);

            //sacar los resultados retornados
            Integer totalRegistros = (Integer) respuesta.get(0);
            List<DocumentoReembolsos> data = (List<DocumentoReembolsos>) respuesta.get(1);

            data.forEach(obj -> {
                DocumentoReembolsosDTO dto = DocumentoReembolsosMapper.INSTANCE.entityToDto(obj);
                dto.setFechaCargaLong(obj.getFechaCarga().getTime());
                if (Objects.nonNull(obj.getFechaAutoriza())) {
                    dto.setFechaAutorizaLong(obj.getFechaAutoriza().getTime());
                }

                Usuario user = uss.stream().filter(u -> Objects.equals(u.getId(), obj.getUsuarioCarga())).findAny().orElse(new Usuario());
                dto.setUsuario(UsuarioMapper.INSTANCE.entityToDto(user));
                dto.setTotalRegistros(totalRegistros);

                lista.add(dto);
            });

            return lista;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }

    public DocumentoReembolsosDTO guardarDocumentoReembolsos(DocumentoReembolsosDTO dto) throws Exception {
        try {
            DocumentoReembolsos ent = DocumentoReembolsosMapper.INSTANCE.dtoToEntity(dto);
            ent.setFechaCarga(new Date());

            DocumentoReembolsos respuesta = dao.guardarDocumentoReembolsos(ent, "", null, false);
            DocumentoReembolsosDTO objDto = DocumentoReembolsosMapper.INSTANCE.entityToDto(respuesta);
            return objDto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }

    public List<DocumentoReembolsosDTO> getDocumentosPorIdUsuario(Long idUsuario) throws Exception {
        try {
            List<DocumentoReembolsosDTO> lista = new ArrayList<>();

            List<DocumentoReembolsos> data = dao.getDocumentosPorIdUsuario(idUsuario);

            UsuarioDAO usdao = new UsuarioDAO();
            List<Usuario> uss = usdao.listarUsuarios();

            data.forEach(obj -> {
                DocumentoReembolsosDTO dto = DocumentoReembolsosMapper.INSTANCE.entityToDto(obj);
                dto.setFechaCargaLong(obj.getFechaCarga().getTime());
                if (Objects.nonNull(obj.getFechaAutoriza())) {
                    dto.setFechaAutorizaLong(obj.getFechaAutoriza().getTime());
                }

                Usuario user = uss.stream().filter(u -> Objects.equals(u.getId(), obj.getUsuarioCarga())).findAny().orElse(new Usuario());
                dto.setUsuario(UsuarioMapper.INSTANCE.entityToDto(user));

                lista.add(dto);
            });

            return lista;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
        }
    }

    public DocumentoReembolsosDTO aprobarDocumentoReembolsos(DocumentoReembolsosDTO dto) throws Exception {
        try {
            DocumentoReembolsos ent = dao.getDocumentosPorId(dto.getId());
            ent.setEstado(dto.getEstado());
            ent.setFechaAutoriza(new Date());
            ent.setUsuarioAutoriza(dto.getUsuarioAutoriza());
            if (dto.getEstado().equalsIgnoreCase("RECHAZADO")) {
                ent.setRazonRechazo(dto.getRazonRechazo());
            }

            //se debe enviar a firmar el documento con la firma del usuario que aprueba
            FirmaDigitalServicio fdao = new FirmaDigitalServicio();
            FirmaDigitalDTO firmaDTO = fdao.getFirmaActivaPorIdUsuario(dto.getIdUsuarioAutoriza());
            //al final el documento debe quedar con dos firmas
            dto.getArchivoBase64();//aqui se tiene el pdf para volver a firmar
            byte[] pdfBytes = Base64.getDecoder().decode(dto.getArchivoBase64());

            FirmarPdfServicio fsrv = new FirmarPdfServicio();
            byte[] pdfDosFirmas = null;
            if(firmaDTO.getTipoFirma() == 0 ){
                pdfDosFirmas = fsrv.firmarConDigital(pdfBytes, firmaDTO, true);
            }
            if(firmaDTO.getTipoFirma() == 1 ){
                pdfDosFirmas = fsrv.firmarConImagen(pdfBytes, firmaDTO, true);
            }

            if (Objects.isNull(pdfDosFirmas)) {
                DocumentoReembolsosDTO objDto = new DocumentoReembolsosDTO();
                objDto.setRespuesta("ERROR: No se pudo firmar el documento.");
                return objDto;
            }

            DocumentoReembolsos respuesta = dao.guardarDocumentoReembolsos(ent, ent.getIdsXml(), null, false);
            DocumentoReembolsosDTO objDto = DocumentoReembolsosMapper.INSTANCE.entityToDto(respuesta);

            objDto.setArchivoBase64(Base64.getEncoder().encodeToString(pdfDosFirmas));
            objDto.setRespuesta("OK");
            
            //enviar un correo electronica al usuario que genero el pdf reembolso, al usuario original
            CorreoServicio correo = new CorreoServicio();
            correo.enviaCorreoApruebaRechaza(respuesta.getUsuarioCarga(), respuesta);

            return objDto;

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            dto.setId(0L);
            dto.setRespuesta(exc.getMessage().replace("java.lang.Exception:", ""));
            return dto;
//            throw new Exception(exc);
        }
    }
    

    public DocumentoReembolsosDTO guardarDatosContabilidad(DocumentoReembolsosDTO dto) throws Exception {
        try {
            DocumentoReembolsos ent = dao.getDocumentosPorId(dto.getId());
            
            ent.setJustificativos(dto.getJustificativos());
            ent.setBatchIngresoLiquidacion(dto.getBatchIngresoLiquidacion());
            ent.setBatchDocumentoInterno(dto.getBatchDocumentoInterno());
            ent.setP3(dto.getP3());
            ent.setP4(dto.getP4());
            ent.setP5(dto.getP5());
            ent.setPhne(dto.getPhne());
            ent.setCruce1(dto.getCruce1());
            ent.setCruce2(dto.getCruce2());
            ent.setTipoDocumento(dto.getTipoDocumento());
            ent.setNumeroDocumento(dto.getNumeroDocumento());
            ent.setNumeroRetencion(dto.getNumeroRetencion());

            DocumentoReembolsos respuesta = dao.guardarDocumentoReembolsos(ent, "", null, false);
            DocumentoReembolsosDTO objDto = DocumentoReembolsosMapper.INSTANCE.entityToDto(respuesta);
            objDto.setRespuesta("OK");
            
            return objDto;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    public String enviarCorreoJustificacion(DocumentoReembolsosDTO dto) throws Exception {
        try {
            DocumentoReembolsos ent = dao.getDocumentosPorId(dto.getId());
            
            CorreoServicio correo = new CorreoServicio();
            correo.enviaCorreoSolicitaJustificacion(ent.getUsuarioCarga(), ent, dto.getArchivoBase64());
            
            return "OK";
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            return exc.getMessage();
        }
    }
    
    
    public String cargarJustificacion(DocumentoReembolsosDTO dto) throws Exception {
        try {
            DocumentoReembolsos ent = dao.getDocumentosPorId(dto.getId());
            ent.setJustificacionBase64(dto.getJustificacionBase64());
            
            DocumentoReembolsos respuesta = dao.guardarDocumentoReembolsos(ent, "", null, false);
            
            CorreoServicio correo = new CorreoServicio();
            correo.enviaCorreoCargaJustificacion(ent.getUsuarioCarga(), ent, dto.getJustificacionBase64());
            
            return "OK";
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            return exc.getMessage();
        }
    }
    
}
