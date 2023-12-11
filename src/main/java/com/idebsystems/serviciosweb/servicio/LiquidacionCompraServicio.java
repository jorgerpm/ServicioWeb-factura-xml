/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. (C) 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.DocumentoReembolsosDAO;
import com.idebsystems.serviciosweb.dao.LiquidacionCompraDAO;
import com.idebsystems.serviciosweb.dto.FirmaDigitalDTO;
import com.idebsystems.serviciosweb.dto.LiquidacionCompraDTO;
import com.idebsystems.serviciosweb.entities.DocumentoReembolsos;
import com.idebsystems.serviciosweb.entities.LiquidacionCompra;
import com.idebsystems.serviciosweb.mappers.LiquidacionCompraMapper;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorge
 */
public class LiquidacionCompraServicio {
    
    private static final Logger LOGGER = Logger.getLogger(LiquidacionCompraServicio.class.getName());

    private final LiquidacionCompraDAO dao = new LiquidacionCompraDAO();
    
    public LiquidacionCompraDTO guardarLiquidacion(LiquidacionCompraDTO dto) throws Exception {
        try {
            LiquidacionCompra ent = LiquidacionCompraMapper.INSTANCE.dtoToEntity(dto);
            ent.setFechaModifica(new Date());
            
            //primero buscar si ya existe la liquidacion con el reembolso, para que no se duplique al mismo reembolso otra liquidacion
            LiquidacionCompra existe = dao.getLiquidacionPorReembolso(dto.getIdReembolso());
            if(Objects.nonNull(existe)){
                ent.setId(existe.getId());
            }
            
            ent = dao.guardarLiquidacion(ent);
            
            dto = LiquidacionCompraMapper.INSTANCE.entityToDto(ent);
            dto.setRespuesta("OK");
            
            //emviar el correo al usuario que cargo el reembolso
            DocumentoReembolsosDAO remdao = new DocumentoReembolsosDAO();
            DocumentoReembolsos dr = remdao.getDocumentosPorId(dto.getIdReembolso());
            CorreoServicio correo = new CorreoServicio();
            correo.enviaCorreoCargaLiquidacion(dr.getUsuarioCarga(), dr, ent);
            
            return dto;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            dto.setId(0L);
            dto.setRespuesta(exc.getMessage().replace("java.lang.Exception:", ""));
            return dto;
//            throw new Exception(exc);
        }
    }
    
    public LiquidacionCompraDTO firmarLiquidacion(LiquidacionCompraDTO dto, String claveFirma) throws Exception {
        try {
            LiquidacionCompra ent = dao.getLiquidacionPorReembolso(dto.getIdReembolso());
            
            ent.setEstado("FIRMADO");
            ent.setFechaModifica(new Date());

            //se debe enviar a firmar el documento con la firma del usuario que aprueba
            FirmaDigitalServicio fdao = new FirmaDigitalServicio();
            FirmaDigitalDTO firmaDTO = fdao.getFirmaActivaPorIdUsuario(dto.getIdUsuarioModifica(), false);
            
            //aqui se tiene el pdf para volver a firmar
            byte[] pdfBytes = Base64.getDecoder().decode(dto.getArchivoBase64());

            FirmarPdfServicio fsrv = new FirmarPdfServicio();
            
            
            byte[] pdfDosFirmas = null;
            if(firmaDTO.getTipoFirma() == 0 ){
                //se coloca la clave de la firma que se envia desde la pantalla
                pdfDosFirmas = fsrv.firmarConDigital(pdfBytes, firmaDTO, false, claveFirma, false);
            }
            if(firmaDTO.getTipoFirma() == 1 ){
                pdfDosFirmas = fsrv.firmarConImagen(pdfBytes, firmaDTO, false);
            }

            if (Objects.isNull(pdfDosFirmas)) {
                LiquidacionCompraDTO objDto = new LiquidacionCompraDTO();
                objDto.setRespuesta("ERROR: No se pudo firmar el documento.");
                return objDto;
            }
            
            LiquidacionCompra respuesta = dao.guardarLiquidacion(ent);
            LiquidacionCompraDTO objDto = LiquidacionCompraMapper.INSTANCE.entityToDto(respuesta);
//            objDto.setPathArchivo(urlCarpeta);
            objDto.setArchivoBase64(Base64.getEncoder().encodeToString(pdfDosFirmas));
            objDto.setRespuesta("OK");
            
            //enviar un correo electronica al usuario que genero el pdf reembolso, al usuario original
            CorreoServicio correo = new CorreoServicio();
            //correo.enviaCorreoLiquidacion(respuesta.getUsuarioCarga(), respuesta, objDto.getArchivoBase64());

            return objDto;

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            dto.setId(0L);
            dto.setRespuesta(exc.getMessage().replace("java.lang.Exception:", ""));
            return dto;
//            throw new Exception(exc);
        }
    }
    
}
