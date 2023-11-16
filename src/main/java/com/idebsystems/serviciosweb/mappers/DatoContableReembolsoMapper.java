/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. (C) 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.mappers;

import com.idebsystems.serviciosweb.entities.DatoContableReembolso;
import com.idebsystems.serviciosweb.dto.DatoContableReembolsoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author jorge
 */
@Mapper
public interface DatoContableReembolsoMapper {
    
    DatoContableReembolsoMapper INSTANCE = Mappers.getMapper(DatoContableReembolsoMapper.class);
            
    DatoContableReembolsoDTO entityToDto(DatoContableReembolso entity);
    DatoContableReembolso dtoToEntity(DatoContableReembolsoDTO dto);
}
