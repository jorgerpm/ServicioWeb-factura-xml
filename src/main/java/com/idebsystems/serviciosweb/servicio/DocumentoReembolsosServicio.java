/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.DocumentoReembolsosDAO;
import com.idebsystems.serviciosweb.dao.TipoReembolsoDAO;
import com.idebsystems.serviciosweb.dao.UsuarioDAO;
import com.idebsystems.serviciosweb.dto.DocumentoReembolsosDTO;
import com.idebsystems.serviciosweb.dto.FirmaDigitalDTO;
import com.idebsystems.serviciosweb.entities.DocumentoReembolsos;
import com.idebsystems.serviciosweb.entities.TipoReembolso;
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

    public List<DocumentoReembolsosDTO> listarDocumentos(Date fechaInicio, Date fechaFinal, Long idUsuarioCarga,
            String estadoSistema, int desde, int hasta, String numeroRC, String tipoReembolso, String numeroReembolso,
            String numeroLC) throws Exception {
        
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
                    FechaUtil.fechaFinal(fechaFinal), idUsuarioCarga, estadoSistema, desde, hasta, idAprobador, numeroRC, 
                    tipoReembolso, numeroReembolso, numeroLC);

            //sacar los resultados retornados
            Integer totalRegistros = (Integer) respuesta.get(0);
            List<DocumentoReembolsos> data = (List<DocumentoReembolsos>) respuesta.get(1);
            
            TipoReembolsoDAO trdao = new TipoReembolsoDAO();
            List<TipoReembolso> listTiposRem = trdao.listarTipoReembolso(null);

            data.forEach(obj -> {
                DocumentoReembolsosDTO dto = DocumentoReembolsosMapper.INSTANCE.entityToDto(obj);
                dto.setFechaCargaLong(obj.getFechaCarga().getTime());
                if (Objects.nonNull(obj.getFechaAutoriza())) {
                    dto.setFechaAutorizaLong(obj.getFechaAutoriza().getTime());
                }
                if (Objects.nonNull(obj.getFechaProcesa())) {
                    dto.setFechaProcesaLong(obj.getFechaProcesa().getTime());
                }

                Usuario user = uss.stream().filter(u -> Objects.equals(u.getId(), obj.getUsuarioCarga())).findAny().orElse(new Usuario());
                dto.setUsuario(UsuarioMapper.INSTANCE.entityToDto(user));
                dto.setTotalRegistros(totalRegistros);
                
                Usuario userAprob = uss.stream().filter(u -> Objects.equals(u.getId(), obj.getIdAprobador())).findAny().orElse(new Usuario());
                dto.setAprobador(userAprob.getNombre());
                
                try{
                    dto.setTipoReembolsoNombre(listTiposRem.stream().filter(tr -> tr.getId() == Long.parseLong(obj.getTipoReembolso())).findFirst().orElse(new TipoReembolso()).getTipo());
                }catch(NumberFormatException nfe){
                    dto.setTipoReembolsoNombre(obj.getTipoReembolso());
                }

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
                if (Objects.nonNull(obj.getFechaProcesa())) {
                    dto.setFechaProcesaLong(obj.getFechaProcesa().getTime());
                }

                Usuario user = uss.stream().filter(u -> Objects.equals(u.getId(), obj.getUsuarioCarga())).findAny().orElse(new Usuario());
                dto.setUsuario(UsuarioMapper.INSTANCE.entityToDto(user));
                
                Usuario userAprob = uss.stream().filter(u -> Objects.equals(u.getId(), obj.getIdAprobador())).findAny().orElse(new Usuario());
                dto.setAprobador(userAprob.getNombre());

                lista.add(dto);
            });

            return lista;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
        }
    }

    public DocumentoReembolsosDTO aprobarDocumentoReembolsos(DocumentoReembolsosDTO dto, String claveFirma, boolean terceraFirma) throws Exception {
        try {
            DocumentoReembolsos ent = dao.getDocumentosPorId(dto.getId());
            
            //en base al estado se debe cambiar la url de la ubicacion del archivo pdf. inicialmente esta dentro 
            //de POR_AUTORIZAR, y aqui debe cambiar por el estado
            String urlCarpeta = ent.getPathArchivo().replace(ent.getEstado(), dto.getEstado());
            ent.setPathArchivo(urlCarpeta);
            
            //ahora colocar el estado que viene desde pantalla
            ent.setEstado(dto.getEstado());
            
            if (dto.getEstado().equalsIgnoreCase("RECHAZADO")) {
                ent.setRazonRechazo(dto.getRazonRechazo());
            }
            //si el ESTADO = aprobado se guarda solo el del usuarioproueba
            if (dto.getEstado().equalsIgnoreCase("APROBADO")) {
                ent.setFechaAutoriza(new Date());
                ent.setUsuarioAutoriza(dto.getUsuarioAutoriza());
            }
            else{//si el estadoes procesado se guarda el usuario de procesado y no autorizado
                ent.setUsuarioProcesa(dto.getUsuarioAutoriza());
                ent.setFechaProcesa(new Date());
            }

            //se debe enviar a firmar el documento con la firma del usuario que aprueba
            FirmaDigitalServicio fdao = new FirmaDigitalServicio();
            FirmaDigitalDTO firmaDTO = fdao.getFirmaActivaPorIdUsuario(dto.getIdUsuarioAutoriza(), false);
            //al final el documento debe quedar con dos firmas
            dto.getArchivoBase64();//aqui se tiene el pdf para volver a firmar
            byte[] pdfBytes = Base64.getDecoder().decode(dto.getArchivoBase64());

            FirmarPdfServicio fsrv = new FirmarPdfServicio();
            
            //primero se agrega la imagen del rechazo y al final de todo se firma el documento.
            //si es rechazado se agrega una imagen que digarechazado en tood el documento
            if (dto.getEstado().equalsIgnoreCase("RECHAZADO")) {
                pdfBytes = fsrv.agregarImagenRechazo(pdfBytes, ent.getRazonRechazo(), ent.getTipoReembolso());
            }
            
            
            byte[] pdfDosFirmas = null;
            if(firmaDTO.getTipoFirma() == 0 ){
                //se coloca la clave de la firma que se envia desde la pantalla
                if(terceraFirma){
                    pdfDosFirmas = fsrv.firmarConDigital(pdfBytes, firmaDTO, false, claveFirma, true);
                    ent.setTresFirmas(1);
                }
                else
                    pdfDosFirmas = fsrv.firmarConDigital(pdfBytes, firmaDTO, true, claveFirma, false);
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
            objDto.setPathArchivo(urlCarpeta);
            objDto.setArchivoBase64(Base64.getEncoder().encodeToString(pdfDosFirmas));
            objDto.setRespuesta("OK");
            
            //enviar un correo electronica al usuario que genero el pdf reembolso, al usuario original
            CorreoServicio correo = new CorreoServicio();
            correo.enviaCorreoApruebaRechaza(respuesta.getUsuarioCarga(), respuesta, objDto.getArchivoBase64());

            return objDto;

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            dto.setId(0L);
            dto.setRespuesta(exc.getMessage().replace("java.lang.Exception:", ""));
            return dto;
//            throw new Exception(exc);
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
            ent.setTipoJustificacionBase64(dto.getTipoJustificacionBase64());
            
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
