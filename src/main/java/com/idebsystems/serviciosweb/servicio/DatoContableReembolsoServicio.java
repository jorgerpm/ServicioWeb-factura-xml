/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. (C) 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.DatoContableReembolsoDAO;
import com.idebsystems.serviciosweb.dto.DatoContableReembolsoDTO;
import com.idebsystems.serviciosweb.entities.DatoContableReembolso;
import com.idebsystems.serviciosweb.mappers.DatoContableReembolsoMapper;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorge
 */
public class DatoContableReembolsoServicio {
    
    private static final Logger LOGGER = Logger.getLogger(DatoContableReembolsoServicio.class.getName());

    private final DatoContableReembolsoDAO dao = new DatoContableReembolsoDAO();
    
    public DatoContableReembolsoDTO guardarDatosContabilidad(DatoContableReembolsoDTO dto) throws Exception {
        try {
            DatoContableReembolso ent = dao.getDatoPorIdReembolso(dto.getIdReembolso());
            
            if(Objects.nonNull(ent)){
                dto.setId(ent.getId());
            }
            ent = DatoContableReembolsoMapper.INSTANCE.dtoToEntity(dto);
            ent.setFechaModifica(new Date());

            DatoContableReembolso respuesta = dao.guardarDatosContabilidad(ent);
            DatoContableReembolsoDTO objDto = DatoContableReembolsoMapper.INSTANCE.entityToDto(respuesta);
            objDto.setRespuesta("OK");
            
            return objDto;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    public DatoContableReembolsoDTO getDatoPorIdReembolso(long idReembolso) throws Exception {
        try {
            DatoContableReembolso ent = dao.getDatoPorIdReembolso(idReembolso);
            
            if(Objects.nonNull(ent)){
                return DatoContableReembolsoMapper.INSTANCE.entityToDto(ent);
            }
            else{
                return new DatoContableReembolsoDTO();
            }
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
